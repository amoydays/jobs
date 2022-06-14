package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobOrder;
import com.amoydays.jobs.vo.OrderSearch;

import java.util.List;

public interface JobOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(JobOrder record);

    int insertSelective(JobOrder record);

    JobOrder selectByPrimaryKey(String id);

    List<JobOrder> selectByOrderSearch(OrderSearch orderSearch);

    int updateByPrimaryKeySelective(JobOrder record);

    int updateByPrimaryKey(JobOrder record);
}