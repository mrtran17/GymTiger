package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.Cart;
import com.sd38.gymtiger.model.ProductDetail;
import com.sd38.gymtiger.model.SessionCart;

public interface CartService {
    boolean addToCart(ProductDetail productDetail, Integer quantity, String email);

    boolean updateCart(ProductDetail productDetail, Integer quantity, String email);

    Cart removeFromCart(ProductDetail productDetail, String email);

    SessionCart addToCartSession(SessionCart sessionCart, ProductDetail productDetail, Integer quantity);

    boolean updateCartSession(SessionCart sessionCart, ProductDetail productDetail, Integer quantity);

    void reloadCartDetail(Cart cart);

    void reloadCartDetailSession(SessionCart sessionCart);

    SessionCart removeFromCartSession(SessionCart sessionCart, ProductDetail productDetail);

    Cart combineCart(SessionCart sessionCart, String email);

    Cart getCart(String email);

    void deleteCartById(Integer id);
}
