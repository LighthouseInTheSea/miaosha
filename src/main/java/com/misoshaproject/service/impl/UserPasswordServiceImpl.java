package com.misoshaproject.service.impl;

import com.misoshaproject.bean.UserInfo;
import com.misoshaproject.bean.UserPassword;
import com.misoshaproject.repository.UserPasswordRepository;
import com.misoshaproject.service.UserPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordServiceImpl implements UserPasswordService {

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Override
    public UserPassword saveUserPassword(UserPassword userPassword) {
        UserPassword saveresult = userPasswordRepository.save(userPassword);
        return saveresult;
    }

    @Override
    public void deleteUserPassword(UserPassword userPassword) {
        userPasswordRepository.delete(userPassword);
    }

}
