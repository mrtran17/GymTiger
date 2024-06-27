package com.sd38.gymtiger.service.impl.user;

import com.sd38.gymtiger.repository.user.UserColorRepository;
import com.sd38.gymtiger.model.Color;
import com.sd38.gymtiger.service.user.UserColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserColorServiceImpl implements UserColorService {
    @Autowired
    private UserColorRepository userColorRepository;

    @Override
    public List<Color> getColorByProductId(Integer id) {
        return userColorRepository.getColorByProductId(id);
    }
}
