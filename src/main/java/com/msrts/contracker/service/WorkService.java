package com.msrts.contracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrts.contracker.entity.Equipment;
import com.msrts.contracker.entity.Work;
import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.model.WorkDto;
import com.msrts.contracker.repository.SiteRepository;
import com.msrts.contracker.repository.EquipmentRepository;
import com.msrts.contracker.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkService {
    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Response<WorkDto> createWork(WorkDto workDto, Response<WorkDto> response) {
        if(workDto != null && workDto.getEquipment().getId()!=null) {
            Optional<Equipment> optionalEquipment = equipmentRepository.findById(workDto.getEquipment().getId());
            if(optionalEquipment.isEmpty()) {
                response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID + " for Work")));
                return response;
            }

            Work work = objectMapper.convertValue(workDto, Work.class);
            work = workRepository.save(work);
            workDto = objectMapper.convertValue(work, WorkDto.class);
            response.setData(workDto);
        } else {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return response;
    }

    public Response<List<WorkDto>> getAllWorksByEquipmentId(Long equipmentId, Pageable pageable, Response<List<WorkDto>> response) {
        List<Work> works = workRepository.findAllWorksByEquipmentId(equipmentId, pageable);
        if (!works.isEmpty()) {
            List<WorkDto> workDtos = works.stream().map(work -> objectMapper.convertValue(work, WorkDto.class)).toList();
            response.setData(workDtos);
        } else {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return  response;
    }

    public Response<List<WorkDto>> getAllWorksBetweenDateAndEquipmentId(Long equipmentId, String startDate, String endDate, Response<List<WorkDto>> response) {
        List<Work> works = workRepository.findAllWorksByEquipmentIdAndDateRange(equipmentId, startDate, endDate);
        if (!works.isEmpty()) {
            List<WorkDto> workDtos = works.stream().map(work -> objectMapper.convertValue(work, WorkDto.class)).toList();
            response.setData(workDtos);
        } else {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return  response;
    }

    public Response<WorkDto> modifyWork(Long id, WorkDto workDto, Response<WorkDto> response) {
        Optional<Work> optionalWork = workRepository.findById(id);
        if(optionalWork.isPresent()) {
            Work work = getWork(workDto, optionalWork.get());
            work = workRepository.save(work);
            workDto = objectMapper.convertValue(work, WorkDto.class);
            response.setData(workDto);
        } else {
            response.setErrors(List.of(new Error("ERROR_TENANT_NOT_FOUND", ErrorConstants.ERROR_TENANT_NOT_FOUND)));
        }
        return response;
    }

    private static Work getWork(WorkDto workDto, Work work) {
        work.setSiteName(workDto.getSiteName());
        work.setDate(workDto.getDate());
        work.setPaymentDone(workDto.isPaymentDone());
        work.setRatePerHr(workDto.getRatePerHr());
        work.setRatePerMonth(workDto.getRatePerMonth());
        work.setRentOnMonth(workDto.isRentOnMonth());
        work.setEquipment(workDto.getEquipment());
        work.setTotalHours(workDto.getTotalHours());
        return work;
    }

    public Response<String> deleteWorkById(Long id, Response<String> response) {
        Optional<Work> optionalTenant = workRepository.findById(id);
        if(optionalTenant.isPresent()) {
            Work work = optionalTenant.get();
            work.setPaymentDone(true);
            workRepository.save(work);
            response.setData("Work deleted successfully");
        } else {
            response.setErrors(List.of(new Error("ERROR_TENANT_NOT_FOUND", ErrorConstants.ERROR_TENANT_NOT_FOUND + id)));
        }
        return response;
    }

}
