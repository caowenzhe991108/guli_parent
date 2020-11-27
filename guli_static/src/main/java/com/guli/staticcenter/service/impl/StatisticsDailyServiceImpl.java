package com.guli.staticcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.result.Result;
import com.guli.staticcenter.client.UcenterClient;
import com.guli.staticcenter.entity.StatisticsDaily;
import com.guli.staticcenter.mapper.StatisticsDailyMapper;
import com.guli.staticcenter.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-11-19
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {


    @Autowired
    private UcenterClient ucenterClient;
    
    @Override
    public void createStatisticsByDay(String day) {

        //1.先删除已经存在的当前日期那一行
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //2.获取统计信息
        Result result = ucenterClient.registerCount(day);
        Integer countRegister = (Integer) result.getData().get("registerCount");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        //3.创建统计对象，将该对象添加到数据库中
        StatisticsDaily daily =new StatisticsDaily();
        daily.setRegisterNum(countRegister);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        baseMapper.insert(daily);
    }
}
