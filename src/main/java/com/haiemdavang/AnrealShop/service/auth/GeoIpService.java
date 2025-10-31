package com.haiemdavang.AnrealShop.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class GeoIpService {

    public String getLocationFromIp(String ip) {
        try {
            String url = "https://ipapi.co/" + ip + "/json/";
            RestTemplate restTemplate = new RestTemplate();

            Map result = restTemplate.getForObject(url, Map.class);
            return result.get("city") + ", " + result.get("country_name");
        } catch (Exception e) {
            log.info("Failed to get location for IP: " + ip, e);
            return "Unknown";
        }
    }
}
