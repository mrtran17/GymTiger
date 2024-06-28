
package com.sd38.gymtiger.controller.admin;

import com.sd38.gymtiger.model.*;
import com.sd38.gymtiger.repository.ColorRepository;
import com.sd38.gymtiger.repository.MaterialRepository;
import com.sd38.gymtiger.repository.SizeRepository;
import com.sd38.gymtiger.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/tiger/pos")
public class OfflineCartController {

    @Autowired
    private OfflineCartService offlineCartService;

    @Autowired
    private BillService billService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CustomerService customerService;

    private Bill bill = new Bill();
    private Account currentUser = new Account();
    Date ngayhomnay = Date.valueOf(LocalDate.now());

    private String prodName = "";
    private String colorCode = "%%";
    private String matrCode = "%%";
    private String sizeName = "%%";

    private boolean tontaitronggio = false;
    private String thongbaotontai = "";

    private String kwds = "";

    @GetMapping()
    public String cart(Model model, HttpSession session, Principal principal,
                       @ModelAttribute("hoadoncho")Bill hoadoncho) {
        String currentUserName = principal.getName();
        currentUser = accountService.findFirstByEmail(currentUserName);

        List<Bill> listHdCho = billService.findAllByStatus(10);
        List<Voucher> danhsachvoucher = voucherService.ActiveVoucher();
        if (bill.getId()!=null){
            List<BillDetail> danhsachHDCT = billService.getLstDetailByBillId(bill.getId());
            model.addAttribute("giohientai", danhsachHDCT);
        }

        if(bill.getCustomer()==null){
            model.addAttribute("khUsername", "Chọn khách hàng");
        }
        else {
            model.addAttribute("khUsername", bill.getCustomer().getName());
        }
        if(bill.getVoucher()==null){
            model.addAttribute("voucherInfo", "Chọn voucher");
        }
        else {
            model.addAttribute("voucherInfo", bill.getVoucher().getName());
            model.addAttribute("giatrivoucher", Double.parseDouble(String.valueOf(bill.getVoucher().getValue()))+"%");
        }

        hoadoncho = bill;
        model.addAttribute("giohientai",billService.getLstDetailByBillId(bill.getId()));
        model.addAttribute("hoadoncho", hoadoncho);
        model.addAttribute("listVoucher", danhsachvoucher);
        model.addAttribute("danhsachhoadon", listHdCho);
        model.addAttribute("soLuongHD", listHdCho.size());
        return "/admin/cart/offline-cart";
    }

    @RequestMapping("/addHD")
    public String taoHoaDon() {

            Bill hoadonmoi = new Bill();
            Integer soluong = billService.getAllBill().size()+1;
            hoadonmoi.setType(0);
            hoadonmoi.setCode("MHD" + soluong);
            hoadonmoi.setOrderDate(ngayhomnay);
            hoadonmoi.setPrice(BigDecimal.valueOf(0));
            hoadonmoi.setShippingFee(BigDecimal.valueOf(0));
            hoadonmoi.setTotalPrice(BigDecimal.valueOf(0));
            hoadonmoi.setCreateDate(ngayhomnay);
            hoadonmoi.setStatus(10);
            hoadonmoi.setVoucher(null);
            hoadonmoi.setEmployee(currentUser);
            hoadonmoi.setCustomer(null);
            billService.addBillPos(hoadonmoi);

            List<Bill> listHD2 = billService.findAllByStatus(10);
            bill = listHD2.get(listHD2.size() - 1);
        return "redirect:/tiger/pos";
    }

    @RequestMapping("/chonHD/{id}")
    public String chonBill(@PathVariable("id")Integer id){
        bill = billService.getOneBill(id);
        bill.setEmployee(currentUser);
        billService.updateBill(bill, id);
        return "redirect:/tiger/pos";
    }

    @RequestMapping("/xoa/{id}")
    public String xoaHoaDon(@PathVariable("id")Integer id){
        List<BillDetail> thuocvehdbihuy = billService.getLstDetailByBillId(id);
        for(BillDetail k: thuocvehdbihuy){
            huydonhang(k.getId());
        }
        Bill hd_bi_huy = billService.getOneBill(id);
        hd_bi_huy.setCancellationDate(Date.valueOf(LocalDate.now()));
        hd_bi_huy.setStatus(0);
        hd_bi_huy.setEmployee(currentUser);
        billService.updateBill(hd_bi_huy, id);
        bill = new Bill();
        return "redirect:/tiger/pos";
    }

