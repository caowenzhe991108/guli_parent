package com.guli.teacher.mapper;

import com.guli.teacher.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.teacher.vo.CourseWebVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author guli
 * @since 2020-10-19
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //前台页面根据课程ID查询详细信息（课程详情）
    CourseWebVo selectInfoById(String courseId);

}
