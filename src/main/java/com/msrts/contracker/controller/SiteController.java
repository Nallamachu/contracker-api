package com.msrts.contracker.controller;

import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.SiteDto;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hostel")
@RequiredArgsConstructor
public class SiteController {
    @Autowired
    private SiteService siteService;

    @GetMapping(path = "/find-all-sites-by-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<SiteDto>> getAllSitesByUserId(@RequestParam String contact,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "id,desc") String[] sort) {
        Response<List<SiteDto>> response = new Response<>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(sort));
        if (contact == null) {
            response.setErrors(List.of(new Error("INVALID_INPUT_ID", ErrorConstants.INVALID_INPUT_ID)));
            return response;
        }
        return siteService.findAllSitesByContact(response, contact, pagingSort);
    }

    @PostMapping(path = "/create-site", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<SiteDto> createSite(@RequestBody SiteDto siteDto) {
        Response<SiteDto> response = new Response<>();
        if (siteDto == null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }

        return siteService.createSite(response, siteDto);

    }

    @PutMapping(path = "/modify-site/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<SiteDto> modifySite(@PathVariable("id") Long id, @RequestBody SiteDto siteDto) {
        Response<SiteDto> response = new Response<>();
        if (id == null || siteDto == null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }

        return siteService.modifySite(id, siteDto, response);
    }

    @DeleteMapping(path = "/delete-site/{id}")
    public Response<String> deleteSite(@PathVariable("id") Long id) {
        Response<String> response = new Response<>();
        if (id == null) {
            response.setErrors(List.of(new Error("INVALID_REQUEST", ErrorConstants.INVALID_REQUEST)));
            return response;
        }

        return siteService.deleteSite(id, response);
    }
}