    @RequestMapping("/thanhtoan")
    public String thanhtoan(@ModelAttribute("hoadoncho")Bill hoadon, Model model){
        if (bill.getId()==null){
            return "redirect:/tiger/pos";
        }
        Double tienSauVoucher = Double.parseDouble(String.valueOf(hoadon.getTotalPrice()));
        Double tienKhachDua = Double.parseDouble(String.valueOf(hoadon.getDiscountAmount()));

        Double tinhtien = tienKhachDua - tienSauVoucher;
        String chuThich = "Tiền thừa của khách là: "+ tinhtien;
        hoadon.setNote(chuThich);
        hoadon.setPaymentDate(ngayhomnay);
        hoadon.setConfirmationDate(ngayhomnay);
        hoadon.setCompletionDate(ngayhomnay);
        hoadon.setOrderDate(bill.getOrderDate());
        hoadon.setShippingFee(BigDecimal.valueOf(0.0));
        hoadon.setCreateDate(bill.getCreateDate());
        hoadon.setUpdateDate(ngayhomnay);
        hoadon.setStatus(1);
        hoadon.setEmployee(currentUser);
        hoadon.setCustomer(bill.getCustomer());
        hoadon.setVoucher(bill.getVoucher());
        hoadon.setType(0);

        billService.addBillPos(hoadon);
        bill = new Bill();
        return "redirect:/tiger/pos";
    }

    //chi tiết sản phẩm trong giỏ
    @RequestMapping("/litSP")
    public String ProDlist(Model model){
        if(bill.getId()==null){
            return "redirect:/tiger/pos";
        }
        List<ProductDetail> detailList = productDetailService.locSpTaiQuay(
                "%"+prodName+"%",
                colorCode,
                matrCode,
                sizeName);

        model.addAttribute("danhsachSP", detailList);

        model.addAttribute("chatlieuL", materialRepository.findAll());
        model.addAttribute("mausacL", colorRepository.findAll());
        model.addAttribute("sizeL", sizeRepository.findAll());

        return "/admin/cart/side_form/product_detail_list";
    }

    @RequestMapping("/locSP")
    public String locSP(
            @RequestParam(defaultValue = "") String tukhoa,
            @RequestParam(defaultValue = "%%") String chatlieu,
            @RequestParam(defaultValue = "%%") String mausac,
            @RequestParam(defaultValue = "%%") String kichco
    ){
        prodName = tukhoa;
        colorCode = mausac;
        matrCode = chatlieu;
        sizeName = kichco;
        return "redirect:/tiger/pos/litSP";
    }

    @RequestMapping("/chon_de_them_vao_hd/{id}")
    public String ctsp_duoc_chon(@PathVariable("id")Integer id,Model model){
        if (bill.getId()==null){
            return "redirect:/tiger/pos";
        }
        ProductDetail detail = productDetailService.getOne(id);
        BillDetail billDetail = new BillDetail();
        billDetail.setProductDetail(detail);
        model.addAttribute("thongtinSP", billDetail);
        model.addAttribute("error", thongbaotontai);
        thongbaotontai = "";
        tontaitronggio = false;
        return "/admin/cart/side_form/chosen_product";
    }

    @RequestMapping("/them_vao_hd")
    public String themvaohoadon(@ModelAttribute("thongtinSP") BillDetail detail){
        List<BillDetail> kiemtratontai = billService.getLstDetailByBillId(bill.getId());
        ProductDetail productDetail = productDetailService.getOne(detail.getProductDetail().getId());

        for(BillDetail k: kiemtratontai){
            System.out.println(k.getStatus());
            if (k.getProductDetail().getId()==productDetail.getId() && k.getStatus()!=0){
                tontaitronggio = true;
            }
        }

        if (tontaitronggio){
            thongbaotontai = "Sản phẩm đã có trong giỏ";
            return "redirect:/tiger/pos/chon_de_them_vao_hd/"+productDetail.getId();
        }

        productDetail.setQuantity( productDetail.getQuantity() - detail.getQuantity() );

        System.out.println(productDetail.getProduct().getName()+" - "+detail.getQuantity()+" - "+productDetail.getId());
        //productDetailService.simplizedUpdate(productDetail.getId(), productDetail);

        detail.setBill(billService.getOneBill(bill.getId()));
        detail.setPrice(productDetail.getPrice());
        detail.setProductDetail(productDetail);
//        System.out.println(detail.getBill()+" - "+detail.getProductDetail().getProduct().getName()+" - "
//            +detail.getPrice()+" - "+detail.getQuantity()
//        );
        billService.addBillDetailPos(detail);
        tinhTongTien();
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    }

