package com.msrts.contracker.controller;

import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.PaymentDto;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(path = "/payments-by-equipment-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<PaymentDto>> getAllPaymentsByEquipmentId(@RequestParam(required = true) Long equipment,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<PaymentDto>> response = new Response<>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(sort));
        if (equipment == 0 || equipment < 0) {
            Error error = new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID);
            response.setErrors(List.of(error));
            return response;
        }
        return paymentService.getAllPaymentsByEquipmentId(equipment, response, pagingSort);
    }

    @PostMapping(path = "/create-payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<PaymentDto> createPaymentRecord(@RequestBody PaymentDto paymentDto) {
        Response<PaymentDto> response = new Response<>();
        if(paymentDto==null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return paymentService.createPayment(paymentDto, response);
    }

    @GetMapping(path = "/payments-by-equipment-id-and-time-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<PaymentDto>> getAllPaymentsByEquipmentIdAndTimePeriod(@RequestParam(required = true) Long equipmentId,
                                                                            @RequestParam(defaultValue = "CURRENT_MONTH",required = true) String timePeriod,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<PaymentDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        if (equipmentId == 0 || equipmentId < 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return paymentService.getPaymentsByEquipmentIdAndTimePeriod(equipmentId, timePeriod, response, pageable);

    }

    @PutMapping(path = "modify-payment/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PaymentDto> modifyPaymentById(@PathVariable("id") Long id, PaymentDto paymentDto) {
        Response<PaymentDto> response = new Response<>();
        if(id == null || id >= 0 || paymentDto == null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return paymentService.modifyPayment(id, paymentDto, response);
    }

    @DeleteMapping(path = "/delete-payment/{id}")
    public Response<String> deletePaymentById(@PathVariable("id") Long id){
        Response<String> response = new Response<>();
        if(id == null || id <= 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return paymentService.deletePaymentById(id, response);
    }

}
