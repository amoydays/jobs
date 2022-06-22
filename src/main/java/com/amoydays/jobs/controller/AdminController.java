package com.amoydays.jobs.controller;

import com.amoydays.jobs.common.ExcelUtil;
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
import javax.servlet.http.HttpServletResponse;
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

    private final List<String> columnList = new ArrayList<>() {{
        add("序号");
        add("日期");
        add("类型");
        add("单位");
        add("货名");
        add("船名航次");
        add("件数");
        add("重量");
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

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping("/goLogin")
    public String goLogin() {
        return "adminLogin";
    }

    /**
     * 管理员登录
     *
     * @param adminUser
     * @param session
     * @param request
     * @return
     */
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

    /**
     * 初始化数据（不带日期）
     *
     * @param request
     */
    private void setInitNoTime(HttpServletRequest request) {
        List<JobArea> jobAreaList = jobAreaMapper.findAll();
        List<JobType> jobTypeList = jobTypeMapper.findAll();
        List<JobGoods> jobGoodsList = jobGoodsMapper.findAll();
        request.setAttribute("jobAreaList", jobAreaList);
        request.setAttribute("jobTypeList", jobTypeList);
        request.setAttribute("jobGoodsList", jobGoodsList);
    }

    /**
     * 初始化OrderSearch（当天日期）
     *
     * @param orderSearch
     */
    //默认查询当天夜班的数据
    private void setInitOrderSearchToday(OrderSearch orderSearch) {
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        orderSearch.setStartDate(sdf.format(calendar.getTime()));
        orderSearch.setStartDuty("夜班");
        orderSearch.setEndDate(sdf.format(calendar.getTime()));
        orderSearch.setEndDuty("夜班");
    }

    /**
     * 进入内部查询页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/admin/goOrderAll")
    public String goOrderAll(HttpServletRequest request) {
        setInitNoTime(request);
        OrderSearch orderSearch = new OrderSearch();
        setInitOrderSearchToday(orderSearch);
        request.setAttribute("orderSearch", orderSearch);

        return "admin/orderInfoAll";
    }

    /**
     * 根据OrderSearch条件查询
     *
     * @param orderSearch
     * @param request
     * @return
     */
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

    /**
     * 根据OrderSearch条件下载Excel
     *
     * @param orderSearch
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/admin/download")
    @ResponseBody
    public void download(OrderSearch orderSearch, HttpServletRequest request, HttpServletResponse response) {
        if (orderSearch == null) {
            orderSearch = new OrderSearch();
        }
        if (orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("")) {
            setInitOrderSearchToday(orderSearch);
        }
        List<JobOrder> jobOrderList = jobOrderMapper.selectByOrderSearch(orderSearch);
        Calendar calendar = Calendar.getInstance();
        String fileName = sdf.format(calendar.getTime()).concat("order.xlsx");
        List<List<Object>> dataList = new ArrayList<>();
        for (JobOrder order : jobOrderList) {
            List<Object> entityList = new ArrayList<>();
            entityList.add(order.getTime());
            entityList.add(order.getTypeName());
            entityList.add(order.getDriver());
            entityList.add(order.getGoodsName());
            entityList.add(order.getVesselVoyage());
            if (order.getGoodsNum() == null) {
                entityList.add(0);
            } else {
                entityList.add(order.getGoodsNum());
            }
            if (order.getWeight() == null) {
                entityList.add(0);
            } else {
                entityList.add(order.getWeight());
            }
            dataList.add(entityList);
        }
        ExcelUtil.uploadExcelAboutUser(response, fileName, columnList, dataList);
    }

    /**
     * 获取UpdateString更新字符串
     *
     * @return
     */
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

    /**
     * 获取UpdateRecord更新记录
     *
     * @param request
     * @return
     */
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

    /**
     * UpdateRecord更新记录查询
     *
     * @param orderSearch
     * @param request
     * @return
     */
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

    /**
     * 进入公共通知页面
     *
     * @param request
     * @return
     */
    @GetMapping("/admin/goNoticeAdd")
    public String goNoticeAdd(HttpServletRequest request) {
        JobNotice jobNotice = jobNoticeMapper.selectTopOne();
        if (jobNotice == null) {
            jobNotice = new JobNotice();
        }
        request.setAttribute("jobNotice", jobNotice);
        return "admin/noticeAdd";
    }

    /**
     * 新增公共通知，JobNotice只需要一条记录
     *
     * @param jobNotice
     * @param request
     * @param session
     * @return
     */
    @PostMapping("/admin/noticeAdd")
    public String noticeAdd(JobNotice jobNotice, HttpServletRequest request, HttpSession session) {
        if (jobNotice != null && jobNotice.getContent() != null && !jobNotice.getContent().equals("")) {
            JobNotice ori = jobNoticeMapper.selectTopOne();
            // JobNotice只需要一条记录，Id不用记录更新
            if (ori != null) {
                ori.setId(null);
            } else {
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

    /**
     * 进入客户提醒页面
     *
     * @param driver
     * @param telephone
     * @param request
     * @return
     */
    @GetMapping("/admin/goWarnAdd")
    public String goWarnAdd(String driver, String telephone, HttpServletRequest request) {
        request.setAttribute("driver", driver);
        request.setAttribute("telephone", telephone);
        return "admin/warnAdd";
    }

    /**
     * 新增客户提醒
     *
     * @param jobWarn
     * @param request
     * @param session
     * @return
     */
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

    /**
     * 根据OrderSearch查询客户提醒
     *
     * @param orderSearch
     * @param request
     * @return
     */
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

    /**
     * 进入客户提醒修改页面
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/admin/goWarnUpdate")
    public String goWarnUpdate(int id, HttpServletRequest request) {
        JobWarn jobWarn = jobWarnMapper.selectByPrimaryKey(id);
        request.setAttribute("jobWarn", jobWarn);

        return "admin/warnUpdate";
    }

    /**
     * 客户提醒修改页面
     *
     * @param jobWarn
     * @param request
     * @param session
     * @return
     */
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

    /**
     * 客户提醒删除
     *
     * @param id
     * @param session
     */
    @GetMapping("/admin/warnDel")
    @ResponseBody
    public void warnDel(int id, HttpSession session) {
        // 增加修改记录
        addUpdateInRecord(session.getAttribute("adminUserName").toString(), jobWarnMapper.selectByPrimaryKey(id), new JobWarn());
        jobWarnMapper.deleteByPrimaryKey(id);
    }

    /**
     * 增加公共通知和客户提醒修改记录
     *
     * @param operName
     * @param oriObj
     * @param newObj
     */
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
