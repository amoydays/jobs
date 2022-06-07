package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobArea;

import java.util.List;
public interface JobAreaMapper {
    int deleteByPrimaryKey(String id);

    int insert(JobArea record);

    int insertSelective(JobArea record);

    JobArea selectByPrimaryKey(String id);

    List<JobArea> findAll();

    int updateByPrimaryKeySelective(JobArea record);

    int updateByPrimaryKey(JobArea record);
}