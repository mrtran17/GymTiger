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
public class District {

    private Integer DistrictID;

    private String DistrictName;

    private List<String> NameExtension;
}
