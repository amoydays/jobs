package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobInRecord;
import com.amoydays.jobs.vo.OrderSearch;

import java.util.List;

public interface JobInRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobInRecord record);

    int insertSelective(JobInRecord record);

    JobInRecord selectByPrimaryKey(Integer id);

    List<JobInRecord> selectByOrderSearch(OrderSearch orderSearch);

    int updateByPrimaryKeySelective(JobInRecord record);

    int updateByPrimaryKey(JobInRecord record);
}