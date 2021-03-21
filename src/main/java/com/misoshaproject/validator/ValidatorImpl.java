package com.misoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        ValidationResult validationResult = new ValidationResult();
        Set<ConstraintViolation<Object>> validate = validator.validate(bean);
        if (validate.size()>0){
            //有错误
            validationResult.setHasError(true);
            validate.forEach(constraintViolation->{
                String message = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,message);
            });
        }
        return validationResult;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
