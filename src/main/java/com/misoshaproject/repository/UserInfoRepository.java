package com.misoshaproject.repository;

import com.misoshaproject.bean.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findByTelphone(String telphone);
}
