package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobUpdateString;

public interface JobUpdateStringMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobUpdateString record);

    int insertSelective(JobUpdateString record);

    JobUpdateString selectByPrimaryKey(Integer id);

    JobUpdateString selectOneNoRead();

    int updateByPrimaryKeySelective(JobUpdateString record);

    int updateByPrimaryKey(JobUpdateString record);
}