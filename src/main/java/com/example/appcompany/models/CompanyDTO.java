package com.example.appcompany.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    @NotNull(message = "corpName kiritilmadi")
    private String corpName;

    @NotNull(message = "directorName kiritilmadi")
    private String directorName;

    @NotNull(message = "address_id kiritilmadi")
    private Integer address_id;
}
