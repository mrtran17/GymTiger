package com.sd38.gymtiger.config;

import lombok.Getter;

import java.util.Random;

public class MailConfig {

    public String company = "GymTiger";

    public String contact = "gymsharkcskh@gmail.com";

    public String confirmMail = "Mã Xác Nhận Cho Tài Khoản Của Bạn";

    public String resetPassMail = "Mã Xác Nhận Cho Yêu Cầu Đặt Lại Mật Khẩu Của Bạn";

    public String changePassMail = "Mã Xác Nhận Cho Yêu Cầu Thay Đổi Mật Khẩu Của Bạn";

    public String placeOrderMail = "Thông báo đặt hàng thành công tại GymTiger Store";

    public String randomCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.format("%06d", randomNumber);
    }

}
