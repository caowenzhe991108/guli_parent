package com.guli.teacher.controller;



import com.guli.common.result.Result;
import com.guli.teacher.service.EduVideoService;
import com.guli.teacher.vo.VideoInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-21
 */
@RestController
@RequestMapping("/eduservice/video")
@Api(description = "课程管理")
@CrossOrigin//解决跨域问题
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @ApiOperation(value = "新增课时")
    @PostMapping("save-video-info")
    public Result saveVideoInfo(
            @ApiParam(name = "videoForm",value="视频表单信息")
            @RequestBody VideoInfoForm videoInfoForm){

            videoService.saveVideoInfo(videoInfoForm);
            return Result.ok();
    }

    @ApiOperation(value = "根据ID查课时")
    @GetMapping("video-info/{id}")
    public Result saveVideoInfo(
            @ApiParam(name = "id",value = "ID")
            @PathVariable String id){

        VideoInfoForm videoInfoForm = videoService.getVideoInfoFormById(id);
        return Result.ok().data("item", videoInfoForm);
    }


    @ApiOperation(value = "更新课时")
    @PutMapping("update-video-info/{id}")
    public Result updateCourseInfoById(
        @ApiParam(name = "VideoInfoForm", value = "课时基本信息", required = true)
        @RequestBody VideoInfoForm videoInfoForm,
        @ApiParam(name = "id", value = "课时ID", required = true)
        @PathVariable String id){

        videoService.updateVideoInfoById(videoInfoForm);
        return Result.ok();
    }


    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("{id}")
    public Result removeById(
        @ApiParam(name = "id", value = "课时ID", required = true)
        @PathVariable String id){

        boolean result = videoService.removeVideoById(id);
        if(result){
            return Result.ok();
        }else{
            return Result.error().message("删除失败");
        }
    }


}


