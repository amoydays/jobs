package com.amoydays.jobs.controller;

import com.amoydays.jobs.common.Utility;
import com.amoydays.jobs.dao.*;
import com.amoydays.jobs.entity.*;
import com.amoydays.jobs.vo.OrderSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class OrderController {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfDT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private final Map<String, String> columnNames = new HashMap<String, String>() {{
        put("driver", "作业单位");
        put("telephone", "手机");
        put("goodsNum", "作业件数");
        put("weight", "作业重量");
        put("typeId", "作业类型");
        put("goodsId", "作业货名");
        put("areaId", "作业区域");
        put("vesselVoyage", "船名航次");
        put("time", "作业时间");
        put("date", "作业日期");
        put("duty", "作业班别");
    }};
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

    //默认查询当天夜班至后天白班的数据
    private void setInitOrderSearch(OrderSearch orderSearch) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        orderSearch.setStartDate(sdf.format(calendar.getTime()));
        orderSearch.setStartDuty("夜班");
        // 明天
        calendar.add(Calendar.DATE, 1);
        // 后天
        calendar.add(Calendar.DATE, 1);
        orderSearch.setEndDate(sdf.format(calendar.getTime()));
        orderSearch.setEndDuty("白班");
    }

    // 动态增加修改记录
    private void addUpdateRecord(JobOrder jobOrder, Object oriObj, Object newObj) {
        Field[] fields = oriObj.getClass().getDeclaredFields();
        Date date = new Date();
        StringBuffer resultSB = new StringBuffer();
        resultSB.append(sdfDT.format(date));
        resultSB.append("，");
        resultSB.append(jobOrder.getDriver());
        resultSB.append("，");
        resultSB.append(jobOrder.getTelephone());
        resultSB.append("，修改记录为 ");
        boolean isUpdate = false;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object oriVal = field.get(oriObj);
                Object newVal = field.get(newObj);
                Object nullVal = new Object();
                if (oriVal == null) {
                    oriVal = nullVal;
                }
                if (newVal == null) {
                    newVal = nullVal;
                }
                if (!oriVal.equals(newVal)) {
                    String name = field.getName().toString();
                    // date和duty的修改记录已包含在time字段里面，无需记录
                    if (name.equals("date") || name.equals("duty")) {
                        continue;
                    }
                    String realName = columnNames.get(name);
                    JobUpdateRecord jobUpdateRecord = new JobUpdateRecord();
                    jobUpdateRecord.setTime(date);
                    jobUpdateRecord.setDriver(jobOrder.getDriver());
                    jobUpdateRecord.setTelephone(jobOrder.getTelephone());
                    if (realName != null) {
                        jobUpdateRecord.setColumnName(realName);
                    } else {
                        jobUpdateRecord.setColumnName(name);
                    }
                    if (field.getName().equals("typeId")) {
                        List<JobType> jobTypeList = jobTypeMapper.findAll();
                        Map<String, String> jobTypeMap = new HashMap<String, String>();
                        for (JobType jobType : jobTypeList) {
                            jobTypeMap.put(jobType.getId(), jobType.getName());
                        }
                        jobUpdateRecord.setOriValue(jobTypeMap.get(oriVal));
                        jobUpdateRecord.setNowValue(jobTypeMap.get(newVal));
                    } else if (field.getName().equals("goodsId")) {
                        List<JobGoods> jobGoodsList = jobGoodsMapper.findAll();
                        Map<String, String> jobGoodsMap = new HashMap<String, String>();
                        for (JobGoods jobGoods : jobGoodsList) {
                            jobGoodsMap.put(jobGoods.getId(), jobGoods.getName());
                        }
                        jobUpdateRecord.setOriValue(jobGoodsMap.get(oriVal));
                        jobUpdateRecord.setNowValue(jobGoodsMap.get(newVal));
                    } else if (field.getName().equals("areaId")) {
                        List<JobArea> jobAreaList = jobAreaMapper.findAll();
                        Map<String, String> jobAreaMap = new HashMap<String, String>();
                        for (JobArea jobArea : jobAreaList) {
                            jobAreaMap.put(jobArea.getId(), jobArea.getName());
                        }
                        jobUpdateRecord.setOriValue(jobAreaMap.get(oriVal));
                        jobUpdateRecord.setNowValue(jobAreaMap.get(newVal));
                    } else {
                        jobUpdateRecord.setOriValue(oriVal.toString());
                        jobUpdateRecord.setNowValue(newVal.toString());
                        if (oriVal == nullVal) {
                            jobUpdateRecord.setOriValue("空值");
                        }
                        if (newVal == nullVal) {
                            jobUpdateRecord.setNowValue("空值");
                        }
                    }
                    jobUpdateRecordMapper.insert(jobUpdateRecord);

                    resultSB.append(jobUpdateRecord.getColumnName());
                    resultSB.append("，原值：");
                    resultSB.append(jobUpdateRecord.getOriValue());
                    resultSB.append("，新值：");
                    resultSB.append(jobUpdateRecord.getNowValue());
                    resultSB.append("；");

                    isUpdate = true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (isUpdate) {
            JobUpdateString jobUpdateString = new JobUpdateString();
            jobUpdateString.setContent(resultSB.toString());
            jobUpdateString.setIsRead(false);
            jobUpdateStringMapper.insert(jobUpdateString);
        }

    }

    @GetMapping("/goOrderAdd")
    public String goOrderAdd(HttpServletRequest request, HttpServletResponse response) {
        setInit(request);

        JobOrder jobOrder = new JobOrder();
        // 客户端通知消息Id
        String noticeId = null;
        // 客户端通知消息是否显示，0不显示，1显示
        String noticeIsShow = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("telephone")) {
                    jobOrder.setTelephone(cookie.getValue());
                }
                if (cookie.getName().equals("driver")) {
                    jobOrder.setDriver(cookie.getValue());
                }
                if (cookie.getName().equals("noticeId")) {
                    noticeId = cookie.getValue();
                }
                if (cookie.getName().equals("noticeIsShow")) {
                    noticeIsShow = cookie.getValue();
                }
            }
        }
        request.setAttribute("jobOrder", jobOrder);

        // 通知消息
        JobNotice jobNotice = jobNoticeMapper.selectTopOne();
        if (jobNotice != null && jobNotice.getContent() != null && !jobNotice.getContent().equals("")) {
            // 客户端通知消息Id为空、消息是否显示为空或者1、客户端消息Id和服务器Id不一致时，客户端显示通知消息
            if (noticeId == null || noticeId.equals("") || noticeIsShow == null || noticeIsShow.equals("") || noticeIsShow.equals("1") || !noticeId.equals(jobNotice.getId().toString())) {
                request.setAttribute("noticeIsShow", "1");
                Cookie noticeIdCookie = new Cookie("noticeId", jobNotice.getId().toString());
                noticeIdCookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(noticeIdCookie);
                // noticeIsShow默认为1，需要显示
                Cookie noticeIsShowCookie = new Cookie("noticeIsShow", "1");
                noticeIsShowCookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(noticeIsShowCookie);
            }
        } else {
            jobNotice = new JobNotice();
        }
        request.setAttribute("jobNotice", jobNotice);

        // 警示消息
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setDriver(jobOrder.getDriver());
        orderSearch.setTelephone(jobOrder.getTelephone());
        JobWarn jobWarn = jobWarnMapper.selectOneNoReadByDriverTel(orderSearch);
        if (jobWarn == null) {
            jobWarn = new JobWarn();
        }
        request.setAttribute("jobWarn", jobWarn);

        return "orderAdd";
    }

    @GetMapping("/warnNoShow")
    @ResponseBody
    public void warnNoShow(JobWarn jobWarn) {
        if (jobWarn != null && jobWarn.getId() != null) {
            jobWarn.setIsRead(true);
            jobWarnMapper.updateByPrimaryKeySelective(jobWarn);
        }
    }

    @GetMapping("/noticeNoShow")
    @ResponseBody
    public void noticeNoShow(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("noticeIsShow")) {
                // noticeIsShow设置为0，不需要显示
                request.setAttribute("noticeIsShow", "0");
                Cookie noticeIsShowCookie = new Cookie("noticeIsShow", "0");
                noticeIsShowCookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(noticeIsShowCookie);
            }
        }
    }

    @PostMapping("/orderAdd")
    public String orderAdd(JobOrder jobOrder, HttpServletRequest request, HttpServletResponse response, boolean isAdd) {
        String date = jobOrder.getTime().substring(0, 10);
        String duty = jobOrder.getTime().substring(10);
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
            addUpdateRecord(jobOrder, jobOrderMapper.selectByPrimaryKey(jobOrder.getId()), jobOrder);
            jobOrderMapper.updateByPrimaryKey(jobOrder);
            request.setAttribute("result", "更新成功！");
        }

        // cookie的值每次发送请求的时候会被自动带上，cookie默认是在你当前浏览器打开的过程中生效
        Cookie telephoneCookie = new Cookie("telephone", jobOrder.getTelephone());
        // 设置存储时间 （单位是 s） 【存放365天】
        telephoneCookie.setMaxAge(60 * 60 * 24 * 365);
        // cookie是存放到前台
        response.addCookie(telephoneCookie);
        Cookie driverCookie = new Cookie("driver", jobOrder.getDriver());
        driverCookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(driverCookie);

        // 直接进入查询页面
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setTelephone(jobOrder.getTelephone());
        return orderSearch(orderSearch, request);
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

    @GetMapping("goUpdate")
    public String goUpdate(JobOrder jobOrder, HttpServletRequest request) {
        if (jobOrder != null) {
            setInit(request);
            request.setAttribute("jobOrderUpdate", jobOrderMapper.selectByPrimaryKey(jobOrder.getId()));

            return "orderUpdate";
        } else {
            return goOrderInfo(request);
        }
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

            if (orderSearch.getTelephone() != null && !orderSearch.getTelephone().equals("")) {
                return orderSearch(orderSearch, request);
            }
        }

        return "orderInfo";
    }

}
