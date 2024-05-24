package com.msrts.contracker.model;

import com.msrts.contracker.entity.Address;
import com.msrts.contracker.entity.Equipment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDto {

    private Long id;
    @NotBlank(message = "Site name should not be blank")
    private String name;
    @NotBlank(message = "Site engineer should not be blank")
    private String engineer;
    private String company;
    @NotBlank(message = "Contact should not be blank")
    private String contact;
    private boolean closed;
    private Address address;
    private List<Equipment> equipments;
}
