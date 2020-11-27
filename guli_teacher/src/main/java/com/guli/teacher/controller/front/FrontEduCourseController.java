package com.guli.teacher.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;

import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.vo.ChapterVo;
import com.guli.teacher.vo.CourseWebVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/eduservice/frontCourse")
@CrossOrigin
public class FrontEduCourseController {

    @Autowired
    EduCourseService courseService;

    @Autowired
    EduChapterService chapterService;

    @GetMapping("{page}/{limit}")
    public Result coursePageList(@PathVariable Long page ,
                                 @PathVariable Long limit){

        Page<EduCourse> page1 = new Page<>(page,limit);//这个Page类是MP的类
        Map<String,Object> map = courseService.getFrontCourseListPage(page1);
        return Result.ok().data(map);
    }


    @ApiOperation(value = "根据ID查询课程")
    @GetMapping( "{courseId}")
    public Result getById(
        @ApiParam(name = "courseId", value = "课程ID", required = true)
        @PathVariable String courseId){
        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);
        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return Result.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList);
    }




}
