package com.guli.teacher.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//前台讲师controller
@RestController
@RequestMapping("/eduservice/frontTeacher")
@CrossOrigin
public class FrontEduTeacherController {


    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;

    //讲师列表
    @GetMapping("{page}/{limit}")
    public Result getFrontTeacherListPage(
            @PathVariable Long page,
            @PathVariable Long limit){
        //实现分页查询
        Page<EduTeacher> page1 = new Page<>(page,limit);
        Map<String,Object> map = eduTeacherService.getFrontTeacherListPage(page1);

        return Result.ok().data(map);
    }

    //根据ID查询讲师详情信息(包含他的课程信息)
    @GetMapping("{id}")
    public  Result getTeacherById(
            @PathVariable String id
    ){
        //1.根据ID查询讲师信息，返回一个对象
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        //2.查询讲师所讲课程，返回集合
        List<EduCourse> courseList = eduTeacherService.getCourseListByTeacherId(id);
        return Result.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);
    }

}
