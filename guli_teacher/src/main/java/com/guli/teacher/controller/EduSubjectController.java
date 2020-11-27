package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.service.EduSubjectService;
import com.guli.teacher.vo.SubjectNestedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-15
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin   //解决跨域问题
@Api(description = "分类管理")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @PostMapping("import")
    public Result importExcelSubject(@RequestParam("file") MultipartFile file){
        //1.上传excel文件
        List<String> msg = eduSubjectService.importSubject(file);
        if(msg.size()==0){
            return Result.ok();
        }
        return Result.error().data("msgList",msg);
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("list")
    public  Result nestedList(){
        List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.nestedList();
        return  Result.ok().data("items",subjectNestedVoList);
    }

    @ApiOperation(value = "学科删除")
    @DeleteMapping("delete/{id}") //占位符:
    // 1、如果占位符中的参数名和形参名一致的话那么@PathVariable可以省略；
    // 2、如果配置了Swagger、并在形参前加了其他注解，那么 @PathVariable 必须加；
    public Result deleteTeacherById(
            @ApiParam(name = "id",value = "学科Id", required = true)
            @PathVariable String id){

        return eduSubjectService.deleteById(id);
        }

    @ApiOperation(value = "新增一级分类")
    @PostMapping("save-level-one")
    public Result saveLevelOne(
        @ApiParam(name = "subject", value = "课程分类对象", required = true)
        //@RequestBody 接收前端返回的json串的格式的对象
        @RequestBody EduSubject subject){

        boolean result = eduSubjectService.saveLevelOne(subject);
        if(result){
            return Result.ok();
        }else{
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation(value = "新增二级分类")
    @PostMapping("save-level-two")
    public Result saveLevelTwo(
        @ApiParam(name = "subject", value = "课程分类对象", required = true)
        @RequestBody EduSubject subject){
        boolean result = eduSubjectService.saveLevelTwo(subject);
        if(result){
            return Result.ok();
        }else{
            return Result.error().message("保存失败");
        }
    }
}

