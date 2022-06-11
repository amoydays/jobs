package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.AdminUser;

public interface AdminUserMapper {
    int deleteByPrimaryKey(String name);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser login(AdminUser adminUser);

    AdminUser selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);
}