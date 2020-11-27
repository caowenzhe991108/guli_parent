package com.guli.teacher.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;

//在服务器启动的时候，让这个类读取配置文件内容

@Component
public class ConstantPropertiesUtil implements InitializingBean{

    @Value("${aliyun.oss.file.endPoint}")
    private String endPoint ;

    @Value("${aliyun.oss.file.accessKeyId}")
    private String accessKeyId ;

    @Value("${aliyun.oss.file.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.file.bucketName}")
    private String bucketName ;

    //定义常量，方便调用
    public static String ENDPOINT;
    public static String ACCESSKEYID;
    public static String ACCESSKEYSECRET;
    public static String BUCKETNAME;

    //服务已启动就会调用afterPropertiesSet()方法
    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endPoint;
        ACCESSKEYID = accessKeyId;
        ACCESSKEYSECRET=accessKeySecret;
        BUCKETNAME=bucketName;
    }
}
