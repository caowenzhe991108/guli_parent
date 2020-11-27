package com.guli.staticcenter.controller;


import com.guli.common.result.Result;
import com.guli.staticcenter.service.StatisticsDailyService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-11-19
 */
@RestController
@CrossOrigin
@RequestMapping("/statisticservice/staticcenter")
public class StatisticsDailyController {

    @Autowired
    StatisticsDailyService statisticsDailyService;

    @PostMapping("{day}")
    public Result createStatisticsByDate(@PathVariable String day){
        statisticsDailyService.createStatisticsByDay(day);
        return Result.ok();
    }
}

