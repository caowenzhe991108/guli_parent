package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.teacher.client.VidClient;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.mapper.EduVideoMapper;
import com.guli.teacher.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.vo.VideoInfoForm;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-21
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入调用guli-video1的接口
    @Autowired
    private VidClient vidClient;

    @Override
    public boolean removeByCourseId(String courseId) {

     //先删阿里云的资源
        //获取课程里面所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id",courseId);

        //只查询video_source_id字段，别的不查出来,提高效率节省时间，这个写法要牢记！！！！！！！
        wrapperVideo.select("video_source_id");

        List<EduVideo> videos = baseMapper.selectList(wrapperVideo); //查询出来的对象还是EduVideo，但是字段只有video_source_id
        List<String> videoSourceIdList = new ArrayList<>();
        for (int i=0;i<videos.size();i++){
            //获取每个小节，提取video_source_id
            EduVideo video = videos.get(i);
            String videoSourceId = video.getVideoSourceId();
            videoSourceIdList.add(videoSourceId);
        }
        //远程调用方法
        vidClient.removeVideoList(videoSourceIdList);

    //再删数据库里的课程
        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("course_id",courseId);


        Integer count = baseMapper.delete(wrapper);
        return null!=count && count>0;
    }

    @Override
    public boolean getCountByChapterId(String id) {
        QueryWrapper<EduVideo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return null!=count && count>0;

    }

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {
        EduVideo video =new EduVideo();
        BeanUtils.copyProperties(videoInfoForm,video);
        boolean res = this.save(video);

        if(!res){
            throw  new EduException(20001,"课程添加失败");
        }

    }

    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        //从video表中取数据
        EduVideo video = this.getById(id);
        if(video == null){
            throw new EduException(20001, "数据不存在");
        }
        //创建videoInfoForm对象
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(video, videoInfoForm);
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoById(VideoInfoForm videoInfoForm) {
        //保存课时基本信息
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        boolean result = this.updateById(video);
        if(!result){
            throw new EduException(20001, "课时信息保存失败");
        }
    }

    @Override
    public boolean removeVideoById(String xiaojieId) {
        //获取视频id,需要查询数据库
        EduVideo video = baseMapper.selectById(xiaojieId);
        String videoSourceId = video.getVideoSourceId();
        //远程调用方法，根据视频ID删除阿里云资源
        if(!StringUtils.isEmpty(videoSourceId)){
            vidClient.removeVideoAliyunId(videoSourceId);
        }
        Integer result = baseMapper.deleteById(xiaojieId);
        return null != result && result > 0;
    }
}
