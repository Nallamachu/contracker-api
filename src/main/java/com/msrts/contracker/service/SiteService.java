package com.msrts.contracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msrts.contracker.entity.Site;
import com.msrts.contracker.entity.User;
import com.msrts.contracker.exception.ErrorConstants;
import com.msrts.contracker.model.Error;
import com.msrts.contracker.model.Response;
import com.msrts.contracker.model.SiteDto;
import com.msrts.contracker.repository.SiteRepository;
import com.msrts.contracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteService {
    //private static final Logger log = LoggerFactory.getLogger(SiteService.class);

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Response<List<SiteDto>> findAllSitesByContact(Response<List<SiteDto>> response, String contact, Pageable pageable){
        //log.info("Start of finding all equipment by current logged in user id");
        if(contact != null) {
            //log.error(ErrorConstants.ERROR_USER_NOT_FOUND);
            Error error = new Error("ERROR_USER_NOT_FOUND", ErrorConstants.ERROR_USER_NOT_FOUND);
            response.setErrors(List.of(error));
            return response;
        }

        List<Site> siteList = siteRepository.findAllSitesByContact(contact, pageable);
        if(siteList != null && !siteList.isEmpty()) {
            List<SiteDto> siteDtos = siteList.stream()
                    .map(hostel -> objectMapper.convertValue(hostel, SiteDto.class)).toList();
            response.setData(siteDtos);
        }
        //log.info("End of finding all equipment by current logged in user id");
        return response;
    }

    public Response<SiteDto> createSite(Response<SiteDto> response, SiteDto siteDto) {
        //log.info("Start of creating new equipment");
        Site site = objectMapper.convertValue(siteDto, Site.class);
        site = siteRepository.save(site);
        siteDto = objectMapper.convertValue(site, SiteDto.class);
        if(siteDto != null) {
            response.setData(siteDto);
        }
        //log.info("End of creating new equipment");
        return response;
    }

    private boolean validateUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.isEmpty();
    }

    public Response<SiteDto> modifySite(Long id, SiteDto siteDto, Response<SiteDto> response) {
        Optional<Site> optionalSite = siteRepository.findById(id);
        if(optionalSite.isPresent()) {
            Site site = optionalSite.get();
            site.setName(siteDto.getName());
            site.setEngineer(siteDto.getEngineer());
            site.setCompany(siteDto.getCompany());
            site.setContact(siteDto.getContact());
            site.setClosed(siteDto.isClosed());
            site.setAddress(siteDto.getAddress());
            site = siteRepository.save(site);
            siteDto = objectMapper.convertValue(site, SiteDto.class);
            response.setData(siteDto);
        } else {
            response.setErrors(List.of(new Error("ERROR_HOSTEL_NOT_FOUND", ErrorConstants.ERROR_HOSTEL_NOT_FOUND)));
        }
        return response;
    }

    public Response<String> deleteSite(Long id, Response<String> response) {
        Optional<Site> optionalSite = siteRepository.findById(id);
        if(optionalSite.isPresent()) {
            Site site = optionalSite.get();
            site.setClosed(false);
            siteRepository.save(site);
            response.setData(site.getName() + " equipment deleted successfully");
        } else {
            response.setErrors(List.of(new Error("ERROR_HOSTEL_NOT_FOUND", ErrorConstants.ERROR_HOSTEL_NOT_FOUND)));
        }
        return response;
    }
}
