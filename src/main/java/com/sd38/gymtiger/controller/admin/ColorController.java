package com.sd38.gymtiger.controller.admin;

import com.sd38.gymtiger.model.Color;
import com.sd38.gymtiger.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/tiger/color")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping("/page")
    public String getPage(@RequestParam(defaultValue = "0") Integer page, Model model) {
        Page<Color> pageColor = colorService.getPage(page);
        model.addAttribute("pageColor", pageColor);
        model.addAttribute("page", page);
        return "admin/color/color";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id,
                         Model model) {
        Color color = colorService.detail(id);
        model.addAttribute("color", color);
        return "/admin/color/color-detail";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      RedirectAttributes redirectAttributes) {
        if (colorService.add(name)) {
            redirectAttributes.addFlashAttribute("mess", "Thêm dữ liệu thành công");
            return "redirect:/tiger/color/page";
        } else {
            redirectAttributes.addFlashAttribute("error", "Tên màu sắc đã tồn tại");
            return "redirect:/tiger/color/page";
        }
    }

    @PostMapping("/attribute")
    public String attribute(@RequestParam String colorName,
                            @RequestParam String productDetailId,
                            RedirectAttributes redirectAttributes) {
        if (colorService.add(colorName)) {
            redirectAttributes.addFlashAttribute("mess", "Thêm dữ liệu thành công");
            return "redirect:/tiger/product-detail/detail/" + productDetailId;
        } else {
            redirectAttributes.addFlashAttribute("error", "Tên màu sắc đã tồn tại");
            return "redirect:/tiger/product-detail/detail/" + productDetailId;
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id,
                         @RequestParam String name,
                         RedirectAttributes redirectAttributes) {
        if (colorService.update(id, name)) {
            redirectAttributes.addFlashAttribute("mess", "Sửa dữ liệu thành công");
            return "redirect:/tiger/color/page";
        } else {
            redirectAttributes.addFlashAttribute("error", "Tên màu sắc đã tồn tại");
            return "redirect:/tiger/color/detail" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id,
                         RedirectAttributes redirectAttributes) {
        colorService.delete(id);
        redirectAttributes.addFlashAttribute("mess", "Xóa dữ liệu thành công");
        return "redirect:/tiger/color/page";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false) String colorNameSearch,
                         @RequestParam(defaultValue = "0") int page) {
        Page<Color> pageColor = colorService.search(colorNameSearch, page);
        if ("".equals(colorNameSearch) || colorNameSearch.isEmpty()) {
            return "redirect:/tiger/color/page";
        }
        model.addAttribute("colorNameSearch", colorNameSearch);
        model.addAttribute("pageColor", pageColor);
        return "admin/color/color";
    }

    @GetMapping("/view-restore")
    public String viewRestore(@RequestParam(defaultValue = "0") int page,
                              Model model) {
        Page<Color> pageColor = colorService.getAllSizeDelete(page);
        model.addAttribute("pageColor", pageColor);
        return "admin/color/color-restore";
    }

    @PostMapping("/restore/{id}")
    public String restore(@PathVariable Integer id,
                          RedirectAttributes redirectAttributes) {
        colorService.restore(id);
        redirectAttributes.addFlashAttribute("mess", "Khôi phục dữ liệu thành công");
        return "redirect:/tiger/color/view-restore";
    }

    @GetMapping("/search-restore")
    public String searchDelete(Model model, @RequestParam(required = false) String colorNameSearch,
                               @RequestParam(defaultValue = "0") int page) {
        Page<Color> pageColor = colorService.searchDeleted(colorNameSearch, page);
        if ("".equals(colorNameSearch) || colorNameSearch.isEmpty()) {
            return "redirect:/tiger/color/view-restore";
        }
        model.addAttribute("colorNameSearch", colorNameSearch);
        model.addAttribute("pageColor", pageColor);
        return "admin/color/color-restore";
    }

    @PostMapping("/excel")
    public String importExcel(RedirectAttributes redirectAttributes, @RequestParam MultipartFile excelFile) throws IOException {
        String result = colorService.importExcel(excelFile);
        if (result.contains("Oke")) {
            redirectAttributes.addFlashAttribute("mess", "Thêm dữ liệu excel thành công");
            return "redirect:/tiger/color/page";
        } else if (result.contains("Sai dữ liệu")) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại kiểu dữ liệu trong file");
            return "redirect:/tiger/color/page";
        } else if (result.contains("Tồn tại")) {
            redirectAttributes.addFlashAttribute("error", "Dữ liệu trong file đã tồn tại");
            return "redirect:/tiger/color/page";
        } else if (result.contains("Trống")) {
            redirectAttributes.addFlashAttribute("error", "Trong file excel không có dữ liệu");
            return "redirect:/tiger/brand/page";
        } else if (result.contains("Trùng")) {
            redirectAttributes.addFlashAttribute("error", "1 số dữ liệu trong file bị trùng lặp");
            return "redirect:/tiger/color/page";
        } else {
            redirectAttributes.addFlashAttribute("error", "Đây không phải là file excel");
            return "redirect:/tiger/color/page";
        }
    }
}
