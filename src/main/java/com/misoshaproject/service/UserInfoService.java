package com.misoshaproject.service;

import com.misoshaproject.bean.UserInfo;
import com.misoshaproject.model.UserModel;

public interface UserInfoService {

    UserInfo saveUserInfo(UserInfo userInfo);

    void deleteUserInfo(UserInfo userInfo);

    void deleteUserInfo(Long id);

    UserInfo updateUserInfo(UserInfo userInfo);

    UserModel findUser(Long id);
}
