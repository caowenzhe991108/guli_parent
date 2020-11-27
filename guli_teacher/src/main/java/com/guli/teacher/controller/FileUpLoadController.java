package com.guli.teacher.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.guli.common.result.Result;
import com.guli.teacher.config.ConstantPropertiesUtil;


import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.util.UUID;

//上传文件到阿里云oss
@RestController
@RequestMapping("/eduservice/oss")
@CrossOrigin
public class FileUpLoadController {

    //上传讲师头像的方法
    @PostMapping("upload")    //spring mvc提供的MultipartFile类，获取前台上传的文件
    public Result upLoadTeacherImg(@RequestParam("file") MultipartFile file) throws IOException { //@RequestParam("file")的file要和表单中的name的属性值一样，不写默认参数名字

        String endPoint = ConstantPropertiesUtil.ENDPOINT;

        String accessKeyId = ConstantPropertiesUtil.ACCESSKEYID;

        String accessKeySecret = ConstantPropertiesUtil.ACCESSKEYSECRET;

        String bucketName = ConstantPropertiesUtil.BUCKETNAME;

        //1.获取文件上传
        //2.获取上传文件名称，获取文件上传输入
        String filename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        filename = uuid +filename;
        InputStream in = file.getInputStream();

        //3.存储到阿里云OSS里面
             // 创建OSSClient实例。
        OSS ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
             //上传文件流
        ossClient.putObject(bucketName,filename,in);
             //关闭oss
        ossClient.shutdown();
            //返回上传的路径
        String path ="http://"+bucketName+"."+endPoint+"/"+filename;
        return Result.ok().data("imgUrl",path);
    }

}
