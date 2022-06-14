package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.AdminUser;
import com.amoydays.jobs.vo.OrderSearch;

import java.util.List;

public interface AdminUserMapper {
    int deleteByPrimaryKey(String name);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser login(AdminUser adminUser);
    List<AdminUser> selectByOrderSearch(OrderSearch orderSearch);

    AdminUser selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);
}