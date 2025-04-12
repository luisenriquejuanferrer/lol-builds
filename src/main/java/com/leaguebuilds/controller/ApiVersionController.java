package com.leaguebuilds.controller;

import com.leaguebuilds.service.ApiVersionService;
import com.leaguebuilds.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Utils.BASE_API_URL)
public class ApiVersionController {

    private ApiVersionService apiVersionService;

    public ApiVersionController(ApiVersionService apiVersionService) {
        this.apiVersionService = apiVersionService;
    }

    @GetMapping("/versions")
    public String[] getApiVersions(){
        return apiVersionService.loadApiVersions();
    }
}
