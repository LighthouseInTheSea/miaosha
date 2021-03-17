package com.misoshaproject.controller;

import com.misoshaproject.controller.vo.UserVo;
import com.misoshaproject.error.BusinessException;
import com.misoshaproject.error.EmBusinessError;
import com.misoshaproject.model.UserModel;
import com.misoshaproject.response.CommonReturnType;
import com.misoshaproject.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel user = userInfoService.findUser(id.longValue());

        //如果获取的对应用户信息不存在
        if (user == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVo userVo = convertFromModel(user);

        //返回通用对象
        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserModel userModel){
        if (userModel==null){
            return  null;
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);

        return userVo;
    }

}
