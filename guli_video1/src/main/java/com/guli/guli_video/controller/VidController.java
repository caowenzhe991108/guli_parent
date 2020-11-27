package com.guli.guli_video.controller;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.guli.common.result.Result;
import com.guli.guli_video.service.VidService;
import com.guli.guli_video.utils.AliyunVodSDKUtils;
import com.guli.guli_video.utils.ConstantPropertiesUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/vidservice/vod")
public class VidController {

    @Autowired
    VidService vidService;

    //上传视频到阿里云服务器
    @PostMapping("upload")
    public Result uploadAliyunVideo(@RequestParam("file") MultipartFile file){
        //调用方法实现视频上传,返回上传之后视频ID
        String videoId = vidService.uploadVideAliyun(file);
        return Result.ok().data("videoId",videoId);
    }

    @DeleteMapping("{videoId}")
    public Result removeVideo(@PathVariable String videoId){
        vidService.removeVideo(videoId);
        return Result.ok().message("视频删除成功");
    }

    /**
     * 批量删除视频
     * @param videoIdList
     * @return
     */
    @DeleteMapping("delete-batch")
    public Result removeVideoList(
            @ApiParam(name = "videoIdList", value = "云端视频id", required = true)
            @RequestParam("videoIdList") List videoIdList){
        vidService.removeVideoList(videoIdList);
        return Result.ok().message("视频删除成功");
    }



        @GetMapping("get-play-auth/{videoId}")

        public Result getVideoPlayAuth(@PathVariable("videoId") String videoId) throws Exception {
            //获取阿里云存储相关常量
            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            //初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(accessKeyId, accessKeySecret);
            //请求
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //得到播放凭证
            String playAuth = response.getPlayAuth();
            //返回结果
            return Result.ok().message("获取凭证成功").data("playAuth", playAuth);
        }




}
