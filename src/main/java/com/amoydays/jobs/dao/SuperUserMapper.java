package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.SuperUser;

public interface SuperUserMapper {
    int deleteByPrimaryKey(String name);

    int insert(SuperUser record);

    int insertSelective(SuperUser record);

    SuperUser selectByPrimaryKey(String name);

    SuperUser login(SuperUser superUser);

    int updateByPrimaryKeySelective(SuperUser record);

    int updateByPrimaryKey(SuperUser record);
}