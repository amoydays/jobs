package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobType;

import java.util.List;
public interface JobTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(JobType record);

    int insertSelective(JobType record);

    JobType selectByPrimaryKey(String id);

    List<JobType> findAll();

    int updateByPrimaryKeySelective(JobType record);

    int updateByPrimaryKey(JobType record);
}