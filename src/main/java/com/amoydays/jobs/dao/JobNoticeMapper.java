package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobNotice;

public interface JobNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteAll();

    int insert(JobNotice record);

    int insertSelective(JobNotice record);

    JobNotice selectByPrimaryKey(Integer id);

    JobNotice selectTopOne();

    int updateByPrimaryKeySelective(JobNotice record);

    int updateByPrimaryKey(JobNotice record);
}