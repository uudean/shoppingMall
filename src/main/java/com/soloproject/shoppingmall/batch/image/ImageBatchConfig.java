package com.soloproject.shoppingmall.batch.image;

import com.amazonaws.services.s3.AmazonS3;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class ImageBatchConfig {

    private final ImageRepository imageRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Bean
    public Job imageJob(JobRepository jobRepository,
                        Step step1) {
        return new JobBuilder("imageJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }


    @Bean
    @JobScope
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager platformTransactionManager,
                      ItemReader imageReader,
                      ItemProcessor imageProcessor,
                      ItemWriter imageWriter) {
        return new StepBuilder("step1", jobRepository)
                .chunk(5, platformTransactionManager)
                .reader(imageReader)
                .processor(imageProcessor)
                .writer(imageWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Image> imageReader() {
        log.info("---- 고아 이미지 DB 탐색 시작 ----");
        return new RepositoryItemReaderBuilder<Image>()
                .name("imageReader")
                .repository(imageRepository)
                .methodName("findAllByProductIdIsNull")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("imageId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Image, List<Image>> imageProcessor() {
        return item -> {
            log.info("---- 고아 이미지 데이터 필터링 시작 ----");
            List<Image> images = new ArrayList<>();

            if (item.getProduct() == null) {
                images.add(item);
                return images;
            } else return null;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<List<Image>> imageWriter() {
        return chunk -> {
            log.info("---- S3 이미지 삭제 시작 ----");
            String folderName = "image/";

            for (List<Image> images : chunk) {
                for (Image image : images) {
                    amazonS3.deleteObject(bucket, folderName + image.getImageName());
                    imageRepository.deleteById(image.getImageId());
                }
            }

            log.info("---- {} 개 이미지 삭제완료 ----",chunk.size());

        };
    }
}
