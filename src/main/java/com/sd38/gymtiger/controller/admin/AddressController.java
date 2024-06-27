package com.sd38.gymtiger.controller.admin;

import com.sd38.gymtiger.model.Account;
import com.sd38.gymtiger.model.Address;
import com.sd38.gymtiger.service.AccountService;
import com.sd38.gymtiger.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tiger/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Address address = addressService.getOne(id);
        List<Account> lstAcc = accountService.getAll();
        model.addAttribute("lstAcc", lstAcc);
        model.addAttribute("address", address);
        return "/admin/address/address-detail";
    }

    @GetMapping("/page")
    public String getAllPT(@RequestParam(defaultValue = "0", value = "page") Integer page, Model model) {
        Page<Address> page1 = addressService.getAllPT(page);
        model.addAttribute("page", page1);
        return "/admin/address/address";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        addressService.delete(id);
        redirectAttributes.addFlashAttribute("mess", "Xoá thành công!!");
        return "redirect:/tiger/address/page";
    }


    @PostMapping("/add")
    public String add(@ModelAttribute("Address") Address address, RedirectAttributes redirectAttributes) {
        addressService.add(address);
        redirectAttributes.addFlashAttribute("mess", "Thêm thành công!!");
        return "redirect:/tiger/address/page";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("Address") Address address, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        addressService.update(address, id);
        redirectAttributes.addFlashAttribute("mess", "Sửa thành công!!");
        return "redirect:/tiger/address/page";

    }
}
