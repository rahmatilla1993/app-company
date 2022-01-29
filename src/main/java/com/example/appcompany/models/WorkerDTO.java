package com.example.appcompany.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDTO {

    @NotNull(message = "firstName kiritilmadi")
    private String firstName;

    @NotNull(message = "lastName kiritilmadi")
    private String lastName;

    @NotNull(message = "phoneNumber kiritilmadi")
    private String phoneNumber;

    @NotNull(message = "address_id kiritilmadi")
    private Integer address_id;

    @NotNull(message = "department_id kiritilmadi")
    private Integer department_id;
}
