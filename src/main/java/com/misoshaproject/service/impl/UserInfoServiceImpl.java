package com.misoshaproject.service.impl;

import com.misoshaproject.bean.UserInfo;
import com.misoshaproject.bean.UserPassword;
import com.misoshaproject.model.UserModel;
import com.misoshaproject.repository.UserInfoRepository;
import com.misoshaproject.repository.UserPasswordRepository;
import com.misoshaproject.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Override
    public UserInfo saveUserInfo(UserInfo userInfo) {
        UserInfo saveresult = userInfoRepository.save(userInfo);
        return saveresult;
    }

    @Override
    public void deleteUserInfo(UserInfo userInfo) {
        userInfoRepository.delete(userInfo);
    }

    @Override
    public void deleteUserInfo(Long id) {
        userInfoRepository.deleteById(id);
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserModel findUser(Long id) {
        Optional<UserInfo> byId = userInfoRepository.findById(id);
        if (!byId.isPresent()){
            return null;
        }

        //根据用户id获取对应的用户加密密码信息
        UserPassword userPasswordByUserId = userPasswordRepository.findUserPasswordByUserId(byId.get().getId());

        return convertFromDataObject(byId.get(),userPasswordByUserId);
    }

    /**
     * 转换成UserModel
     * @param userInfo
     * @param userPassword
     * @return
     */
    private UserModel convertFromDataObject(UserInfo userInfo, UserPassword userPassword){
        if (userInfo == null){
            return  null;
        }
        UserModel userModel  = new UserModel();
        BeanUtils.copyProperties(userInfo,userModel);

        if (userPassword!=null){
            userModel.setEncryptPassword(userPassword.getEncrptPassword());
        }
        return  userModel;
    }
}
