package com.xxl.sso.client.login;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xxl.sso.client.conf.Conf;
import com.xxl.sso.client.entity.ReturnT;
import com.xxl.sso.client.user.XxlSsoUser;
import com.xxl.sso.client.util.HttpClientUtil;
import com.xxl.sso.client.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxueli 2018-11-15 15:54:40
 */
public class SsoTokenLoginHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(SsoTokenLoginHelper.class);

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId, String logoutUrl) {
        if (!StringUtils.hasText(sessionId)) {
            return;
        }
        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("sessionId", sessionId);
        LOGGER.info("logout param sessionId is [{}] ", sessionId);
        String loginResultJson = HttpClientUtil.post(logoutUrl, loginParam, null);
        LOGGER.info("loginResultJson is [{}] ", loginResultJson);
    }
    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request, String logoutUrl) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId, logoutUrl);
    }

    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static XxlSsoUser loginCheck(String sessionId, String logoCheckUrl){
        if (!StringUtils.hasText(sessionId)) {
            return null;
        }
        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("sessionId", sessionId);
        LOGGER.info("loginCheck param sessionId is [{}] ", sessionId);
        String checkResultJson = HttpClientUtil.post(logoCheckUrl, requestParam, null);
        LOGGER.info("checkResultJson is [{}] ", checkResultJson);
        JSONObject jsonObject = JSON.parseObject(checkResultJson);
        if (jsonObject == null) {
            return null;
        }
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            LOGGER.info("登录成功，sessionid = " + sessionId);
            return JSON.parseObject(jsonObject.getString("data"), XxlSsoUser.class);
        }
        String failMsg = jsonObject.getString("msg");
        LOGGER.info("登录失败：" + failMsg);
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static XxlSsoUser loginCheck(HttpServletRequest request, String logoCheckUrl){
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId, logoCheckUrl);
    }
}
