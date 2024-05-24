package com.msrts.contracker.model;

import com.msrts.contracker.entity.Equipment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    @NotBlank(message = "Transaction type should be CASH/UPI/CARD/ACCOUNT-TRANSFER")
    private String transactionType;
    @NotNull(message = "Start date should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @NotNull(message = "End date should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @Min(value = 0L, message = "Amount should be greater than Zero")
    private double amount;
    @NotNull(message = "Select the valid equipment to create expense record")
    private Equipment equipment;

}