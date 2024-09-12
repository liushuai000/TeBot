package com.niu.tebot.controller;


import com.niu.tebot.base.JsonResult;
import com.niu.tebot.base.user;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/acl/index")
public class vueController {
//    @ApiOperation("获取所有Sar图像信息")
    @PostMapping("/login")
    public JsonResult findImageDetail(user user) {

        return new JsonResult();
    }
}
