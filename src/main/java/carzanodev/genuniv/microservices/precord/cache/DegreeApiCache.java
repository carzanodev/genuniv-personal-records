package carzanodev.genuniv.microservices.precord.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.precord.cache.model.DegreeDTO;
import carzanodev.genuniv.microservices.precord.cache.model.DegreeDTO.List;
import carzanodev.genuniv.microservices.precord.config.IntraServiceProperties;

@Component
public class DegreeApiCache extends ApiCache<Integer, DegreeDTO, DegreeDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<DegreeDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public DegreeApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getCollege().getDegreeApiUrl(),
                properties.getCollege().getDegreeApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<DegreeDTO> getResultData(StandardResponse<DegreeDTO.List> response) {
        return response.getResponse().getDegrees();
    }

    @Override
    protected Integer id(DegreeDTO degreeDTO) {
        return degreeDTO.getId();
    }

}
