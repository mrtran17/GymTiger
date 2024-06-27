package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Page<Role> getPage(Integer page);

    Role add(Role role);

    Role update(Role role, Integer id);

    Role delete(Integer id);

    Role getOne(Integer id);

    Page<Role> search(String name, int page);
}
