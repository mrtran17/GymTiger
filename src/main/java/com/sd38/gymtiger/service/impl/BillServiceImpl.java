package com.sd38.gymtiger.service.impl;

import com.sd38.gymtiger.config.MailConfig;
import com.sd38.gymtiger.dto.admin.BillDto;
import com.sd38.gymtiger.model.Account;
import com.sd38.gymtiger.model.Address;
import com.sd38.gymtiger.model.BillDetail;
import com.sd38.gymtiger.model.Cart;
import com.sd38.gymtiger.model.CartDetail;
import com.sd38.gymtiger.model.Customer;
import com.sd38.gymtiger.model.PaymentMethod;
import com.sd38.gymtiger.model.ProductDetail;
import com.sd38.gymtiger.model.Role;
import com.sd38.gymtiger.model.SessionCart;
import com.sd38.gymtiger.model.SessionCartItem;
import com.sd38.gymtiger.model.Voucher;
import com.sd38.gymtiger.repository.AccountRepository;
import com.sd38.gymtiger.repository.AddressRepository;
import com.sd38.gymtiger.repository.BillDetailRepository;
import com.sd38.gymtiger.model.Bill;
import com.sd38.gymtiger.repository.BillRepository;
import com.sd38.gymtiger.repository.CustomerRepository;
import com.sd38.gymtiger.repository.PaymentMethodRepository;
import com.sd38.gymtiger.repository.ProductDetailRepository;
import com.sd38.gymtiger.repository.RoleRepository;
import com.sd38.gymtiger.repository.VoucherRepository;
import com.sd38.gymtiger.service.BillService;
import com.sd38.gymtiger.service.CartService;
import com.sd38.gymtiger.utils.GHNUtil;
import com.sd38.gymtiger.utils.MailUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GHNUtil ghnUtil;

    MailConfig mailConfig = new MailConfig();

    @Override
    public List<Bill> getAllBill() {
        return billRepository.findAll();
    }

    @Override
    public Page<Bill> getAllBillPT(Integer page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "OrderDate"));
        return billRepository.findAll(pageable);
    }

    @Override
    public Bill addBillPos(Bill bill) {
//        bill.setStatus(1);
//        bill.setCreateDate(new Date());
//        bill.setUpdateDate(new Date());
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Bill bill, Integer id) {
        Optional<Bill> optional = billRepository.findById(id);
        if (optional.isPresent()) {
            Bill oldBill = optional.get();
            bill.setId(oldBill.getId());
            bill.setStatus(oldBill.getStatus());
            bill.setCreateDate(oldBill.getCreateDate());
            bill.setUpdateDate(new Date());
            return billRepository.save(bill);
        }
        return null;
    }

    @Override
    public List<BillDetail> getAllBillDetail() {
        return billDetailRepository.findAllByStatus(1);
    }

    @Override
    public Page<BillDetail> getAllBillDetailPT(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return billDetailRepository.findAllByStatus(pageable, 1);
    }

    @Override
    public BillDetail addBillDetailPos(BillDetail billDetail) {
        billDetail.setStatus(1);
        return billDetailRepository.save(billDetail);
    }

    @Override
    public BillDetail updateBillDetail(BillDetail billDetail, Integer id) {
        Optional<BillDetail> optional = billDetailRepository.findById(id);
        if (optional.isPresent()) {
            BillDetail oldBillDetail = optional.get();
            billDetail.setId(oldBillDetail.getId());
            return billDetailRepository.save(billDetail);
        }
        return null;
    }

    @Override
    public Bill getOneBill(Integer id) {
        return billRepository.findById(id).orElse(null);
    }

    @Override
    public BillDetail getOneBillDetail(Integer id) {
        return billDetailRepository.findById(id).orElse(null);
    }

    @Override
    public List<BillDetail> getLstDetailByBillId(Integer id) {
        return billDetailRepository.findAllByBill_Id(id);
    }

    @Override
    public Bill delete(Integer id) {
        return null;
    }

    @Override // Ghi đè phương thức từ lớp cha hoặc interface
    @Transactional // Đảm bảo tất cả các thao tác CSDL trong phương thức này được coi là một đơn vị công việc (hoặc tất cả thành công hoặc tất cả thất bại)
    public Bill placeOrder(Cart cart, String name, String specificAddress, String ward, String district, String city, String phoneNumber, String payment, Integer voucher) {

        // Tạo một đối tượng Bill mới để đại diện cho đơn hàng của khách hàng
        Bill bill = new Bill();

        // Tạo và đặt một mã bill duy nhất
        bill.setCode(generateBillCode());

        // Đặt loại bill là 0 (có thể đại diện cho loại đơn hàng tiêu chuẩn)
        bill.setType(0);

        // Đặt thông tin khách hàng
        bill.setCustomerName(name);

        // Định dạng và đặt địa chỉ đầy đủ của khách hàng
        String address = specificAddress + ", " + ward + ", " + district + ", " + city;
        bill.setAddress(address);
        bill.setPhoneNumber(phoneNumber);
        bill.setOrderDate(new Date());

        // Đặt giá ban đầu (từ giỏ hàng) và tính phí vận chuyển
        bill.setPrice(cart.getTotalPrice());
        BigDecimal shippingFee = ghnUtil.calculateShippingFee(city, district, ward, cart.getTotalItems());
        bill.setShippingFee(shippingFee);

        // Xử lý việc áp dụng mã giảm giá
        if (voucher == null) {
            // Không có mã giảm giá: đặt mức giảm giá bằng 0, tổng giá bao gồm phí vận chuyển
            bill.setDiscountAmount(BigDecimal.ZERO);
            bill.setTotalPrice(cart.getTotalPrice().add(shippingFee));
        } else {
            // Tìm thấy mã giảm giá: lấy thông tin chi tiết của mã giảm giá
            Voucher uuDai = voucherRepository.findById(voucher).orElse(null);
            bill.setDiscountAmount(uuDai.getValue());
            bill.setTotalPrice(cart.getTotalPrice().add(shippingFee).subtract(uuDai.getValue()));
            bill.setVoucher(uuDai);

            // Cập nhật số lượng và trạng thái của mã giảm giá
            uuDai.setQuantity(uuDai.getQuantity() - 1);
            if (uuDai.getQuantity() == 0) {
                uuDai.setStatus(10); // Có thể cho biết mã giảm giá không còn hiệu lực
            }
            voucherRepository.save(uuDai);
        }

        // Đặt thời gian tạo và cập nhật bill
        bill.setCreateDate(new Date());
        bill.setUpdateDate(new Date());

        // Đặt trạng thái của bill dựa trên phương thức thanh toán
        if (payment.equals("Thanh toán qua VNPAY") || payment.equals("Thanh toán qua Momo") || payment.equals("Thanh toán qua ZaloPay")) {
            // Thanh toán trực tuyến: đánh dấu là đã thanh toán và xác nhận
            bill.setPaymentDate(new Date());
            bill.setConfirmationDate(new Date());
            bill.setStatus(3); // Có thể đại diện cho trạng thái "Đã thanh toán"
        } else {
            bill.setStatus(10); // Có thể đại diện cho trạng thái "Chờ xử lý" hoặc "Chưa thanh toán"
        }

        // Liên kết bill với khách hàng (lấy từ giỏ hàng)
        bill.setCustomer(cart.getCustomer());

        // Kiểm tra xem khách hàng đã có địa chỉ lưu trong hệ thống chưa
        List<Address> listAccountAddress = addressRepository.findAllAccountAddress(cart.getCustomer().getId());
        if (listAccountAddress.isEmpty()) {
            // Nếu chưa có, tạo mới địa chỉ cho khách hàng
            Account account = accountRepository.findById(cart.getCustomer().getId()).orElse(null);
            Address newAddress = new Address();
            newAddress.setCustomerName(name);
            newAddress.setPhoneNumber(phoneNumber);
            newAddress.setSpecificAddress(specificAddress);
            newAddress.setWard(ward);
            newAddress.setDistrict(district);
            newAddress.setCity(city);
            newAddress.setCreateDate(new Date());
            newAddress.setUpdateDate(new Date());
            newAddress.setStatus(1); // Có thể đại diện cho trạng thái "Hoạt động" của địa chỉ
            newAddress.setAccount(account);
            addressRepository.save(newAddress);
        }

        // Tạo danh sách chi tiết đơn hàng từ các sản phẩm trong giỏ hàng
        List<BillDetail> billDetailList = new ArrayList<>();
        for (CartDetail item : cart.getCartDetails()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setProductDetail(item.getProductDetail());
            billDetail.setPrice(item.getPrice());
            billDetail.setQuantity(item.getQuantity());
            billDetail.setStatus(1); // Có thể đại diện cho trạng thái "Hoạt động" của chi tiết đơn hàng
            billDetailRepository.save(billDetail);
            billDetailList.add(billDetail);

            // Cập nhật số lượng sản phẩm sau khi đặt hàng
            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetail().getId()).orElse(null);
            productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
            productDetailRepository.save(productDetail);
        }
        cartService.deleteCartById(cart.getId());

        // Tạo phương thức thanh toán và liên kết với bill
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setBill(bill);
        paymentMethod.setName(payment);

        // Đặt trạng thái và số tiền của phương thức thanh toán dựa trên phương thức thanh toán
        if (payment.equals("Thanh toán qua VNPAY") || payment.equals("Thanh toán qua Momo") || payment.equals("Thanh toán qua ZaloPay")) {
            // Thanh toán trực tuyến: đánh dấu là đã thanh toán và xác nhận
            paymentMethod.setMoney(bill.getTotalPrice());
            paymentMethod.setStatus(1);
        } else {
            paymentMethod.setMoney(bill.getTotalPrice());
            paymentMethod.setStatus(10); // Có thể đại diện cho trạng thái "Chờ xử lý" hoặc "Chưa thanh toán"
        }

        paymentMethod.setDescription(payment);
        paymentMethodRepository.save(paymentMethod);

        // Lưu bill vào cơ sở dữ liệu và trả về bill đã được lưu
        return billRepository.save(bill);
    }

    @Override
    @Transactional
    public Bill placeOrderSession(SessionCart cart, String email, String name, String specificAddress, String ward, String district, String city, String phoneNumber, String payment, Integer voucher) {
        Bill bill = new Bill();
        bill.setCode(generateBillCode());
        bill.setType(0);
        bill.setCustomerName(name);
        String address = specificAddress + ", " + ward + ", " + district + ", " + city;
        bill.setAddress(address);
        bill.setPhoneNumber(phoneNumber);
        bill.setOrderDate(new Date());
        bill.setPrice(cart.getTotalPrice());
        BigDecimal shippingFee = ghnUtil.calculateShippingFee(city, district, ward, cart.getTotalItems());
        bill.setShippingFee(shippingFee);
        if (voucher == null) {
            bill.setDiscountAmount(BigDecimal.ZERO);
            bill.setTotalPrice(cart.getTotalPrice().add(shippingFee));
        } else {
            Voucher uuDai = voucherRepository.findById(voucher).orElse(null);
            bill.setDiscountAmount(uuDai.getValue());
            bill.setTotalPrice(cart.getTotalPrice().add(shippingFee).subtract(uuDai.getValue()));
            bill.setVoucher(uuDai);
            uuDai.setQuantity(uuDai.getQuantity() - 1);
            if (uuDai.getQuantity() == 0) {
                uuDai.setStatus(10);
            }
            voucherRepository.save(uuDai);
        }
        bill.setCreateDate(new Date());
        bill.setUpdateDate(new Date());
        if (payment.equals("Thanh toán qua VNPAY") || payment.equals("Thanh toán qua Momo") || payment.equals("Thanh toán qua ZaloPay")) {
            bill.setPaymentDate(new Date());
            bill.setConfirmationDate(new Date());
            bill.setStatus(3);
        } else {
            bill.setStatus(10);
        }
        Customer customer = customerRepository.findByEmail(email);
        String body;
        if (customer == null) {
            customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setCreateDate(new Date());
            customer.setUpdateDate(new Date());
            customer.setStatus(1);
            Role role = roleRepository.findFirstByName("User");
            customer.setRole(role);
            customer.setPassword(passwordEncoder.encode("123456"));
            customerRepository.save(customer);
            body = mailUtil.noAccountMailTemplate(email, mailConfig.company, mailConfig.contact);
        } else {
            body = mailUtil.accountMailTemplate(email, mailConfig.company, mailConfig.contact);
        }
        mailUtil.sendEmail(email, mailConfig.placeOrderMail, body);
        bill.setCustomer(customer);
        List<Address> listAccountAddress = addressRepository.findAllAccountAddress(bill.getCustomer().getId());
        if (listAccountAddress.isEmpty()) {
            Account account = accountRepository.findById(bill.getCustomer().getId()).orElse(null);
            Address newAddress = new Address();
            newAddress.setCustomerName(name);
            newAddress.setPhoneNumber(phoneNumber);
            newAddress.setSpecificAddress(specificAddress);
            newAddress.setWard(ward);
            newAddress.setDistrict(district);
            newAddress.setCity(city);
            newAddress.setCreateDate(new Date());
            newAddress.setUpdateDate(new Date());
            newAddress.setStatus(1);
            newAddress.setAccount(account);
            addressRepository.save(newAddress);
        }

        List<BillDetail> billDetailList = new ArrayList<>();
        for (SessionCartItem item : cart.getCartDetails()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetail().getId()).orElse(null);
            billDetail.setProductDetail(productDetail);
            billDetail.setPrice(item.getPrice());
            billDetail.setQuantity(item.getQuantity());
            billDetail.setStatus(1);
            billDetailRepository.save(billDetail);
            billDetailList.add(billDetail);
            productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
            productDetailRepository.save(productDetail);
        }
        cart.clear();
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setBill(bill);
        paymentMethod.setName(payment);
        if (payment.equals("Thanh toán qua VNPAY") || payment.equals("Thanh toán qua Momo") || payment.equals("Thanh toán qua ZaloPay")) {
            paymentMethod.setMoney(bill.getTotalPrice());
            paymentMethod.setStatus(1);
        } else {
            paymentMethod.setMoney(bill.getTotalPrice());
            paymentMethod.setStatus(10);
        }
        paymentMethod.setDescription(payment);
        paymentMethodRepository.save(paymentMethod);
        return billRepository.save(bill);
    }

    @Override
    public List<Bill> getStatusOrders(Integer status ,Integer customerId) {
        return billRepository.getOrders(status, customerId);
    }

    @Override
    public List<Bill> getAllOrders(Integer customerId) {
        return billRepository.getAllOrders(customerId);
    }

    @Override
    @Transactional
    public boolean userCancelOrder(Integer billId) {
        Bill bill = billRepository.findById(billId).orElse(null);
        if (bill.getStatus() != 10) {
            return false;
        } else {
            bill.setStatus(0);
            bill.setCancellationDate(new Date());
            if (bill.getVoucher() != null) {
                Voucher voucher = bill.getVoucher();
                if (voucher.getStatus() == 10 || voucher.getStatus() == 1) {
                    voucher.setQuantity(voucher.getQuantity() + 1);
                    voucher.setStatus(1);
                }
                voucherRepository.save(voucher);
            }
            List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAllByBillIdOrderById(billId);
            for (PaymentMethod paymentMethod : paymentMethodList) {
                paymentMethod.setStatus(0);
            }
            List<BillDetail> billDetailList = billDetailRepository.findAllByBill_Id(billId);
            for (BillDetail billDetail : billDetailList) {
                billDetail.setStatus(0);
                billDetailRepository.save(billDetail);
                ProductDetail productDetail = productDetailRepository.findById(billDetail.getProductDetail().getId()).orElse(null);
                productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
                productDetailRepository.save(productDetail);
            }
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    public boolean confirmOrder(Integer id, Account account) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill.getStatus() != 10) {
            return false;
        } else {
            bill.setStatus(3);
            bill.setConfirmationDate(new Date());
//            bill.setShippingFee(shippingFee);
//            bill.setTotalPrice(bill.getPrice().subtract(bill.getDiscountAmount()).add(shippingFee));
            bill.setEmployee(account);
//            PaymentMethod paymentMethod = paymentMethodRepository.findByStatusAndBillId(10, id);
//            if (paymentMethod != null) {
//                paymentMethod.setMoney(bill.getTotalPrice());
//                paymentMethodRepository.save(paymentMethod);
//            } else if (shippingFee.compareTo(BigDecimal.ZERO) != 0) {
//                PaymentMethod newPaymentMethod = new PaymentMethod();
//                newPaymentMethod.setName("Thanh toán khi nhận hàng");
//                newPaymentMethod.setMoney(shippingFee);
//                newPaymentMethod.setDescription("Thanh toán khi nhận hàng");
//                newPaymentMethod.setStatus(10);
//                newPaymentMethod.setBill(bill);
//                paymentMethodRepository.save(newPaymentMethod);
//            }
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    public boolean shippingOrder(Integer id, Account account) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill.getStatus() != 3) {
            return false;
        } else {
            bill.setStatus(2);
            bill.setShippingDate(new Date());
            bill.setEmployee(account);
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    public boolean completeOrder(Integer id, Account account) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill.getStatus() != 2) {
            return false;
        } else {
            bill.setStatus(1);
            bill.setCompletionDate(new Date());
            if (bill.getPaymentDate() == null) {
                bill.setPaymentDate(new Date());
            }
            bill.setEmployee(account);
            List<PaymentMethod> listPaymentMethod = paymentMethodRepository.findAllByBillIdOrderById(id);
            for (PaymentMethod paymentMethod : listPaymentMethod) {
                if (paymentMethod.getStatus() == 10) {
                    paymentMethod.setStatus(1);
                    paymentMethodRepository.save(paymentMethod);
                }
            }
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    public boolean changeAddressOrder(Integer billId, String name, String phoneNumber, String specificAddress, String ward, String district, String city, Account account) {
        Bill bill = billRepository.findById(billId).orElse(null);
        if (bill.getStatus() != 10){
            return false;
        } else {
            String address = specificAddress + ", " + ward + ", " + district + ", " + city;
            bill.setCustomerName(name);
            bill.setAddress(address);
            List<BillDetail> billDetails = billDetailRepository.findAllByBill_Id(billId);
            int totalQuantity = billDetails.stream()
                    .mapToInt(BillDetail::getQuantity)
                    .sum();
            BigDecimal newShippingFee = ghnUtil.calculateShippingFee(city, district, ward, totalQuantity);
            bill.setShippingFee(newShippingFee);
            bill.setTotalPrice(bill.getPrice().add(newShippingFee).subtract(bill.getDiscountAmount()));
            bill.setUpdateDate(new Date());
            bill.setEmployee(account);
            List<PaymentMethod> listPaymentMethod = paymentMethodRepository.findAllByBillIdOrderById(billId);
            for (PaymentMethod paymentMethod : listPaymentMethod) {
                if (paymentMethod.getStatus() == 10) {
                    paymentMethod.setMoney(bill.getTotalPrice());
                    paymentMethodRepository.save(paymentMethod);
                }
            }
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean updateBillItem(Integer itemId, Integer quantity, Account account) {
        BillDetail billDetail = billDetailRepository.findById(itemId).orElse(null);
        Integer quantityGap = quantity - billDetail.getQuantity();
        ProductDetail productDetail = productDetailRepository.findById(billDetail.getProductDetail().getId()).orElse(null);
        if (quantity <= productDetail.getQuantity() + billDetail.getQuantity()){
            billDetail.setQuantity(quantity);
            billDetailRepository.save(billDetail);
            productDetail.setQuantity(productDetail.getQuantity() - quantityGap);
            productDetailRepository.save(productDetail);
            Bill bill = billRepository.findById(billDetail.getBill().getId()).orElse(null);
            List<BillDetail> billDetailList = billDetailRepository.findAllByBill_Id(bill.getId());
            BigDecimal newPrice = BigDecimal.ZERO;
            for (BillDetail item : billDetailList){
                BigDecimal price = item.getPrice();
                BigDecimal qtt = BigDecimal.valueOf(item.getQuantity());
                BigDecimal subTotal = price.multiply(qtt);
                newPrice = newPrice.add(subTotal);
            }
            bill.setPrice(newPrice);
            String address = bill.getAddress();
            String[] parts = address.split(", ");
            String ward = parts[1];
            String district = parts[2];
            String city = parts[3];
            int totalQuantity = billDetailList.stream()
                    .mapToInt(BillDetail::getQuantity)
                    .sum();
            BigDecimal newShippingFee = ghnUtil.calculateShippingFee(city, district, ward, totalQuantity);
            bill.setShippingFee(newShippingFee);
            Voucher voucher = bill.getVoucher();
            if (voucher != null){
                if (newPrice.compareTo(voucher.getMinimumPrice()) < 0){
                    bill.setDiscountAmount(BigDecimal.ZERO);
                } else {
                    bill.setDiscountAmount(voucher.getValue());
                }
            }
            bill.setTotalPrice(newPrice.add(newShippingFee).subtract(bill.getDiscountAmount()));
            bill.setUpdateDate(new Date());
            bill.setEmployee(account);

            List<PaymentMethod> listPaymentMethod = paymentMethodRepository.findAllByBillIdOrderById(bill.getId());
            for (PaymentMethod paymentMethod : listPaymentMethod) {
                if (paymentMethod.getStatus() == 10) {
                    paymentMethod.setMoney(bill.getTotalPrice());
                    paymentMethodRepository.save(paymentMethod);
                }
            }
            billRepository.save(bill);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteBillItem(Integer itemId, Account account) {
        BillDetail billDetail = billDetailRepository.findById(itemId).orElse(null);
        Bill bill = billRepository.findById(billDetail.getBill().getId()).orElse(null);
        List<BillDetail> billDetailList = billDetailRepository.findAllByBill_Id(bill.getId());
        ProductDetail productDetail = productDetailRepository.findById(billDetail.getProductDetail().getId()).orElse(null);
        if (billDetailList.size() > 1){
            productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
            productDetailRepository.save(productDetail);
            billDetail.setProductDetail(null);
            billDetailRepository.delete(billDetail);
            BigDecimal newPrice = BigDecimal.ZERO;
            List<BillDetail> billDetailList2 = billDetailRepository.findAllByBill_Id(bill.getId());
            for (BillDetail item : billDetailList2){
                BigDecimal price = item.getPrice();
                BigDecimal qtt = BigDecimal.valueOf(item.getQuantity());
                BigDecimal subTotal = price.multiply(qtt);
                newPrice = newPrice.add(subTotal);
            }
            bill.setPrice(newPrice);
            String address = bill.getAddress();
            String[] parts = address.split(", ");
            String ward = parts[1];
            String district = parts[2];
            String city = parts[3];
            int totalQuantity = billDetailList2.stream()
                    .mapToInt(BillDetail::getQuantity)
                    .sum();
            BigDecimal newShippingFee = ghnUtil.calculateShippingFee(city, district, ward, totalQuantity);
            bill.setShippingFee(newShippingFee);
            Voucher voucher = bill.getVoucher();
            if (voucher != null){
                if (newPrice.compareTo(voucher.getMinimumPrice()) < 0){
                    bill.setDiscountAmount(BigDecimal.ZERO);
                } else {
                    bill.setDiscountAmount(voucher.getValue());
                }
            }
            bill.setTotalPrice(newPrice.add(newShippingFee).subtract(bill.getDiscountAmount()));
            bill.setUpdateDate(new Date());
            bill.setEmployee(account);

            List<PaymentMethod> listPaymentMethod = paymentMethodRepository.findAllByBillIdOrderById(bill.getId());
            for (PaymentMethod paymentMethod : listPaymentMethod) {
                if (paymentMethod.getStatus() == 10) {
                    paymentMethod.setMoney(bill.getTotalPrice());
                    paymentMethodRepository.save(paymentMethod);
                }
            }
            billRepository.save(bill);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addBillItem(Integer billId, Integer productDetailId, Integer quantity, Account account) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
        if (quantity <= productDetail.getQuantity()){
            BillDetail billDetail = billDetailRepository.findByBill_IdAndProductDetail_Id(billId, productDetailId);
            Bill bill = billRepository.findById(billId).orElse(null);
            if (billDetail == null){
                billDetail = new BillDetail();
                billDetail.setProductDetail(productDetail);
                billDetail.setPrice(productDetail.getPriceDiscount());
                billDetail.setQuantity(quantity);
                billDetail.setBill(bill);
                billDetail.setStatus(1);
                billDetailRepository.save(billDetail);
            } else {
                billDetail.setQuantity(billDetail.getQuantity() + quantity);
                billDetailRepository.save(billDetail);
            }
            productDetail.setQuantity(productDetail.getQuantity() - quantity);
            productDetailRepository.save(productDetail);
            List<BillDetail> billDetailList = billDetailRepository.findAllByBill_Id(billId);
            BigDecimal newPrice = BigDecimal.ZERO;
            for (BillDetail item : billDetailList){
                BigDecimal price = item.getPrice();
                BigDecimal qtt = BigDecimal.valueOf(item.getQuantity());
                BigDecimal subTotal = price.multiply(qtt);
                newPrice = newPrice.add(subTotal);
            }
            bill.setPrice(newPrice);
            String address = bill.getAddress();
            String[] parts = address.split(", ");
            String ward = parts[1];
            String district = parts[2];
            String city = parts[3];
            int totalQuantity = billDetailList.stream()
                    .mapToInt(BillDetail::getQuantity)
                    .sum();
            BigDecimal newShippingFee = ghnUtil.calculateShippingFee(city, district, ward, totalQuantity);
            bill.setShippingFee(newShippingFee);
            Voucher voucher = bill.getVoucher();
            if (voucher != null){
                if (newPrice.compareTo(voucher.getMinimumPrice()) < 0){
                    bill.setDiscountAmount(BigDecimal.ZERO);
                } else {
                    bill.setDiscountAmount(voucher.getValue());
                }
            }
            bill.setTotalPrice(newPrice.add(newShippingFee).subtract(bill.getDiscountAmount()));
            bill.setUpdateDate(new Date());
            bill.setEmployee(account);

            List<PaymentMethod> listPaymentMethod = paymentMethodRepository.findAllByBillIdOrderById(bill.getId());
            for (PaymentMethod paymentMethod : listPaymentMethod) {
                if (paymentMethod.getStatus() == 10) {
                    paymentMethod.setMoney(bill.getTotalPrice());
                    paymentMethodRepository.save(paymentMethod);
                }
            }
            billRepository.save(bill);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean cancelOrder(Integer billId, Account account) {
        Bill bill = billRepository.findById(billId).orElse(null);
        if (bill.getStatus() == 0) {
            return false;
        } else {
            bill.setStatus(0);
            bill.setCancellationDate(new Date());
            if (bill.getVoucher() != null) {
                Voucher voucher = bill.getVoucher();
                if (voucher.getStatus() == 10 || voucher.getStatus() == 1) {
                    voucher.setQuantity(voucher.getQuantity() + 1);
                    voucher.setStatus(1);
                }
                voucherRepository.save(voucher);
            }
            bill.setEmployee(account);
            List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAllByBillIdOrderById(billId);
            for (PaymentMethod paymentMethod : paymentMethodList) {
                paymentMethod.setStatus(0);
                paymentMethodRepository.save(paymentMethod);
            }
            List<BillDetail> billDetailList = billDetailRepository.findAllByBill_Id(billId);
            for (BillDetail billDetail : billDetailList) {
                billDetail.setStatus(0);
                billDetailRepository.save(billDetail);
                ProductDetail productDetail = productDetailRepository.findById(billDetail.getProductDetail().getId()).orElse(null);
                productDetail.setQuantity(productDetail.getQuantity() + billDetail.getQuantity());
                productDetailRepository.save(productDetail);
            }
            billRepository.save(bill);
            return true;
        }
    }

    @Override
    public String generateBillCode() {
        Integer lastId = 0;
        Bill lastBill = billRepository.findTopByOrderByIdDesc();
        if (lastBill != null) {
            lastId = lastBill.getId();
        }
        return String.format("HD%06d", lastId + 1);
    }

    @Override
    public Page<BillDto> findAll(Pageable pageable) {
        return billRepository.listBill(pageable);
    }

    @Override
    public Page<BillDto> searchListBill(String code, Date ngayTaoStart, Date ngayTaoEnd, Integer status, Integer type, String phoneNumber, String customerName, Pageable pageable) {
        return billRepository.listSearchBill(code, ngayTaoStart, ngayTaoEnd, status, type, phoneNumber, customerName, pageable);
    }

    public void exportToExcel(HttpServletResponse response, Page<BillDto> bills, String exportUrl) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bills.xlsx");

        // Tạo workbook Excel và các sheet, row, cell tương ứng
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bills");

        // Tạo tiêu đề cột
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã Hóa Đơn");
        headerRow.createCell(1).setCellValue("Họ và Tên");
        headerRow.createCell(2).setCellValue("Số điện thoại");
        headerRow.createCell(3).setCellValue("Ngày đặt");
        headerRow.createCell(4).setCellValue("Tổng tiền");
        headerRow.createCell(5).setCellValue("Trạng thái");
        headerRow.createCell(6).setCellValue("Loại đơn");

        int rowNum = 1;
        for (BillDto bill : bills) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getCode());
            row.createCell(1).setCellValue(bill.getCustomerName());
            row.createCell(2).setCellValue(bill.getPhoneNumber());
            XSSFCell dateCell = row.createCell(3);
            XSSFCellStyle dateCellStyle = workbook.createCellStyle();
            XSSFDataFormat dataFormat = workbook.createDataFormat();
            dateCellStyle.setDataFormat(dataFormat.getFormat("dd/MM/yyyy"));
            dateCell.setCellStyle(dateCellStyle);
            dateCell.setCellValue(bill.getCreateDate());
            XSSFCell totalCell = row.createCell(4);
            totalCell.setCellValue(bill.getTotalPrice().doubleValue());

            XSSFCellStyle totalCellStyle = workbook.createCellStyle();
            XSSFDataFormat dataFormat1 = workbook.createDataFormat();
            totalCellStyle.setDataFormat(dataFormat1.getFormat("#,###"));
            totalCell.setCellStyle(totalCellStyle);
            String trangThaiText = "";
            switch (bill.getStatus()) {
                case 10:
                    trangThaiText = "Chờ xác nhận";
                    break;
                case 3:
                    trangThaiText = "Chờ giao hàng";
                    break;
                case 2:
                    trangThaiText = "Đang giao hàng";
                    break;
                case 1:
                    trangThaiText = "Đã hoàn thành";
                    break;
                case 0:
                    trangThaiText = "Đã hủy";
                    break;
                default:
                    trangThaiText = "  ";
            }
            String loaiDonText = "";
            switch (bill.getType()) {
                case 1:
                    loaiDonText = "Tại quầy";
                    break;
                case 0:
                    loaiDonText = "Mua Online";
                    break;
                default:
                    loaiDonText = "  ";
            }
            row.createCell(5).setCellValue(trangThaiText);
            row.createCell(6).setCellValue(loaiDonText);

            XSSFCell linkCell = row.createCell(7);
            XSSFRichTextString linkText = new XSSFRichTextString(" ");
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(exportUrl);
            linkCell.setHyperlink(hyperlink);
            linkCell.setCellValue(linkText);

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
        }
    }

    @Override
    public List<Bill> findAllByStatus(Integer status) {
        return billRepository.findAllByStatus(status);
    }

    @Override
    public void deleteBillDetail(BillDetail id) {
        id.setQuantity(0);
        id.setStatus(0);
        billDetailRepository.save(id);
    }
}
