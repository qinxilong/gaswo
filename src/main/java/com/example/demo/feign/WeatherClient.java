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
@FeignClient(name = "weatherClient", url ="${weather.weatherUrl}")
public interface WeatherClient {

    @RequestMapping(method = RequestMethod.GET, path = "${weather.nowPath}", consumes = "application/json")
    Object getWeatherNow(@SpringQueryMap WeatherRequestParams params);

    @RequestMapping(method = RequestMethod.GET, path = "${weather.24hPath}", consumes = "application/json")
    Object getWeather24h(@SpringQueryMap WeatherRequestParams params);

    @RequestMapping(method = RequestMethod.GET, path = "${weather.3dPath}", consumes = "application/json")
    Object getWeather3d(@SpringQueryMap WeatherRequestParams params);
}

