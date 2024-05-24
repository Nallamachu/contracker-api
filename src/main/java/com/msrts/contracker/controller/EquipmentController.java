package com.msrts.contracker.controller;

import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.model.EquipmentDto;
import com.msrts.contracker.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping(path = "/equipments-by-site-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<EquipmentDto>> getAllEquipmentsBySiteId(@RequestParam(required = true) Long siteId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<EquipmentDto>> response = new Response<>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(sort));
        if (siteId == 0 || siteId < 0) {
            Error error = new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID);
            response.setErrors(List.of(error));
            return response;
        }
        return equipmentService.findAllEquipmentsBySiteId(siteId, response, pagingSort);
    }

    @PostMapping(path = "/create-equipment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<EquipmentDto> createEquipment(@RequestBody EquipmentDto equipmentDto) {
        Response<EquipmentDto> response = new Response<>();
        if(equipmentDto ==null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return equipmentService.createEquipment(equipmentDto, response);
    }

    @PutMapping(path = "/modify-equipment/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<EquipmentDto> modifyEquipment(@PathVariable("id") Long id, @RequestBody EquipmentDto equipmentDto) {
        Response<EquipmentDto> response = new Response<>();
        if(id== null || equipmentDto ==null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }
        return equipmentService.modifyEquipment(id, equipmentDto, response);
    }


    @DeleteMapping(path = "/delete-equipment-by-id/{id}")
    public Response<String> deleteEquipmentById(@PathVariable(value = "id") Long id) {
        Response<String> response = new Response<>();
        if(id == null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID",ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }
        return equipmentService.deleteEquipmentById(id, response);
    }
}
