package com.guli.ucenter.controller;


import com.guli.common.result.Result;
import com.guli.ucenter.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-11-19
 */
@RestController
@RequestMapping("/ucenterservice/ucenter")
@CrossOrigin
public class MemberController {

    @Autowired
    MemberService memberService;
    //统计某一天的注册人数
    @GetMapping("countRegistNum/{day}")
    public Result countRegistNum(@PathVariable String day ){
        Integer  res = memberService.countRegistNum(day);
        return Result.ok().data("registerCount",res);
    }

}

