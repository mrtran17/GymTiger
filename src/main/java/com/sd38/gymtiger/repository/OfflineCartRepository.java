/*
 *  © 2023 Nyaruko166
 */

package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.OfflineCart;
import com.sd38.gymtiger.model.TempBill;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Getter
@Setter
public class OfflineCartRepository {

    private List<OfflineCart> cartSP = new ArrayList<>();

    private List<TempBill> lstTempBill = new ArrayList<>();

}
