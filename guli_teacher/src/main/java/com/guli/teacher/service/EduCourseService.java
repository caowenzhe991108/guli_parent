package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.vo.CourseInfoForm;
import com.guli.teacher.vo.CourseWebVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-19
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);


    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    boolean deleteCourseById(String id);


    Map<String,Object> getFrontCourseListPage(Page<EduCourse> page1);

    CourseWebVo selectInfoWebById(String courseId);

    void updatePageViewCount(String id);
}
