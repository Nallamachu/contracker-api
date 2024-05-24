package com.msrts.contracker.model;

import com.msrts.contracker.entity.Equipment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDto {

    private Long id;
    @NotBlank(message = "Site name should not be blank")
    private String siteName;
    private boolean rentOnMonth;
    @Size(min = 1, message = "Hourly rental amount should not be zero")
    private Double ratePerHr;
    private Double ratePerMonth;
    @Size(min = 0, message = "Hours worked per day should not be zero")
    private Double totalHours;
    private boolean paymentDone;
    @NotNull(message = "Date should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Equipment equipment;
}
