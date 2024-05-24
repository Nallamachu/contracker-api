package com.msrts.contracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrts.contracker.entity.Equipment;
import com.msrts.contracker.entity.Work;
import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.EquipmentDto;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Response<EquipmentDto> createEquipment(EquipmentDto equipmentDto, Response<EquipmentDto> response) {
        Equipment equipment = objectMapper.convertValue(equipmentDto, Equipment.class);
        equipment = equipmentRepository.save(equipment);
        equipmentDto = objectMapper.convertValue(equipment, EquipmentDto.class);
        response.setData(equipmentDto);
        return response;
    }

    public Response<List<EquipmentDto>> findAllEquipmentsBySiteId(Long siteId, Response<List<EquipmentDto>> response, Pageable pageable) {
        List<Equipment> equipments = equipmentRepository.findAllEquipmentsBySiteId(siteId, pageable);
        if(!equipments.isEmpty()) {
            List<EquipmentDto> equipmentDtos = equipments.stream().map(room -> objectMapper.convertValue(room, EquipmentDto.class)).toList();
            response.setData(equipmentDtos);
        }
        return response;
    }

    public Response<String> deleteEquipmentById(Long equipmentId, Response<String> response) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        if(optionalEquipment.isEmpty()){
            response.setErrors(List.of(new Error("ERROR_ROOM_NOT_FOUND", ErrorConstants.ERROR_ROOM_NOT_FOUND)));
            return response;
        }

        Set<Work> workSet = optionalEquipment.get().getWorks();
        if(!workSet.isEmpty()) {
            response.setErrors(List.of(new Error("ERROR_ROOM_NOT_EMPTY", ErrorConstants.ERROR_ROOM_NOT_EMPTY)));
            return response;
        }

        equipmentRepository.deleteById(equipmentId);
        response.setData("Site deleted with id of "+equipmentId);
        return response;
    }

    public Response<EquipmentDto> modifyEquipment(Long id, EquipmentDto equipmentDto, Response<EquipmentDto> response) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);
        if(optionalEquipment.isPresent()) {
            Equipment equipment = optionalEquipment.get();
            equipment.setName(equipmentDto.getName());
            equipment.setModel(equipmentDto.getModel());
            equipment.setEngineNo(equipmentDto.getEngineNo());
            // equipment.setWorks(equipmentDto.getWorks());
            equipment.setChasisNo(equipmentDto.getChasisNo());
            equipment.setHp(equipmentDto.getHp());
            equipment.setType(equipmentDto.getType());
            equipment.setOwner(equipmentDto.getOwner());
            equipment = equipmentRepository.save(equipment);
            equipmentDto = objectMapper.convertValue(equipment, EquipmentDto.class);
            response.setData(equipmentDto);
        } else {
            response.setErrors(List.of(new Error("ERROR_ROOM_NOT_FOUND", ErrorConstants.ERROR_ROOM_NOT_FOUND)));
        }
        return response;
    }
}
