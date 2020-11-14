package com.xxl.sso.sample.controller;

import com.xxl.sso.client.conf.Conf;
import com.xxl.sso.client.entity.ReturnT;
import com.xxl.sso.client.user.XxlSsoUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxueli 2017-08-01 21:39:47
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public ReturnT<XxlSsoUser> index(HttpServletRequest request) {
        XxlSsoUser xxlUser = (XxlSsoUser) request.getAttribute(Conf.SSO_USER);
        return new ReturnT<>(xxlUser);
    }

}