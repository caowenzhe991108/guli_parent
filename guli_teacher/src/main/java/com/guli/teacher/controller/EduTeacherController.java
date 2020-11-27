package com.guli.teacher.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.common.result.ResultCode;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;

import com.guli.teacher.exception.EduException;
import com.guli.teacher.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author guli
 * @since 2019-08-14
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin   //解决跨域问题
@Api(description = "讲师管理")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    //1.处理登录
    @PostMapping("login")
    public Result login(){
        return Result.ok().data("token","admin");
    }

    @GetMapping("info")
    public Result info(){
        //"data":
        // {"roles":["admin"],
        // "name":"admin",
        // "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}
        return Result.ok()
                .data("roles","[\"admin\"]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public Result list(){
        //int i = 1/0; //ArithmeticException
        try {
            List<EduTeacher> list = teacherService.list(null);
            return Result.ok().data("items",list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "所有讲师名字  这个方法不测，同上一个方法，是我多此一举" )
    @GetMapping("listname")
    public Result listname(){
        //int i = 1/0; //ArithmeticException
        try {
            ArrayList<String> nameList = new ArrayList<>();
            List<EduTeacher> list = teacherService.list(null);
            for(int i=0;i<list.size();i++){
                EduTeacher eduTeacher = list.get(0);
                String name = eduTeacher.getName();
                nameList.add(name);
            }
            return Result.ok().data("items",list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "讲师删除")
    @DeleteMapping("/{id}") //占位符:
    // 1、如果占位符中的参数名和形参名一致的话那么@PathVariable可以省略；
    // 2、如果配置了Swagger、并在形参前加了其他注解，那么 @PathVariable 必须加；
    public Result deleteTeacherById(
            @ApiParam(name = "id",value = "讲师Id", required = true)
            @PathVariable String id){
        try {
            teacherService.removeById(id);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }


    @ApiOperation(value = "讲师分页列表")
    @GetMapping("/{page}/{limit}")
    public Result selectTeacherByPage(
            @ApiParam(name="page",value = "当前页", required = true)
            @PathVariable(value = "page") Integer page,
            @ApiParam(name="limit",value = "每页显示记录数", required = true)
            @PathVariable Integer limit){
        try {
            Page<EduTeacher> teacherPage = new Page<>(page, limit);
            teacherService.page(teacherPage,null);
            return Result.ok().data("total",teacherPage.getTotal()).data("rows",teacherPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    //条件查询的分页
    // 1、 有分页
    // 2、条件 ： 根据讲师名称、讲师等级、创建时间、修改时间
    @ApiOperation(value = "根据讲师条件分页查询")
    @PostMapping("moreConditionPageList/{page}/{limit}")
    public Result selectTeacherByPageAndWrapper(
            @ApiParam(name="page",value = "当前页", required = true)
            @PathVariable(value = "page") Integer page,

            @ApiParam(name="limit",value = "每页显示记录数", required = true)
            @PathVariable Integer limit,

            @RequestBody TeacherQuery query){

        try {
            Page<EduTeacher> teacherPage = new Page<>(page, limit);
            //Wrapper<TeacherQuery> queryWrapper = new QueryWrapper<TeacherQuery>();
            teacherService.pageQuery(teacherPage, query);
            return Result.ok().data("total",teacherPage.getTotal()).data("rows",teacherPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "保存讲师对象")
    @PostMapping("save")
    public Result saveTeacher(@RequestBody EduTeacher teacher){
        try {
            teacherService.save(teacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "根据ID查询")
    @GetMapping("getTeacherInfo/{id}")
    public Result selectTeacherById(
            @ApiParam(name="id",value = "讲师ID", required = true)
            @PathVariable  String id){

        //当我们的业务被非法参数操作时、我们可以自定义异常（业务异常）



        try {
            EduTeacher teacher = teacherService.getById(id);
            if(teacher == null){
                throw new EduException(2222222,"没有此讲师信息");
            }
            return Result.ok().data("teacher",teacher);
        } catch (EduException e) {
            e.printStackTrace();
            return Result.error().message(e.getMessage());
        }
    }

    @ApiOperation(value = "修改讲师信息")
    @PutMapping("update")
    public Result selectTeacherById(@RequestBody EduTeacher teacher){
        try {
            teacherService.updateById(teacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message(e.getMessage());
        }
    }

}

