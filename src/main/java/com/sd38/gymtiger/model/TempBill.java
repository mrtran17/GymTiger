/*
 *  © 2023 Nyaruko166
 */

/*
 *  © 2023 Nyaruko166
 */

package com.sd38.gymtiger.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TempBill {

    private Integer billId;

    private String billCode;

    private Integer idCustomer;

    private Integer idEmployee;

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private BigDecimal totalCartPrice;

    private List<OfflineCart> lstDetailProduct;

    public Integer billIdPP() {
        return billId + 1;
    }
}
