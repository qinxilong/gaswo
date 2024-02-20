package com.example.demo.controller;

import com.example.demo.feign.CityClient;
import com.example.demo.feign.WeatherClient;
import com.example.demo.request.WeatherRequestParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WeatherController")
@RequestMapping("/qy/gas")
@RestController
public class WeatherController {

    @Autowired
    private WeatherClient weatherClient;

    @Autowired
    private CityClient citylient;

    @ApiOperation(value = "获取实时天气")
    @GetMapping("api/v7/weather/now")
    public Object getWeatherNow(WeatherRequestParams params) {
        Object weatherNow = weatherClient.getWeatherNow(params);
//        System.out.println(weatherNow);
        return  weatherNow;
    }

    @ApiOperation(value = "获取24h天气")
    @GetMapping("api/v7/weather/24h")
    public Object getWeather24h(WeatherRequestParams params) {
        Object weather24h = weatherClient.getWeather24h(params);
//        System.out.println(weather24h);
        return weather24h;
    }

    @ApiOperation(value = "获取未来3d天气")
    @GetMapping("api/v7/weather/3d")
    public Object getWeather3d(WeatherRequestParams params) {
        Object weather3d =weatherClient.getWeather3d(params);
//        System.out.println(weather3d);
        return weather3d;
    }

    @ApiOperation(value = "获取城市列表")
    @GetMapping("api/v2/city/lookup")
    public Object getCity(WeatherRequestParams params) {
        if(params.getNumber()<1){
            params.setNumber(1);
        }
        Object cityList = citylient.getCity(params);
//        System.out.println(cityList);
        return cityList;
    }


}
