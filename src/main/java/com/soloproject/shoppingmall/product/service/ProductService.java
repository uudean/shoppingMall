package com.soloproject.shoppingmall.product.service;

import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.mapper.ProductMapper;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import com.soloproject.shoppingmall.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisUtil redisUtil;
    private final ProductMapper productMapper;
    private final RedisTemplate redisTemplate;

    @Transactional
    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Product product) {

        Product findProduct = findProduct(product.getProductId());

        Optional.ofNullable(product.getName()).ifPresent(findProduct::setName);
        Optional.ofNullable(product.getDescription()).ifPresent(findProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(findProduct::setPrice);
        Optional.ofNullable(product.getStock()).ifPresent(findProduct::setStock);
        Optional.ofNullable(product.getModifiedAt()).ifPresent(localDateTime -> findProduct.setModifiedAt(LocalDateTime.now()));

        return productRepository.save(findProduct);

    }
    // 상품 조회 캐시사용
    @Cacheable(value = "ProductCache", cacheManager = "RedisCacheManager")
    @Transactional
    public ProductResponseDto getProduct(long productId) {

        Product product = findProduct(productId);

        ProductResponseDto response = productMapper.productToProductResponseDto(product);

        viewCount(productId);

        return response;
    }
    // 조회 할때 마다 Redis view + 1 씩 증가
    @Async
    public void viewCount(long productId) {

        String key = "ProductView : " + productId;

        redisUtil.setIncrement(key, 1L);

    }

    // 30분 마다 조회수 DB 반영
    @Scheduled(cron = "0 */30 * * * *",zone = "Asia/Seoul")
    @Transactional
    public void addViewCount() {

        ScanOptions scanOptions = ScanOptions.scanOptions().match("ProductView*").build();
        Cursor<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);

        while (keys.hasNext()){

           byte[] keyByte = keys.next();
           String key = new String(keyByte);
            long productId = Long.parseLong(key.replace("ProductView : ",""));
            Product product = productRepository.findById(productId).orElseThrow();
            Object value = redisUtil.get(key);
            long viewCount = Long.parseLong(value.toString());
            product.setViews(viewCount);

            productRepository.save(product);

            redisUtil.delete(key);
        }
        log.info("조회 수 수정이 완료 되었습니다. ");
    }

    // 상품 전체 목록
    @Transactional(readOnly = true)
    public Page<Product> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(pageRequest);
    }

    // 인기 상품순
    @Transactional(readOnly = true)
    public Page<Product> productRanking(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("totalSales").descending());
        return productRepository.findAll(pageRequest);
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
    }

    private Product findProduct(long productId) {
        return productRepository.findById(productId).orElseThrow();
    }
}
