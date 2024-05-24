package com.msrts.contracker.controller;

import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.model.WorkDto;
import com.msrts.contracker.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/work")
public class WorkController {

    @Autowired
    private WorkService workService;

    @PostMapping(path = "/create-work", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WorkDto> createWork(@RequestBody WorkDto workDto) {
        Response<WorkDto> response = new Response<>();
        if(workDto ==null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return workService.createWork(workDto, response);
    }

    @GetMapping(path = "/all-works-by-equipment-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<WorkDto>> getAllWorksByEquipmentId(@RequestParam(required = true) Long equipmentId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<WorkDto>> response = new Response<>();
        if (equipmentId == 0 || equipmentId < 0) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return workService.getAllWorksByEquipmentId(equipmentId, pageable, response);
    }

    @GetMapping(path = "/all-works-by-equipment-id-and-date-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<WorkDto>> findAllWorksByEquipmentIdAndDateRange(@RequestParam(required = true) Long equipmentId,
                                                                       @RequestParam(required = true) String startDate,
                                                                       @RequestParam(required = true) String endDate,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<WorkDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        if (equipmentId == null || startDate == null || endDate == null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }

        return workService.getAllWorksBetweenDateAndEquipmentId(equipmentId, startDate, endDate, response);
    }

    @PutMapping(path = "/modify-work/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<WorkDto> modifyWork(@PathVariable("id") Long id, @RequestBody WorkDto workDto) {
        Response<WorkDto> response = new Response<>();
        if(id == null || workDto == null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return workService.modifyWork(id, workDto, response);
    }

    @DeleteMapping(path = "/delete-work-by-id/{id}")
    public Response<String> deleteWorkById(@PathVariable(value = "id") Long id) {
        Response<String> response = new Response<>();
        if(id==null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID",ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }
        return workService.deleteWorkById(id, response);
    }
}
