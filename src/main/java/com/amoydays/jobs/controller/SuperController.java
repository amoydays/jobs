package com.amoydays.jobs.controller;

import com.amoydays.jobs.dao.AdminUserMapper;
import com.amoydays.jobs.dao.JobInRecordMapper;
import com.amoydays.jobs.dao.SuperUserMapper;
import com.amoydays.jobs.entity.AdminUser;
import com.amoydays.jobs.entity.JobInRecord;
import com.amoydays.jobs.entity.SuperUser;
import com.amoydays.jobs.vo.OrderSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Controller
public class SuperController {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    SuperUserMapper superUserMapper;

    @Resource
    JobInRecordMapper jobInRecordMapper;

    @Resource
    AdminUserMapper adminUserMapper;

    @RequestMapping("/goSuperLogin")
    public String goSuperLogin() {
        return "superLogin";
    }

    @PostMapping("/superLogin")
    public String superLogin(HttpSession session, HttpServletRequest request, SuperUser superUser) {
        SuperUser superUserQuery = superUserMapper.login(superUser);
        if (superUserQuery != null) {
            session.setAttribute("superUserName", superUserQuery.getName());
            // 跳转到内部修改记录查询
            return inRecordSearch(null, request);
        } else {
            return "superLogin";
        }
    }

    @RequestMapping("/super/inRecordSearch")
    public String inRecordSearch(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch == null || orderSearch.getStartDate() == null || orderSearch.getStartDate().equals("") || orderSearch.getEndDate() == null || orderSearch.getEndDate().equals("")) {
            orderSearch = new OrderSearch();
            // 当前时间
            Calendar calendar = Calendar.getInstance();
            String time = sdf.format(calendar.getTime());
            orderSearch.setStartDate(time);
            orderSearch.setEndDate(time + " 23:59:59");
        } else {
            if (orderSearch.getEndDate().indexOf(" 23:59:59") == -1) {
                orderSearch.setEndDate(orderSearch.getEndDate() + " 23:59:59");
            }
        }
        List<JobInRecord> jobInRecordList = jobInRecordMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("jobInRecordList", jobInRecordList);
        request.setAttribute("orderSearchNew", orderSearch);

        return "super/inRecord";
    }

    @RequestMapping("/super/adminUserSearch")
    public String adminUserSearch(OrderSearch orderSearch, HttpServletRequest request) {
        if (orderSearch == null || orderSearch.getName() == null || orderSearch.getName().equals("")) {
            orderSearch = new OrderSearch();
        }
        List<AdminUser> adminUserList = adminUserMapper.selectByOrderSearch(orderSearch);
        request.setAttribute("adminUserList", adminUserList);
        request.setAttribute("orderSearchNew", orderSearch);

        return "super/adminUserInfo";
    }

    @RequestMapping("/super/goAdminUserAdd")
    public String goAdminUserAdd(){
        return "super/adminUserAdd";
    }

    @PostMapping("/super/adminUserAdd")
    public String adminUserAdd(AdminUser adminUser,HttpServletRequest request){
        adminUserMapper.insert(adminUser);

        return adminUserSearch(null,request);
    }

    @RequestMapping("/super/goAdminUserUpdate")
    public String goAdminUserUpdate(AdminUser adminUser,HttpServletRequest request){
        request.setAttribute("adminUser",adminUser);

        return "super/adminUserUpdate";
    }

    @PostMapping("/super/adminUserUpdate")
    public String adminUserUpdate(AdminUser adminUser,HttpServletRequest request){
        adminUserMapper.updateByPrimaryKeySelective(adminUser);

        return adminUserSearch(null,request);
    }

    @RequestMapping("super/adminUserDel")
    @ResponseBody
    public void adminUserDel(String name){
        adminUserMapper.deleteByPrimaryKey(name);
    }
}
