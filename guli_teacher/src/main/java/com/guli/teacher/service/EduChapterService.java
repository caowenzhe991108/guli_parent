package com.guli.teacher.service;

import com.guli.teacher.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-21
 */
public interface EduChapterService extends IService<EduChapter> {
    boolean removeByCourseId(String courseId);

    List<ChapterVo> nestedList(String courseId);

    boolean removeChapterById(String id);
}
