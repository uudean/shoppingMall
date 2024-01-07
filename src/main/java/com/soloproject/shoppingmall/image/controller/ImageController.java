package com.soloproject.shoppingmall.image.controller;

import com.soloproject.shoppingmall.image.mapper.ImageMapper;
import com.soloproject.shoppingmall.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/image")
@RestController
public class ImageController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PostMapping("/upload/{product-id}")
    public ResponseEntity<List<String>> uploadImage(@RequestPart List<MultipartFile> multipartFiles,
                                                    @PathVariable("product-id") long productId) throws Exception{
        return new ResponseEntity<>(imageService.uploadImage(multipartFiles,productId), HttpStatus.OK);
    }
}
