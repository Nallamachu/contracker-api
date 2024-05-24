package com.msrts.contracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrts.contracker.entity.Payment;
import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.PaymentDto;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public Response<PaymentDto> createPayment(PaymentDto paymentDto, Response<PaymentDto> response) {
        if(paymentDto.getAmount() <= 0) {
            response.setErrors(List.of(new Error("INVALID_AMOUNT", ErrorConstants.INVALID_AMOUNT)));
            return response;
        }
        Payment payment = objectMapper.convertValue(paymentDto, Payment.class);
        payment = paymentRepository.save(payment);
        if(payment.getId() != null) {
            paymentDto = objectMapper.convertValue(payment, PaymentDto.class);
            response.setData(paymentDto);
        }
        return response;
    }

    public Response<List<PaymentDto>> getAllPaymentsByEquipmentId(Long equipmetId, Response<List<PaymentDto>> response, Pageable pageable) {
        List<Payment> payments = paymentRepository.findAllByEquipment(equipmetId, pageable);
        if(!payments.isEmpty()) {
            List<PaymentDto> paymentDtos = payments.stream().map(payment -> objectMapper.convertValue(payment, PaymentDto.class)).toList();
            response.setData(paymentDtos);
        }
        return response;
    }

    @Transactional
    public Response<List<PaymentDto>> getPaymentsByEquipmentIdAndTimePeriod(Long equipmentId, String timePeriod, Response<List<PaymentDto>> response, Pageable pageable) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        List<Payment> payments = null;

        if(equipmentId != null && timePeriod.equalsIgnoreCase(ErrorConstants.TIME_PERIOD_LAST_MONTH)){
            String startDate = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).format(format);
            String endDate = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).format(format);
            payments = paymentRepository.findAllPaymentsByEquipmentIdsAndTimePeriod(
                    equipmentId,
                    startDate,
                    endDate,
                    pageable);
        } else if(equipmentId != null && timePeriod.equalsIgnoreCase(ErrorConstants.TIME_PERIOD_CURRENT_MONTH)) {
            String startDate = now.with(TemporalAdjusters.firstDayOfMonth()).format(format);
            payments = paymentRepository.findAllPaymentsByEquipmentIdsAndTimePeriod(
                    equipmentId,
                    startDate,
                    now.format(format),
                    pageable);
        } else {
            response.setErrors(List.of(new Error("INVALID_TIME_PERIOD", ErrorConstants.INVALID_TIME_PERIOD)));
        }

        if(payments != null) {
            List<PaymentDto> paymentDtos = payments.stream().map(expense -> objectMapper.convertValue(expense, PaymentDto.class)).toList();
            response.setData(paymentDtos);
        }
        return response;
    }

    public Response<PaymentDto> modifyPayment(Long id, PaymentDto paymentDto, Response<PaymentDto> response) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setAmount(paymentDto.getAmount());
            payment.setStartDate(paymentDto.getStartDate());
            payment.setEndDate(paymentDto.getEndDate());
            payment.setTransactionType(paymentDto.getTransactionType());
            payment = paymentRepository.save(payment);
            paymentDto = objectMapper.convertValue(payment, PaymentDto.class);
            response.setData(paymentDto);
        } else {
            response.setErrors(List.of(new Error("ERROR_PAYMENT_NOT_FOUND", ErrorConstants.ERROR_PAYMENT_NOT_FOUND + id)));
        }
        return response;
    }

    public Response<String> deletePaymentById(Long id, Response<String> response) {
        paymentRepository.deleteById(id);
        response.setData("Payment deleted with the id of " + id);
        return response;
    }

}
