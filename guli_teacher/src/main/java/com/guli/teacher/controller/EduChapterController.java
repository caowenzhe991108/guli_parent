package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.service.EduChapterService;
import com.guli.teacher.vo.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-21
 */

@Api(description="课程章节管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    EduChapterService chapterService;

    @ApiOperation(value = "嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public Result nestedListByCourseId(@ApiParam(name = "courseId", value = "课程ID", required = true)
                                        @PathVariable String courseId){

        List<ChapterVo> voList = chapterService.nestedList(courseId);
        System.out.print("正在查找课程章节");
        return  Result.ok().data("items",voList);
    }

    @ApiOperation(value = "新增章节")
    @PostMapping
    public  Result save(
            @ApiParam(name = "chapter",value = "章节对象")
            @RequestBody EduChapter chapter){

        chapterService.save(chapter);
        return  Result.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("{id}")
    public  Result getById(
            @ApiParam(name = "id",value = "章节ID",required = true)
            @PathVariable String id){

        EduChapter chapter = chapterService.getById(id);
        return  Result.ok().data("item",chapter);
    }

    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("{id}")
    public  Result updateById(
            @ApiParam(name = "id",value = "章节ID",required = true)
            @PathVariable String id,
            @ApiParam(name = "chapter",value = "章节对象",required = true)
            @RequestBody EduChapter chapter){

        chapter.setId(id);
        chapterService.updateById(chapter);
        return  Result.ok();
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("{id}")
    public Result removeById(
            @ApiParam(name = "id",value = "章节ID",required = true)
            @PathVariable String id){

        boolean res = chapterService.removeChapterById(id);
        if(res){
            return Result.ok().message("删除成功");
        }
        return Result.error().message("删除失败");

    }


}

