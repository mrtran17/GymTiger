/*
 *  © 2023 Nyaruko166
 */

/*
 *  © 2023 Nyaruko166
 */

package com.sd38.gymtiger.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Province {

    private Integer ProvinceID;

    private String ProvinceName;

    private List<String> NameExtension;
}
