package com.guli.guli_video.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VidService {
    String uploadVideAliyun(MultipartFile file);

    void removeVideo(String videoId);

    //删除多个阿里云资源视频
    void removeVideoList(List videoIdList);
}
