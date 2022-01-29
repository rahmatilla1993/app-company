package com.example.appcompany.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddressDTO {

    @NotNull(message = "street kiritilmadi")
    private String street;

    @NotNull(message = "homeNumber kiritilmadi")
    private Integer homeNumber;
}
