package com.misoshaproject.service;

import com.misoshaproject.bean.UserInfo;
import com.misoshaproject.error.BusinessException;
import com.misoshaproject.model.UserModel;

public interface UserInfoService {

    UserInfo saveUserInfo(UserInfo userInfo);

    void deleteUserInfo(UserInfo userInfo);

    void deleteUserInfo(Long id);

    UserInfo updateUserInfo(UserInfo userInfo);

    UserModel findUser(Long id);

    void register(UserModel userModel) throws BusinessException;

    UserModel  validateLogin(String telphone,String Password) throws BusinessException;
}
