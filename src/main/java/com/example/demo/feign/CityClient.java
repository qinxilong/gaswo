package com.example.demo.feign;

import com.example.demo.request.WeatherRequestParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 客户端
 * @author
 */
@Component
@FeignClient(name = "citylient", url ="${weather.cityUrl}")
public interface CityClient {
    @RequestMapping(method = RequestMethod.GET, path = "${weather.cityPath}", consumes = "application/json")
    Object getCity(@SpringQueryMap WeatherRequestParams params);
}

