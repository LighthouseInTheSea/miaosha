package com.misoshaproject.service;

import com.misoshaproject.bean.UserPassword;

public interface UserPasswordService {
    UserPassword saveUserPassword(UserPassword userPassword);
    void deleteUserPassword(UserPassword userPassword);
}
