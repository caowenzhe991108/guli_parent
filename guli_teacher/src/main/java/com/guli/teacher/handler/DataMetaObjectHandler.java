package com.guli.teacher.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DataMetaObjectHandler implements MetaObjectHandler{
    @Override
    public void insertFill(MetaObject metaObject) {
        //自动补充teacher对象属性中的数据， isDeleted ： Boolean 所以我们应该放入true, false
        //数据库的is_deleted时tinyint类型，会自动把false变为0
        this.setFieldValByName("isDeleted",false,metaObject);

        this.setFieldValByName("gmtCreate",new Date(),metaObject);

        // 这个是insert的时候用的，插入的时候时候强制进行填充
       // this.strictInsertFill(metaObject, "gmtCreate", Date.class, new Date()); // 起始版本 3.3.0(推荐使用)

        this.setFieldValByName("gmtModified",new Date(),metaObject);


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
