package com.example.appcompany.models;

import com.example.appcompany.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    @NotNull(message = "name kiritilmadi")
    private String name;

    @NotNull(message = "company_id kiritilmadi")
    private Integer company_id;
}
