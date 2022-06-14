package com.amoydays.jobs.dao;

import com.amoydays.jobs.entity.JobWarn;
import com.amoydays.jobs.vo.OrderSearch;

import java.util.List;

public interface JobWarnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobWarn record);

    int insertSelective(JobWarn record);

    JobWarn selectByPrimaryKey(Integer id);

    List<JobWarn> selectNoReadByDriverTel(OrderSearch orderSearch);

    List<JobWarn> selectByOrderSearch(OrderSearch orderSearch);

    int updateByPrimaryKeySelective(JobWarn record);

    int updateByPrimaryKey(JobWarn record);
}