    @RequestMapping("/doi_so_luong_tai_quay/{id}")
    public String dieuchinhsoluongtronggio(
            @PathVariable("id")Integer id,
            @RequestParam("soluongmoi") Integer soluongmoi
    ){
        BillDetail hdct_dc_doi = billService.getOneBillDetail(id);
        ProductDetail spthaydoi = hdct_dc_doi.getProductDetail();

        Integer traVeSP = spthaydoi.getQuantity() + hdct_dc_doi.getQuantity() - soluongmoi;
        hdct_dc_doi.setQuantity(soluongmoi);
        spthaydoi.setQuantity(traVeSP);

        billService.updateBillDetail(hdct_dc_doi, id);
        productDetailService.simplizedUpdate(spthaydoi.getId(), spthaydoi);

        tinhTongTien();
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    };

    @RequestMapping("/xoa-hdtq/{id}")
    public String xoaBillDetail(@PathVariable("id")Integer id){
        huydonhang(id);
        tinhTongTien();
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    }
    //Khách và voucher
    @RequestMapping("/listKH_tai_quay")
    public String danhSachKhachHang(Model model){
        if (bill.getId()==null){
            return "redirect:/tiger/pos";
        }
        List<Customer> listCustomer = customerService.findByKwds("%"+kwds+"%");
        model.addAttribute("listKH", listCustomer);
        return "/admin/cart/side_form/customers_list";
    }

    @RequestMapping("/tim_KH")
    public String locKH(@RequestParam(defaultValue = "")String tukhoa){
        kwds = tukhoa;
        return "redirect:/tiger/pos/listKH_tai_quay";
    }
    @RequestMapping("/chonKH/{id}")
    public String chonKH(@PathVariable("id")Integer id){
        Customer kh = customerService.findById(id);
        bill = billService.getOneBill(bill.getId());
        bill.setCustomer(kh);
        billService.addBillPos(bill);
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    };
    @RequestMapping("/listKH_tai_quay/huy_KH")
    public String huyKhTrongHd(){
        bill = billService.getOneBill(bill.getId());
        bill.setCustomer(null);
        billService.addBillPos(bill);
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    }
    @RequestMapping("/chonVoucher/{id}")
    public String chonVcr(@PathVariable("id")Integer id){
        Voucher vcr = voucherService.getVoucherById(id);
        Bill them_vcr = billService.getOneBill(bill.getId());
        them_vcr.setVoucher(vcr);
        billService.addBillPos(them_vcr);
        tinhTongTien();
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    }
    @RequestMapping("/listKH_tai_quay/huy_vcr")
    public String huyVcr(){
        Bill them_vcr = billService.getOneBill(bill.getId());
        them_vcr.setVoucher(null);
        billService.addBillPos(them_vcr);
        tinhTongTien();
        return "redirect:/tiger/pos/chonHD/"+bill.getId();
    }
    //public hỗ trợ
    private void tinhTongTien() {
        Double tongtien = 0.0;
        Double tinhKm = 0.0;
        List<BillDetail> listTrongGio = billService.getLstDetailByBillId(bill.getId());
        for (BillDetail k :listTrongGio){
            Integer soluong = k.getQuantity();
            Double giatien = Double.parseDouble(String.valueOf(k.getPrice()));
            Double nhanlen = soluong*giatien;
            tongtien = tongtien + nhanlen;
        }

        Bill hoadon = billService.getOneBill(bill.getId());
        if (hoadon.getVoucher()==null){
            tinhKm = tongtien;
        }
        else {
            tinhKm = tongtien - (tongtien/100*Double.parseDouble(String.valueOf(hoadon.getVoucher().getValue())));
        }

        hoadon.setPrice(BigDecimal.valueOf(tongtien));
        hoadon.setTotalPrice(BigDecimal.valueOf(tinhKm));
        billService.updateBill(hoadon, bill.getId());
    }

    private void huydonhang(Integer id) {
        BillDetail cthd_bi_xoa = billService.getOneBillDetail(id);
        ProductDetail sp_duoc_tra = cthd_bi_xoa.getProductDetail();

        sp_duoc_tra.setQuantity(
                sp_duoc_tra.getQuantity() + cthd_bi_xoa.getQuantity()
        );
        productDetailService.simplizedUpdate(sp_duoc_tra.getId(), sp_duoc_tra);
        billService.deleteBillDetail(cthd_bi_xoa);
    }
}
