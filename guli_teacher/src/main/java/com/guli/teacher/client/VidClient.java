package com.guli.teacher.client;


import com.guli.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("guli-video1")
@Component
public interface VidClient {
    //定义调用的方法
    //方法调用路径
    @DeleteMapping("/vidservice/vod/{videoId}")
    public Result removeVideoAliyunId(@PathVariable("videoId") String videoId);//@PathVariable中必须要加值，不然会报错

    @DeleteMapping("/vidservice/vod/delete-batch")
    public Result removeVideoList(@RequestParam("videoIdList") List videoList);
}
