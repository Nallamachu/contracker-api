package com.msrts.contracker.controller;

import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.ExpenseDto;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(path = "/create-expense", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<ExpenseDto> createExpenseRecord(@RequestBody ExpenseDto expenseDto) {
        Response<ExpenseDto> response = new Response<>();
        if(expenseDto==null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return expenseService.saveExpenseDetails(expenseDto, response);
    }

    @GetMapping(path = "/find-all-expenses-by-equipment-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ExpenseDto>> getAllExpensesByEquipmentId(@RequestParam(required = true) Long equipmentId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<ExpenseDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        if (equipmentId == 0 || equipmentId < 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return expenseService.getExpensesByEquipmentId(equipmentId, response, pageable);

    }

    @GetMapping(path = "/find-all-expenses-by-equipment-id-and-time-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ExpenseDto>> getAllExpensesByEquipmentIdAndTimePeriod(@RequestParam(required = true) Long equipmentId,
                                                               @RequestParam(defaultValue = "CURRENT_MONTH",required = true) String timePeriod,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<ExpenseDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        if (equipmentId == 0 || equipmentId < 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return expenseService.getAllExpensesByTimePeriod(equipmentId, timePeriod, response, pageable);

    }

    @PutMapping(path = "modify-expense/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ExpenseDto> modifyExpenseById(@PathVariable("id") Long id, ExpenseDto expenseDto) {
        Response<ExpenseDto> response = new Response<>();
        if(id == null || id >= 0 || expenseDto == null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return expenseService.modifyExpenseDetails(id, expenseDto, response);
    }

    @DeleteMapping(path = "/delete-expense/{id}")
    public Response<String> deleteExpenseById(@PathVariable("id") Long id){
        Response<String> response = new Response<>();
        if(id == null || id <= 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return expenseService.deleteExpenseById(id, response);
    }

}
