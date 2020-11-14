package com.xxl.sso.client.conf;

import com.xxl.sso.client.entity.ReturnT;

/**
 * conf
 *
 * @author xuxueli 2018-04-02 19:18:04
 */
public class Conf {

    /**
     * sso sessionid, between browser and sso-server (web + token client)
     */
    public static final String SSO_SESSIONID = "xxl_sso_sessionid";

    public static final String SSO_USER = "xxl_sso_user";

    /**
     * sso server address (web + token client)
     */
    public static final String SSO_SERVER = "sso_server";

    /**
     * logout path, client relatice path
     */
    public static final String SSO_LOGOUT_PATH = "SSO_LOGOUT_PATH";

    /**
     * logoCheck url, server relative path (web client)
     */
    public static final String SSO_LOGIN_CHECK_PATH = "SSO_LOGIN_CHECK";

    public static final String SSO_LOGIN_PATH = "SSO_LOGIN";

    /**
     * excluded paths, client relatice path, include path can be set by "filter-mapping"
     */
    public static final String SSO_EXCLUDED_PATHS = "SSO_EXCLUDED_PATHS";


    /**
     * login fail result
     */
    public static final ReturnT<String> SSO_LOGIN_FAIL_RESULT = new ReturnT(501, "sso not login.");


}
