package com.sd38.gymtiger.controller.common;

import com.sd38.gymtiger.service.AccountService;
import com.sd38.gymtiger.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ShowImageController {
    private final ImageService imageService;

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) throws IOException {
        byte[] imageData = imageService.getImage(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<byte[]> getImageDeleted(@PathVariable Integer id) throws IOException {
        byte[] imageData = imageService.getImageDeleted(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Integer productId) throws IOException {
        byte[] imageData = imageService.getImageByProductId(productId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @GetMapping("/product/deleted/{productId}")
    public ResponseEntity<byte[]> getImageDeletedByProductId(@PathVariable Integer productId) throws IOException {
        byte[] imageData = imageService.getImageDeletedByProductId(productId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<byte[]> getImageByAccountId(@PathVariable Integer accountId) {
        byte[] imageData = accountService.getImageByAccountId(accountId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }
}
