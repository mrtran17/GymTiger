/*
 *  © 2023 Nyaruko166
 */

package com.sd38.gymtiger.controller.admin;

import com.sd38.gymtiger.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tiger/account")
public class RestAccountController {

    @Autowired
    private ProductDetailService productDetailService;

//    @PostMapping("/api/productFilter")
//    public ResponseEntity<?> searchProducts(@RequestParam("keyword") String keywordProduct,
//                                            Model model) {
//        Pageable pageable = Pageable.ofSize(10);
//        List<ProductDetail> lstPro = new ArrayList<>();
//        if (keywordProduct.isBlank()) {
//            model.addAttribute("lstPro", null);
//            return ResponseEntity.ok("Notabc");
//        }
//        lstPro = productDetailService.findAllByProductNameAndStatus(keywordProduct, 1, pageable).getContent();
//        model.addAttribute("lstPro", lstPro);
//        model.addAttribute("keywordProduct", keywordProduct);
//
//        return ResponseEntity.ok("abc");
//    }

}
