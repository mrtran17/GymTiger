package com.sd38.gymtiger.response;

import com.sd38.gymtiger.model.ProductDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProductDetails {

    private List<ProductDetail> listProductDetail;
}
