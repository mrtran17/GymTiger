package com.sd38.gymtiger.service.user;

import com.sd38.gymtiger.model.Color;

import java.util.List;

public interface UserColorService {
    List<Color> getColorByProductId(Integer id);
}
