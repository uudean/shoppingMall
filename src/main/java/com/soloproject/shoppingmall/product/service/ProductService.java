package com.soloproject.shoppingmall.product.service;

import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.image.entity.Image;
import com.soloproject.shoppingmall.image.service.ImageService;
import com.soloproject.shoppingmall.product.dto.ProductResponseDto;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.mapper.ProductMapper;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import com.soloproject.shoppingmall.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.Cursor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RedisUtil redisUtil;
    private final ImageService imageService;

    @Transactional
    public Product createProduct(Product product, List<MultipartFile> image) {

        List<Image> images = imageService.uploadImage(image,product);
        product.setImages(images);

        return productRepository.save(product);
    }

    @CacheEvict(cacheNames = "ProductCache", cacheManager = "RedisCacheManager", allEntries = true)
    @Transactional
    public Product updateProduct(Product product) {

        Product findProduct = findProduct(product.getProductId());

        Optional.ofNullable(product.getName()).ifPresent(findProduct::setName);
        Optional.ofNullable(product.getCategory()).ifPresent(findProduct::setCategory);
        Optional.ofNullable(product.getDescription()).ifPresent(findProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(findProduct::setPrice);
        Optional.ofNullable(product.getStock()).ifPresent(findProduct::setStock);
        Optional.ofNullable(product.getModifiedAt()).ifPresent(localDateTime -> findProduct.setModifiedAt(LocalDateTime.now()));

        return productRepository.save(findProduct);

    }

    // 상품 조회 캐시사용
    @Cacheable(value = "ProductCache", cacheManager = "RedisCacheManager")
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(long productId) {

        Product product = findProduct(productId);

        ProductResponseDto response = productMapper.productToProductResponseDto(product);

        return response;
    }

    // 조회 할때 마다 Redis view + 1 씩 증가
    @Async
    public void viewCount(long productId) {

        String key = "ProductView : " + productId;

        redisUtil.setIncrement(key, 1L);

    }

    // 30분 마다 조회수 DB 반영
    @CacheEvict(cacheNames = "ProductCache", cacheManager = "RedisCacheManager", allEntries = true)
    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void addViewCount() {

        String pattern = "ProductView*";

        Cursor<byte[]> keys = redisUtil.getScanKeys(pattern);

        while (keys.hasNext()) {

            byte[] keyByte = keys.next();
            String key = new String(keyByte);
            long productId = Long.parseLong(key.replace("ProductView : ", ""));
            Product product = productRepository.findById(productId).orElseThrow();
            Object value = redisUtil.get(key);
            long viewCount = Long.parseLong(value.toString());
            product.setViews(viewCount+product.getViews());

            productRepository.save(product);

            redisUtil.delete(key);
        }
        keys.close();
        log.info("조회 수 수정이 완료 되었습니다. ");
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public Page<Product> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(pageRequest);
    }

    // 상품 목록 조회
    @Transactional(readOnly = true)
    public Page<Product> getCategoryProducts(int page, int size, int type, Product.Category category) throws Exception {

        PageRequest pageRequest = PageRequest.of(page,size);

        // 최신순
        if (type == 1) {
            return productRepository.findByCategory(pageRequest.withSort(Sort.by("createdAt").descending()), category);
        }
        // 판매량순
        else if (type == 2) {
            return productRepository.findByCategory(pageRequest.withSort(Sort.by("totalSales").descending()), category);
        }
        // 높은 가격순
        else if (type == 3) {
            return productRepository.findByCategory(pageRequest.withSort(Sort.by("price").descending()), category);
        }
        // 낮은 가격순
        else if (type == 4) {
            return productRepository.findByCategory(pageRequest.withSort(Sort.by("price").ascending()), category);
        }
        else throw new Exception("올바른 타입을 해야합니다.");
    }

    @Transactional(readOnly = true)
    public List<Product> recentProducts(List<Long> productIds){

        List<Product> recentProducts = new ArrayList<>();

        for (Long productId : productIds) {
            Product recentProduct = productRepository.findById(productId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
            recentProducts.add(recentProduct);
        }

        return recentProducts;
    }

    // 상품 삭제
    @CacheEvict(cacheNames = "ProductCache", cacheManager = "RedisCacheManager", allEntries = true)
    @Transactional
    public void deleteProduct(long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
    }

    private Product findProduct(long productId) {
        return productRepository.findById(productId).orElseThrow(()->new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
    }
}
