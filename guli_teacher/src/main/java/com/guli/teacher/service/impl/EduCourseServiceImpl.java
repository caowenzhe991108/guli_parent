package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.*;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.vo.CourseInfoForm;
import com.guli.teacher.vo.CourseWebVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-19
 */
@Service
@Transactional
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {


    @Autowired
    EduCourseDescriptionService courseDescriptionService;

    @Autowired
    EduVideoService videoService;

    @Autowired
    EduChapterService chapterService;

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    EduSubjectService subjectService;



    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //保存课程基本信息
        EduCourse course = new EduCourse();
        course.setStatus(EduCourse.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        //这个类是EduCourseService的实现，而EduCourseService继承了了 IService，save方法是ServiceImpl的，它实现了IService
        //写this.save也没错
        boolean res = super.save(course);
        if(!res){
            throw new EduException(20001, "课程信息保存失败");
        }

        //往课程描述表添加
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());//之前设置不让他自动生成主键，他的主键就是coursr表的主键
        boolean resultDescription = courseDescriptionService.save(courseDescription);
        if(!resultDescription){
            throw new EduException(20001, "课程详情信息保存失败");
        }

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {
        EduCourse course = this.getById(id);
        if(course == null){
            throw new EduException(20001, "数据不存在");
        }
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        EduCourseDescription courseDescription = courseDescriptionService.getById(id);
        if(course != null){
            courseInfoForm.setDescription(courseDescription.getDescription());
        }
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,course);
        boolean b = this.updateById(course);
        if(!b){
            throw new EduException(20001,"课程信息保存失败");
        }
        EduCourseDescription courseDescription =new EduCourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        boolean resultDescription = courseDescriptionService.updateById(courseDescription);
        if(!resultDescription){
            throw new EduException(20001,"课程描述信息保存失败");
        }
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper();
        wrapper.orderByDesc("gmt_create");
        if (wrapper==null){
            baseMapper.selectPage(pageParam, wrapper);
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if(!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(teacherId) ) {
            wrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.ge("subject_parent_id", subjectParentId);//陈宇提出的改进意见。原本是eq。因为有些课程还没有分类，id默认为0，故写ge
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.ge("subject_id", subjectId);
        }
        baseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public boolean deleteCourseById(String id) {
        //根据id删除所有视频
        videoService.removeByCourseId(id);
        //根据id删除所有章节
        chapterService.removeByCourseId(id);
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Override
    public Map<String, Object> getFrontCourseListPage(Page<EduCourse> pageParam) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        baseMapper.selectPage(pageParam, queryWrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    @Transactional
    public CourseWebVo selectInfoWebById(String courseId) {

        CourseWebVo courseWebVo =new CourseWebVo();

        //从edu_course拷贝课程信息到VO中
        EduCourse course = baseMapper.selectById(courseId);
        courseWebVo.setId(course.getId());
        courseWebVo.setTitle(course.getTitle());
        courseWebVo.setPrice(course.getPrice());
        courseWebVo.setLessonNum(course.getLessonNum());
        courseWebVo.setCover(course.getCover());
        courseWebVo.setBuyCount(course.getBuyCount());
        courseWebVo.setViewCount(course.getViewCount());

        courseWebVo.setSubjectLevelOneId(course.getSubjectId());
        courseWebVo.setSubjectLevelTwoId(course.getSubjectParentId());

        //从edu_subject拿出一级分类和二级分类的名字
        //二级分类
        EduSubject subject1 = subjectService.getById(course.getSubjectId());
        String title1 = subject1.getTitle();
        courseWebVo.setSubjectLevelTwo(title1);
        //一级分类
        EduSubject subject2 = subjectService.getById(course.getSubjectParentId());
        String title2 = subject2.getTitle();
        courseWebVo.setSubjectLevelOne(title2);


        //从edu_course_description拷贝课程信息到VO中
        QueryWrapper<EduCourseDescription> courseDescriptionQueryWrapper = new QueryWrapper<>();
        courseDescriptionQueryWrapper.like("id",courseId);
        EduCourseDescription courseDescription = courseDescriptionService.getOne(courseDescriptionQueryWrapper);
        courseWebVo.setDescription(courseDescription.getDescription());


        //从edu_teacher拷贝教师信息到VO中
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper() ;
        wrapper.like("id",course.getTeacherId());
        EduTeacher teacher = teacherService.getOne(wrapper);
        courseWebVo.setTeacherId(teacher.getId());
        courseWebVo.setTeacherName(teacher.getName());
        courseWebVo.setIntro(teacher.getIntro());
        courseWebVo.setAvatar(teacher.getAvatar());


        this.updatePageViewCount(courseId);
        return courseWebVo;
    }

    @Override
    public void updatePageViewCount(String id) {
        EduCourse course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course); //要在数据库中保存修改啊，这一步不能忘记啦
    }


}



