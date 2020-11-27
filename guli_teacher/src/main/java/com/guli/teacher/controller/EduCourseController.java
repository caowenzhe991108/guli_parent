package com.guli.teacher.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.vo.CourseInfoForm;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-19
 */
@RestController
@CrossOrigin //跨域
@RequestMapping("/eduservice/course")
@Api(description="课程管理")
public class EduCourseController {

    @Autowired
    EduCourseService courseService;

    @ApiOperation(value ="新增课程")
    @PostMapping("saveInfo")
    public Result saveCourseInfo(
            @ApiParam(name = "CourseInfoForm",value = "表单信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        String courseId = courseService.saveCourseInfo(courseInfoForm);
        if(!StringUtils.isEmpty(courseId)){
            System.out.print(courseId);
            return Result.ok().data("courseId", courseId);
        }else{
            return Result.error().message("保存失败");
        }
    }

    @ApiOperation(value ="根据id查课程")
    @GetMapping("courseInfoById/{id}")
    public Result CourseInfoById(
            @ApiParam(name = "id",value = "课程id",required = true)
            @PathVariable String id){

        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        return  Result.ok().data("item",courseInfoForm);
    }

    @ApiOperation(value ="根据id修改课程")
    @GetMapping("updateById/{id}")
    public Result UpdateById(
            @ApiParam(name = "courseInfo",value = "课程信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm ,
            @ApiParam(name = "id",value = "课程id",required = true)
            @PathVariable String id){

        courseService.updateCourseInfoById(courseInfoForm);
        return  Result.ok();
    }

    @ApiOperation(value = "条件查询课程")
    @GetMapping("query/{page}/{limit}")
    public Result pageQuery(
            @ApiParam(name = "page",value = "页数" ,required = true)
            @PathVariable long page,

            @ApiParam(name = "limit",value = "每页个数",required = true)
            @PathVariable long limit,

            @ApiParam(name = "courseQuery",value = "课程查询条件",required = false)
            CourseQuery courseQuery){

        Page<EduCourse> pageParam = new Page<>(page,limit);
        courseService.pageQuery(pageParam,courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return  Result.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value ="根据id删除课程")
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(

            @ApiParam(name = "id",value = "课程id",required = true)
            @PathVariable String id){

        boolean result = courseService.deleteCourseById(id);
        if(result) {
            return Result.ok();
        }
        return Result.error().message("删除失败");
    }




}

