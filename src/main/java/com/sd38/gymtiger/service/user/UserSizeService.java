package com.sd38.gymtiger.service.user;

import com.sd38.gymtiger.model.Size;

import java.util.List;

public interface UserSizeService {
    List<Size> getSizeByProductId(Integer id);
}
