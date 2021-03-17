package com.misoshaproject.repository;

import com.misoshaproject.bean.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPasswordRepository extends JpaRepository<UserPassword,Integer> {
    /**
     * 根据uid查找password
     * @param userId
     * @return
     */
    UserPassword findUserPasswordByUserId(Long userId);
}
