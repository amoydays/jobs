package com.amoydays.jobs.controller;

import com.amoydays.jobs.dao.AdminUserMapper;
import com.amoydays.jobs.entity.AdminUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class AdminUserController {
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
}
