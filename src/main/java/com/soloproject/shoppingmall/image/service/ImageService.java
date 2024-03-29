package com.soloproject.shoppingmall.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.image.repository.ImageRepository;
import com.soloproject.shoppingmall.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public List<Image> uploadImage(List<MultipartFile> multipartFiles, Product product) {

        List<Image> images = new ArrayList<>();

        String dirName = "image/";

        for (MultipartFile file : multipartFiles) {

            String fileName = createFileName(file.getOriginalFilename());
            String folder = dirName + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            String bucketUrl = amazonS3.getUrl(bucket, folder).toString();

            Image image = Image.builder()
                    .imageName(fileName)
                    .originalName(file.getOriginalFilename())
                    .url(bucketUrl)
                    .product(product)
                    .build();

            imageRepository.save(image);

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, folder, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            images.add(image);
        }
        return images;
    }

    public String createFileName(String fileName) {

        return UUID.randomUUID().toString().concat(getFileExtension(fileName));

    }

    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
