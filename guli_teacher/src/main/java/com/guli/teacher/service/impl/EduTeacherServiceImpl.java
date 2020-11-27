package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.mapper.EduTeacherMapper;
import com.guli.teacher.service.EduCourseService;
import com.guli.teacher.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author guli
 * @since 2019-08-14
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {


    @Autowired
    EduCourseService courseService;

    @Override
    public void pageQuery(Page<EduTeacher> teacherPage, TeacherQuery query) {

        if(query == null) {
            baseMapper.selectPage(teacherPage, null);
        }

        //获取对象属性
        String name = query.getName();
        Integer level = query.getLevel();
        Date gmtCreate = query.getGmtCreate();
        Date gmtModified = query.getGmtModified();

        //创建一个Wrapper
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //判断对象属性是否存在
        if(!StringUtils.isEmpty(name)){
            //如果存在再加入条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        //判断创建时间是否存在，如果存在，查询的是大于等于此时间的
        if (!StringUtils.isEmpty(gmtCreate)) {
            wrapper.ge("gmt_create", gmtCreate);
        }

        if (!StringUtils.isEmpty(gmtModified)) {
            wrapper.le("gmt_create", gmtModified);
        }
        baseMapper.selectPage(teacherPage,wrapper);
    }

    @Override
    public Map<String, Object> getFrontTeacherListPage(Page<EduTeacher> page1) {
        //调用方法进行分页查询,通过page1就可找到分页之后的数据
        baseMapper.selectPage(page1,null);
        //从page1集合中提取数据，放大map集合中
        List<EduTeacher> records = page1.getRecords(); //每页的数据
        long total = page1.getTotal();//总记录数
        long size = page1.getSize();//每页显示的记录数
        long pages = page1.getPages();//总页数
        long current = page1.getCurrent();//当前页
        boolean hasNext = page1.hasNext();//是否有下一页
        boolean hasPrevious = page1.hasPrevious();//是否有上一页

        //然后把数据放到map集合中去，然后返回map
        Map<String,Object> map = new HashMap();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }


    //根据讲师ID查询讲师所讲的课程
    @Override
    public List<EduCourse> getCourseListByTeacherId(String id) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper();
        wrapper.like("teacher_id",id);

        List<EduCourse> list = courseService.list(wrapper);
        return list;
    }


}
