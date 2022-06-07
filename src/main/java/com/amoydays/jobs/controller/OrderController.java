package com.amoydays.jobs.controller;

import com.amoydays.jobs.common.Utility;
import com.amoydays.jobs.dao.JobAreaMapper;
import com.amoydays.jobs.dao.JobGoodsMapper;
import com.amoydays.jobs.dao.JobOrderMapper;
import com.amoydays.jobs.dao.JobTypeMapper;
import com.amoydays.jobs.entity.JobArea;
import com.amoydays.jobs.entity.JobGoods;
import com.amoydays.jobs.entity.JobOrder;
import com.amoydays.jobs.entity.JobType;
import com.amoydays.jobs.vo.OrderSearch;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Controller
public class OrderController {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    JobAreaMapper jobAreaMapper;
    @Resource
    JobGoodsMapper jobGoodsMapper;
    @Resource
    JobTypeMapper jobTypeMapper;
    @Resource
    JobOrderMapper jobOrderMapper;

    //初始化当前日期和基础数据，保存到request
    private void setInit(HttpServletRequest request) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        // 默认预约今天晚班、明天白班、明天夜班、后天白班
        // 14点之后只能预约明天晚班、后天白班、后天夜班、大后天白班
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 14) {
            calendar.add(Calendar.DATE, 1);
        }
        String today = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String tomorrow = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String tomorrowNext = sdf.format(calendar.getTime());
        request.setAttribute("today", today);
        request.setAttribute("tomorrow", tomorrow);
        request.setAttribute("tomorrowNext", tomorrowNext);

        setInitNoTime(request);
    }

    private void setInitNoTime(HttpServletRequest request) {
        List<JobArea> jobAreaList = jobAreaMapper.findAll();
        List<JobType> jobTypeList = jobTypeMapper.findAll();
        List<JobGoods> jobGoodsList = jobGoodsMapper.findAll();
        request.setAttribute("jobAreaList", jobAreaList);
        request.setAttribute("jobTypeList", jobTypeList);
        request.setAttribute("jobGoodsList", jobGoodsList);
    }

    //默认查询当天夜班和明天白班的数据
    private void setInitOrderSearch(OrderSearch orderSearch) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        orderSearch.setStartDate(sdf.format(calendar.getTime()));
        orderSearch.setStartDuty("夜班");
        // 明天
//        calendar.add(Calendar.DATE, 1);
        orderSearch.setEndDate(sdf.format(calendar.getTime()));
        orderSearch.setEndDuty("夜班");
    }

    @GetMapping("/goOrderAdd")
    public String goOrderAdd(HttpServletRequest request) {
        setInit(request);

        JobOrder jobOrder = new JobOrder();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("telephone")) {
                    jobOrder.setTelephone(cookie.getValue());
                }
                if (cookie.getName().equals("driver")) {
                    jobOrder.setDriver(cookie.getValue());
                }
            }
        }
        request.setAttribute("jobOrder", jobOrder);

        return "orderAdd";
    }

    @PostMapping("/orderAdd")
    public String orderAdd(JobOrder jobOrder, HttpServletRequest request, HttpServletResponse response, boolean isAdd) {
        String date = jobOrder.getTime().substring(0, 10);
        String duty = jobOrder.getTime().substring(10, jobOrder.getTime().length());
        try {
            jobOrder.setDate(sdf.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        jobOrder.setDuty(duty);
        if (isAdd) {
            jobOrder.setId(Utility.getNewUUID());
            jobOrderMapper.insert(jobOrder);
            request.setAttribute("result", "预约成功！");
        } else {
            jobOrderMapper.updateByPrimaryKey(jobOrder);
            request.setAttribute("result", "更新成功！");
        }
        request.setAttribute("jobOrder", jobOrder);

        setInit(request);

        // cookie的值每次发送请求的时候会被自动带上，cookie默认是在你当前浏览器打开的过程中生效
        Cookie telephoneCookie = new Cookie("telephone", jobOrder.getTelephone());
        // 设置存储时间 （单位是 s） 【存放365天】
        telephoneCookie.setMaxAge(60 * 60 * 24 * 365);
        // cookie是存放到前台
        response.addCookie(telephoneCookie);
        Cookie driverCookie = new Cookie("driver", jobOrder.getDriver());
        driverCookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(driverCookie);

        return "orderUpdate";
    }

    @GetMapping("/goOrderInfo")
    public String goOrderInfo(HttpServletRequest request) {
        if (request.getCookies() != null) {
            OrderSearch orderSearch = new OrderSearch();
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("telephone")) {
                    orderSearch.setTelephone(cookie.getValue());
                }
            }
            request.setAttribute("orderSearch", orderSearch);
        }

        return "orderInfo";
    }

    @PostMapping("/orderSearch")
    public String orderSearch(OrderSearch orderSearch, HttpServletRequest request) {
        // 没有手机号码，不让查询
        if (orderSearch != null && orderSearch.getTelephone() != null && !orderSearch.getTelephone().equals("")) {
            if (orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
                setInitOrderSearch(orderSearch);
            }
            request.setAttribute("ordersList", jobOrderMapper.selectByOrderSearch(orderSearch));
            request.setAttribute("orderSearch", orderSearch);

            List<JobType> jobTypeList = jobTypeMapper.findAll();
            List<JobGoods> jobGoodsList = jobGoodsMapper.findAll();
            request.setAttribute("jobTypeList", jobTypeList);
            request.setAttribute("jobGoodsList", jobGoodsList);
        }

        return "orderInfo";
    }

    @GetMapping("/goOrderAll")
    public String goOrderAll(HttpServletRequest request) {
        setInitNoTime(request);
        OrderSearch orderSearch = new OrderSearch();
        setInitOrderSearch(orderSearch);
        request.setAttribute("orderSearch", orderSearch);

        return "orderInfoAll";
    }

    @PostMapping("/orderSearchAll")
    public String orderSearchAll(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch != null) {
            if (orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
                setInitOrderSearch(orderSearch);
            }
            request.setAttribute("ordersList", jobOrderMapper.selectByOrderSearch(orderSearch));
            request.setAttribute("orderSearch", orderSearch);
        }
        setInitNoTime(request);

        return "orderInfoAll";
    }
}
