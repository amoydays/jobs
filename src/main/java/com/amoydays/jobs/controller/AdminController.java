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
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class AdminController {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
        put("isRead", "是否已读");
        put("content", "消息内容");
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

    @Resource
    JobInRecordMapper jobInRecordMapper;

    @Resource
    AdminUserMapper adminUserMapper;

    @RequestMapping("/goLogin")
    public String goLogin() {
        return "adminLogin";
    }

    @PostMapping("/login")
    public String login(AdminUser adminUser, HttpSession session, HttpServletRequest request) {
        AdminUser adminUserQuery = adminUserMapper.login(adminUser);
        if (adminUserQuery != null) {
            session.setAttribute("adminUserName", adminUser.getName());
            // 跳转到预登记查询controller
            return "forward:/admin/goOrderAll";
        } else {
            return "adminLogin";
        }

    }

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

    @RequestMapping("/admin/goOrderAll")
    public String goOrderAll(HttpServletRequest request) {
        setInitNoTime(request);
        OrderSearch orderSearch = new OrderSearch();
        setInitOrderSearchToday(orderSearch);
        request.setAttribute("orderSearch", orderSearch);

        return "admin/orderInfoAll";
    }

    @PostMapping("/admin/orderSearchAll")
    public String orderSearchAll(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch != null) {
            if (orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
                setInitOrderSearchToday(orderSearch);
            }
            request.setAttribute("ordersList", jobOrderMapper.selectByOrderSearch(orderSearch));
            request.setAttribute("orderSearch", orderSearch);
        }
        setInitNoTime(request);

        return "admin/orderInfoAll";
    }

    @GetMapping("/admin/getUpdateString")
    @ResponseBody
    public JobUpdateString getUpdateString() {
        JobUpdateString jobUpdateString = jobUpdateStringMapper.selectOneNoRead();
        if (jobUpdateString != null) {
            jobUpdateString.setIsRead(true);
            jobUpdateStringMapper.updateByPrimaryKey(jobUpdateString);
        }
        return jobUpdateString;
    }

    @GetMapping("/admin/goUpdateRecord")
    public String goUpdateRecord(HttpServletRequest request) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        OrderSearch orderSearch = new OrderSearch();
        String time = sdf.format(calendar.getTime());
        orderSearch.setStartDate(time);
        orderSearch.setEndDate(time + " 23:59:59");
        List<JobUpdateRecord> jobUpdateRecordList = jobUpdateRecordMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobUpdateRecordList", jobUpdateRecordList);
        request.setAttribute("orderSearch", orderSearch);

        return "admin/orderUpdateRecord";
    }

    @PostMapping("/admin/updateRecordSearch")
    public String updateRecordSearch(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch == null) {
            return goUpdateRecord(request);
        } else {
            if (orderSearch.getEndDate().indexOf(" 23:59:59") == -1) {
                orderSearch.setEndDate(orderSearch.getEndDate() + " 23:59:59");
            }
            List<JobUpdateRecord> jobUpdateRecordList = jobUpdateRecordMapper.selectByOrderSearch(orderSearch);
            request.setAttribute("jobUpdateRecordList", jobUpdateRecordList);
            return "admin/orderUpdateRecord";
        }
    }

    @GetMapping("/admin/goNoticeAdd")
    public String goNoticeAdd(HttpServletRequest request) {
        JobNotice jobNotice = jobNoticeMapper.selectTopOne();
        if (jobNotice == null) {
            jobNotice = new JobNotice();
        }
        request.setAttribute("jobNotice", jobNotice);
        return "admin/noticeAdd";
    }

    @PostMapping("/admin/noticeAdd")
    public String noticeAdd(JobNotice jobNotice, HttpServletRequest request, HttpSession session) {
        if (jobNotice != null && jobNotice.getContent() != null && !jobNotice.getContent().equals("")) {
            JobNotice ori = jobNoticeMapper.selectTopOne();
            // JobNotice只需要一条记录，Id不用记录更新
            if (ori != null) {
                ori.setId(null);
            }else {
                ori = new JobNotice();
            }
            // 增加修改记录
            addUpdateInRecord(session.getAttribute("adminUserName").toString(), ori, jobNotice);
            jobNoticeMapper.deleteAll();
            jobNoticeMapper.insert(jobNotice);
            request.setAttribute("result", "公共通知登记成功！");
        } else {
            request.setAttribute("result", "公共通知不能为空！");
        }

        return goNoticeAdd(request);
    }

    @GetMapping("/admin/goWarnAdd")
    public String goWarnAdd(String driver, String telephone, HttpServletRequest request) {
        request.setAttribute("driver", driver);
        request.setAttribute("telephone", telephone);
        return "admin/warnAdd";
    }

    @PostMapping("/admin/warnAdd")
    public String warnAdd(JobWarn jobWarn, HttpServletRequest request, HttpSession session) {
        if (jobWarn != null && jobWarn.getContent() != null && !jobWarn.getContent().equals("")) {
            jobWarn.setTime(new Date());
            jobWarn.setIsRead(false);
            // 增加修改记录
            addUpdateInRecord(session.getAttribute("adminUserName").toString(), new JobWarn(), jobWarn);
            jobWarnMapper.insert(jobWarn);
        } else {
            request.setAttribute("result", "客户提醒不能为空！");
            return "admin/warnAdd";
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

        return "admin/warnInfo";
    }

    @RequestMapping("/admin/warnSearch")
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

        return "admin/warnInfo";
    }

    @GetMapping("/admin/goWarnUpdate")
    public String goWarnUpdate(int id, HttpServletRequest request) {
        JobWarn jobWarn = jobWarnMapper.selectByPrimaryKey(id);
        request.setAttribute("jobWarn", jobWarn);

        return "admin/warnUpdate";
    }

    @PostMapping("/admin/warnUpdate")
    public String warnUpdate(JobWarn jobWarn, HttpServletRequest request, HttpSession session) {
        if (jobWarn != null && jobWarn.getContent() != null && !jobWarn.getContent().equals("")) {
            jobWarn.setTime(new Date());
            jobWarn.setIsRead(false);
            // 增加修改记录
            addUpdateInRecord(session.getAttribute("adminUserName").toString(), jobWarnMapper.selectByPrimaryKey(jobWarn.getId()), jobWarn);
            jobWarnMapper.updateByPrimaryKeySelective(jobWarn);
        } else {
            request.setAttribute("result", "客户提醒不能为空！");
            return "admin/warnUpdate";
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

        return "admin/warnInfo";
    }

    @GetMapping("/admin/warnDel")
    @ResponseBody
    public void warnDel(int id, HttpSession session) {
        // 增加修改记录
        addUpdateInRecord(session.getAttribute("adminUserName").toString(), jobWarnMapper.selectByPrimaryKey(id), new JobWarn());
        jobWarnMapper.deleteByPrimaryKey(id);
    }

    private void addUpdateInRecord(String operName, Object oriObj, Object newObj) {
        if (oriObj == null || newObj == null) {
            return;
        }
        Field[] fields = oriObj.getClass().getDeclaredFields();
        Date date = new Date();
        Object nullVal = new Object();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object oriVal = field.get(oriObj);
                Object newVal = field.get(newObj);
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
                    JobInRecord record = new JobInRecord();
                    record.setTime(date);
                    record.setOperName(operName);
                    record.setTbName(oriObj.getClass().getName().substring(oriObj.getClass().getName().lastIndexOf(".") + 1));
                    if (realName != null) {
                        record.setColumnName(realName);
                    } else {
                        record.setColumnName(name);
                    }

                    record.setOriValue(oriVal.toString());
                    record.setNowValue(newVal.toString());
                    if (oriVal == nullVal) {
                        record.setOriValue("空值");
                    }
                    if (newVal == nullVal) {
                        record.setNowValue("空值");
                    }
                    jobInRecordMapper.insert(record);

                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
