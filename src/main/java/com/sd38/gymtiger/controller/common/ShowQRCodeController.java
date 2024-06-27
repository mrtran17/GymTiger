package com.sd38.gymtiger.controller.common;

import com.sd38.gymtiger.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("qrcode")
@RequiredArgsConstructor
public class ShowQRCodeController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getQRCodeByProductDetailId(@PathVariable Integer id) throws IOException {
        byte[] qrCodeData = productDetailService.getProductDetail(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeData);
    }
}
