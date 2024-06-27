package com.sd38.gymtiger.service;

import com.sd38.gymtiger.response.CustomerResponse;
import com.sd38.gymtiger.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface AccountService {
    List<Account> getAll();

    Page<Account> getPage(Integer page);

    String add(Account account, MultipartFile avt, Integer roleId);

    String update(Account account, MultipartFile avt, Integer roleId, Integer id);

    Account delete(Integer id);

    Page<Account> search(String name, Date birthday, String email, String phoneNumber, Integer roleId, int page);

    Account findOne(Integer id);

    Account findFirstByEmail(String email);

    byte[] getImageByAccountId(Integer accountId);

    Integer registerUser(Account user);

    Page<CustomerResponse> searchCustomer(String keyword, Pageable pageable);
}
