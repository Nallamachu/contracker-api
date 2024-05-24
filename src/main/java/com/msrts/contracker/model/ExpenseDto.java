package com.msrts.contracker.model;

import com.msrts.contracker.entity.Equipment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {

    private Long id;
    @NotNull(message = "Expense Type should be mandatory")
    private String expenseType;
    private String description;
    @Min(value = 0L, message = "Minimum amount value should be greater than Zero")
    private double amount;
    @NotNull(message = "Expense date should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @NotNull(message = "Select the valid equipment to create expense record")
    private Equipment equipment;
}
