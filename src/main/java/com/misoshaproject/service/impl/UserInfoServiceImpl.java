package com.misoshaproject.service.impl;

import com.misoshaproject.bean.UserInfo;
import com.misoshaproject.bean.UserPassword;
import com.misoshaproject.error.BusinessException;
import com.misoshaproject.error.EmBusinessError;
import com.misoshaproject.model.UserModel;
import com.misoshaproject.repository.UserInfoRepository;
import com.misoshaproject.repository.UserPasswordRepository;
import com.misoshaproject.service.UserInfoService;
import com.misoshaproject.validator.ValidationResult;
import com.misoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Autowired
    private ValidatorImpl validator;

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

    @Override
    public UserModel validateLogin(String telphone, String Password) throws BusinessException {
        UserInfo byTelphone = userInfoRepository.findByTelphone(telphone);
        if (byTelphone == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPassword userPasswordByUserId = userPasswordRepository.findUserPasswordByUserId(byTelphone.getId());
        UserModel userModel = convertFromDataObject(byTelphone,userPasswordByUserId);

        //比对是否一致
        if (!StringUtils.equals(Password,userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException{
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //if (StringUtils.isEmpty(userModel.getName())
        //        ||userModel.getGender() == null
        //        ||userModel.getAge() ==null
        //        ||StringUtils.isEmpty(userModel.getTelphone())){
        //    throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //}

        ValidationResult validate = validator.validate(userModel);
        if (validate.isHasError()){
            throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validate.getErrorMsg());
        }


        //实现model - >dataobject方法
        UserInfo userInfo = convertFromModel(userModel);
        try {
            userInfoRepository.save(userInfo);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userInfo.getId().intValue());

        UserPassword userPassword = convertPasswordFromModel(userModel);
        userPasswordRepository.save(userPassword);

        return;
    }

    private UserPassword convertPasswordFromModel(UserModel userModel) {
        if (userModel == null) {
            return  null;
        }

        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncryptPassword());
        userPassword.setUserId(Long.parseLong(userModel.getId()+""));
        return userPassword;
    }

    private UserInfo convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel,userInfo);

        return  userInfo;
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
