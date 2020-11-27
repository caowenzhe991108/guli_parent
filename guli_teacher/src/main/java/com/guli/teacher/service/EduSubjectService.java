package com.guli.teacher.service;

import com.guli.common.result.Result;
import com.guli.teacher.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.vo.SubjectNestedVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-15
 */
public interface EduSubjectService extends IService<EduSubject> {

    //读取excel内容
    List<String> importSubject(MultipartFile file);

    List<SubjectNestedVo> nestedList();

    Result deleteById(String id);

    boolean saveLevelOne(EduSubject subject);

    boolean saveLevelTwo(EduSubject subject);
}
