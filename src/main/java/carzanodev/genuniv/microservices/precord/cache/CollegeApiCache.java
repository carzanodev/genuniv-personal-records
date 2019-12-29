package carzanodev.genuniv.microservices.precord.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.precord.cache.model.CollegeDTO;
import carzanodev.genuniv.microservices.precord.config.IntraServiceProperties;

@Component
public class CollegeApiCache extends ApiCache<Integer, CollegeDTO, CollegeDTO.List> {

    @Autowired
    public CollegeApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getCollege().getCollegeApiUrl(),
                properties.getCollege().getCollegeApiUrl() + "/info");
    }

    @Override
    protected Collection<CollegeDTO> getResultData(StandardResponse<CollegeDTO.List> response) {
        return response.getResponse().getColleges();
    }

    @Override
    protected Integer id(CollegeDTO collegeDTO) {
        return collegeDTO.getId();
    }

}
