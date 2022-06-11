package com.amoydays.jobs.controller;

import com.amoydays.jobs.dao.*;
import com.amoydays.jobs.entity.*;
import com.amoydays.jobs.vo.OrderSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class AdminController {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    JobAreaMapper jobAreaMapper;
    @Resource
    JobGoodsMapper jobGoodsMapper;
    @Resource
    JobTypeMapper jobTypeMapper;
    @Resource
    JobOrderMapper jobOrderMapper;

    @Resource
    JobUpdateRecordMapper jobUpdateRecordMapper;

    @Resource
    JobUpdateStringMapper jobUpdateStringMapper;

    @Resource
    JobNoticeMapper jobNoticeMapper;

    @Resource
    JobWarnMapper jobWarnMapper;

    private void setInitNoTime(HttpServletRequest request) {
        List<JobArea> jobAreaList = jobAreaMapper.findAll();
        List<JobType> jobTypeList = jobTypeMapper.findAll();
        List<JobGoods> jobGoodsList = jobGoodsMapper.findAll();
        request.setAttribute("jobAreaList", jobAreaList);
        request.setAttribute("jobTypeList", jobTypeList);
        request.setAttribute("jobGoodsList", jobGoodsList);
    }

    //默认查询当天夜班的数据
    private void setInitOrderSearchToday(OrderSearch orderSearch) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        orderSearch.setStartDate(sdf.format(calendar.getTime()));
        orderSearch.setStartDuty("夜班");
        orderSearch.setEndDate(sdf.format(calendar.getTime()));
        orderSearch.setEndDuty("夜班");
    }

    @GetMapping("/goOrderAll")
    public String goOrderAll(HttpServletRequest request) {
        setInitNoTime(request);
        OrderSearch orderSearch = new OrderSearch();
        setInitOrderSearchToday(orderSearch);
        request.setAttribute("orderSearch", orderSearch);

        return "orderInfoAll";
    }

    @PostMapping("/orderSearchAll")
    public String orderSearchAll(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch != null) {
            if (orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
                setInitOrderSearchToday(orderSearch);
            }
            request.setAttribute("ordersList", jobOrderMapper.selectByOrderSearch(orderSearch));
            request.setAttribute("orderSearch", orderSearch);
        }
        setInitNoTime(request);

        return "orderInfoAll";
    }

    @GetMapping("/getUpdateString")
    @ResponseBody
    public JobUpdateString getUpdateString() {
        JobUpdateString jobUpdateString = jobUpdateStringMapper.selectOneNoRead();
        if (jobUpdateString != null) {
            jobUpdateString.setIsRead(true);
            jobUpdateStringMapper.updateByPrimaryKey(jobUpdateString);
        }
        return jobUpdateString;
    }

    @GetMapping("/goUpdateRecord")
    public String goUpdateRecord(HttpServletRequest request) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        OrderSearch orderSearch = new OrderSearch();
        String time = sdf.format(calendar.getTime());
        orderSearch.setStartDate(time);
        if (time.indexOf(" 23:59:59") == -1) {
            orderSearch.setEndDate(time + " 23:59:59");
        }
        List<JobUpdateRecord> jobUpdateRecordList = jobUpdateRecordMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobUpdateRecordList", jobUpdateRecordList);
        request.setAttribute("orderSearch", orderSearch);

        return "orderUpdateRecord";
    }

    @PostMapping("/updateRecordSearch")
    public String updateRecordSearch(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch == null) {
            return goUpdateRecord(request);
        } else {
            if (orderSearch.getEndDate().indexOf(" 23:59:59") == -1) {
                orderSearch.setEndDate(orderSearch.getEndDate() + " 23:59:59");
            }
            List<JobUpdateRecord> jobUpdateRecordList = jobUpdateRecordMapper.selectByOrderSearch(orderSearch);
            request.setAttribute("jobUpdateRecordList", jobUpdateRecordList);
            return "orderUpdateRecord";
        }
    }

    @GetMapping("/goNoticeAdd")
    public String goNoticeAdd(HttpServletRequest request) {
        JobNotice jobNotice = jobNoticeMapper.selectTopOne();
        request.setAttribute("jobNotice", jobNotice);
        return "noticeAdd";
    }

    @PostMapping("/noticeAdd")
    public String noticeAdd(JobNotice jobNotice, HttpServletRequest request) {
        if (jobNotice != null && jobNotice.getContent() != null && !jobNotice.getContent().equals("")) {
            jobNoticeMapper.deleteAll();
            jobNoticeMapper.insert(jobNotice);
            request.setAttribute("result", "通知消息登记成功！");
        } else {
            request.setAttribute("result", "通知消息不能为空！");
        }

        return goNoticeAdd(request);
    }

    @GetMapping("/goWarnAdd")
    public String goWarnAdd() {
        return "warnAdd";
    }

    @PostMapping("/warnAdd")
    public String warnAdd(JobWarn jobWarn, HttpServletRequest request) {
        if (jobWarn != null && jobWarn.getContent() != null && !jobWarn.getContent().equals("")) {
            jobWarn.setTime(new Date());
            jobWarn.setIsRead(false);
            jobWarnMapper.insert(jobWarn);
        } else {
            request.setAttribute("result", "警示信息不能为空！");
            return "warnAdd";
        }

        // 当前时间
        Calendar calendar = Calendar.getInstance();
        OrderSearch orderSearch = new OrderSearch();
        String time = sdf.format(calendar.getTime());
        orderSearch.setStartDate(time);
        if (time.indexOf(" 23:59:59") == -1) {
            orderSearch.setEndDate(time + " 23:59:59");
        }
        List<JobWarn> jobWarnList = jobWarnMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobWarnList", jobWarnList);
        request.setAttribute("orderSearch", orderSearch);

        return "warnInfo";
    }

    @RequestMapping("/warnSearch")
    public String warnSearch(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch == null || orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
            // 当前时间
            Calendar calendar = Calendar.getInstance();
            String time = sdf.format(calendar.getTime());
            orderSearch.setStartDate(time);
            if (time.indexOf(" 23:59:59") == -1) {
                orderSearch.setEndDate(time + " 23:59:59");
            }
        }
        List<JobWarn> jobWarnList = jobWarnMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobWarnList", jobWarnList);
        request.setAttribute("orderSearch", orderSearch);

        return "warnInfo";
    }

    @GetMapping("/goWarnUpdate")
    public String goWarnUpdate(int id, HttpServletRequest request) {
        JobWarn jobWarn = jobWarnMapper.selectByPrimaryKey(id);
        request.setAttribute("jobWarn", jobWarn);

        return "warnUpdate";
    }

    @PostMapping("/warnUpdate")
    public String warnUpdate(JobWarn jobWarn, HttpServletRequest request) {
        if (jobWarn != null && jobWarn.getContent() != null && !jobWarn.getContent().equals("")) {
            jobWarn.setTime(new Date());
            jobWarn.setIsRead(false);
            jobWarnMapper.updateByPrimaryKeySelective(jobWarn);
        } else {
            request.setAttribute("result", "警示信息不能为空！");
            return "warnUpdate";
        }

        // 当前时间
        Calendar calendar = Calendar.getInstance();
        OrderSearch orderSearch = new OrderSearch();
        String time = sdf.format(calendar.getTime());
        orderSearch.setStartDate(time);
        if (time.indexOf(" 23:59:59") == -1) {
            orderSearch.setEndDate(time + " 23:59:59");
        }
        List<JobWarn> jobWarnList = jobWarnMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobWarnList", jobWarnList);
        request.setAttribute("orderSearch", orderSearch);

        return "warnInfo";
    }

    @GetMapping("/warnDel")
    @ResponseBody
    public void warnDel(int id) {
        jobWarnMapper.deleteByPrimaryKey(id);
    }

}
