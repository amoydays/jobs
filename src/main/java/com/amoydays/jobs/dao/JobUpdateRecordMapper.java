package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobUpdateRecord;
import com.amoydays.jobs.vo.OrderSearch;

import java.util.List;

public interface JobUpdateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobUpdateRecord record);

    int insertSelective(JobUpdateRecord record);

    JobUpdateRecord selectByPrimaryKey(Integer id);

    List<JobUpdateRecord> selectByOrderSearch(OrderSearch orderSearch);

    int updateByPrimaryKeySelective(JobUpdateRecord record);

    int updateByPrimaryKey(JobUpdateRecord record);
}