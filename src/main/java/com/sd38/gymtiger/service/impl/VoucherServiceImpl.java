package com.sd38.gymtiger.service.impl;

import com.sd38.gymtiger.repository.VoucherRepository;
import com.sd38.gymtiger.dto.admin.thongke.VoucherSearchDTO;
import com.sd38.gymtiger.model.Voucher;
import com.sd38.gymtiger.service.VoucherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    private Boolean checkName(String name) {
        // Loại bỏ dấu cách đầu tiên
        name = name.replaceFirst("^\\s+", "");

        // Loại bỏ các dấu cách khi có hai dấu cách trở lên liền nhau
        name = name.replaceAll("\\s{2,}", " ");
        Voucher voucher = voucherRepository.findByNameAndStatus(name, 1);
        if (voucher != null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void scheduleUpdateExpiredVouchers() {
        List<Voucher> expiredVouchers = getExpiredVouchers();
        for (Voucher voucher : expiredVouchers) {
            updateExpiredVoucher(voucher);
        }
    }

    @Override
    public List<Voucher> getExpiredVouchers() {
        Date currentDate = new Date();
        return voucherRepository.findByEndDateBeforeAndStatus(currentDate, 1);
    }

    @Override
    public void updateExpiredVoucher(Voucher voucher) {
        voucher.setStatus(0);
        try {
            voucherRepository.save(voucher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<VoucherSearchDTO> searchVoucher(String code, Date ngayTaoStart, Date ngayTaoEnd, Integer status, String name, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return voucherRepository.searchVoucher(code, ngayTaoStart, ngayTaoEnd, status, name, pageable);
    }

    @Override
    public List<Voucher> ActiveVoucher() {
        scheduleUpdateExpiredVouchers();
        return voucherRepository.findAllByStatusOrderByIdDesc(1);
    }

    @Override
    public List<Voucher> getAll() {
        scheduleUpdateExpiredVouchers();
        return voucherRepository.findAllByStatusOrderByIdDesc(1);
    }

    @Override
    public Page<Voucher> getAll(Integer page) {
        scheduleUpdateExpiredVouchers();
        Pageable pageable = PageRequest.of(page, 5);
        return voucherRepository.findAllByStatusOrderByIdDesc(pageable);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updatePromotionStatus() {
        List<Voucher> vouchersToUpdate = voucherRepository.findAllByStatusOrderByIdDesc(2);
        Date currentDate = new Date();

        for (Voucher voucher : vouchersToUpdate) {
            if (voucher.getStartDate().before(currentDate)) {
                voucher.setStatus(1);
                voucherRepository.save(voucher);
            }
        }
    }

    public String generateCode() {
        Voucher voucher = voucherRepository.findTopByOrderByIdDesc();
        if (voucher == null) {
            return "M00001";
        }
        Integer idFinalPresent = voucher.getId() + 1;
        String code = String.format("%05d", idFinalPresent);
        return "M" + code;
    }

    @Override
    public Boolean add(Voucher voucher) {
        if (checkName(voucher.getName())) {
            voucher.setCreateDate(new Date());
            voucher.setUpdateDate(new Date());
            voucher.setQuantity(5000);
            voucher.setMinimumPrice(BigDecimal.valueOf(1.0));
            voucher.updateStatus();
            voucher.setCode(generateCode());
            voucherRepository.save(voucher);
            return true;
        }
        return false;
    }

    @Override
    public Boolean update(Voucher voucher, Integer id) {
        Optional<Voucher> optional = voucherRepository.findById(id);
        if (optional.isPresent()) {
            Voucher updateVoucher = optional.get();
            if (updateVoucher.getName().equalsIgnoreCase(voucher.getName())) {
                Voucher oldVoucher = optional.get();
                voucher.setId(oldVoucher.getId());
                voucher.setCode(oldVoucher.getCode());
                voucher.setCreateDate(oldVoucher.getCreateDate());
                voucher.setUpdateDate(new Date());
                voucher.setQuantity(5000);
                voucher.setMinimumPrice(BigDecimal.valueOf(1.0));
                voucher.setStatus(oldVoucher.getStatus());

                voucher.updateStatus();
                voucherRepository.save(voucher);
                return true;
            } else {
                if (checkName(voucher.getName())) {
                    Voucher oldVoucher = optional.get();
                    voucher.setId(oldVoucher.getId());
                    voucher.setCode(oldVoucher.getCode());
                    voucher.setCreateDate(oldVoucher.getCreateDate());
                    voucher.setUpdateDate(new Date());
                    voucher.setStatus(oldVoucher.getStatus());
                    voucher.updateStatus();
                    voucherRepository.save(voucher);
                    return true;
                }else {
                    return false;
                }
            }
        }else{
                return null;
            }
        }


    @Override
    public Voucher delete(Integer id) {
        Optional<Voucher> optional = voucherRepository.findById(id);
        if (optional.isPresent()) {
            Voucher voucher = optional.get();
            voucher.setStatus(0);
            return voucherRepository.save(voucher);
        } else {
            return null;
        }
    }

    @Override
    public Voucher getOne(Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Voucher> search(String name, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return voucherRepository.findAllByNameContainsAndStatusOrderByIdDesc(name, 1, pageable);
    }

    @Override
    public List<Voucher> getVoucherByCartPrice(BigDecimal cartPrice) {
        return voucherRepository.getVoucherByCartPrice(cartPrice);
    }

    @Override
    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }


}
