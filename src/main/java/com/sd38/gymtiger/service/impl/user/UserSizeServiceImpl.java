package com.sd38.gymtiger.service.impl.user;

import com.sd38.gymtiger.repository.user.UserSizeRepository;
import com.sd38.gymtiger.model.Size;
import com.sd38.gymtiger.service.user.UserSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSizeServiceImpl implements UserSizeService {
    @Autowired
    private UserSizeRepository userSizeRepository;

    @Override
    public List<Size> getSizeByProductId(Integer id) {
        return userSizeRepository.getSizeByProductId(id);
    }
}
