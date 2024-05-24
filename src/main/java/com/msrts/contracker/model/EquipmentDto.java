package com.msrts.contracker.model;

import com.msrts.contracker.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDto {
    private Long id;
    @NotBlank(message = "Equipment name should not be blank")
    private String name;
    @NotBlank(message = "Equipment model should not be blank")
    private Long model;
    @NotBlank(message = "Equipment engineNo should not be blank")
    private String engineNo;
    @NotBlank(message = "Equipment chasisNo should not be blank")
    private String chasisNo;
    @Size(message = "Equipment hp should not be Zero")
    private Long hp;
    @NotBlank(message = "Equipment type should not be blank")
    private EquipmentType type;

    private Set<Site> sites;
    private Set<Work> works;
    private User owner;
}
