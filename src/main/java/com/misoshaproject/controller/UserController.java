package com.misoshaproject.controller;

import com.misoshaproject.controller.vo.UserVo;
import com.misoshaproject.error.BusinessException;
import com.misoshaproject.error.EmBusinessError;
import com.misoshaproject.model.UserModel;
import com.misoshaproject.response.CommonReturnType;
import com.misoshaproject.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登入接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参数校验
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserModel userModel = userInfoService.validateLogin(telphone, this.EncodeByMd5(password));

        //将登入凭证加入到用户登入成功的session
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }


    //用户注册接口
    @PostMapping(value = "/register",consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号码与对应的otpcode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);

        if (!otpCode.equals(inSessionOtpCode)){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender.byteValue());
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncryptPassword(EncodeByMd5(password));

        userInfoService.register(userModel);

        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密
        String encode = base64Encoder.encode(md5.digest(str.getBytes("UTF-8")));
        return encode;
    }

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

    //用户获取otp短信接口
    @PostMapping(value = "/getotp",consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt +=10000;
        String otpCode = String.valueOf(randomInt);

        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        System.out.println("telphone="+telphone+"& otpCode = "+otpCode);

        return CommonReturnType.create(null);
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
