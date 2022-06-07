package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobGoods;

import java.util.List;
public interface JobGoodsMapper {
    int deleteByPrimaryKey(String id);

    int insert(JobGoods record);

    int insertSelective(JobGoods record);

    JobGoods selectByPrimaryKey(String id);

    List<JobGoods> findAll();

    int updateByPrimaryKeySelective(JobGoods record);

    int updateByPrimaryKey(JobGoods record);
}