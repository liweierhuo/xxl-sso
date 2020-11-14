package com.xxl.sso.client.filter;

import com.xxl.sso.client.conf.Conf;
import com.xxl.sso.client.entity.ReturnT;
import com.xxl.sso.client.login.SsoTokenLoginHelper;
import com.xxl.sso.client.path.impl.AntPathMatcher;
import com.xxl.sso.client.user.XxlSsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * app sso filter
 *
 * @author xuxueli 2018-04-08 21:30:54
 */
public class XxlSsoTokenFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(XxlSsoTokenFilter.class);

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String ssoServer;
    private String logoutPath;
    private String loginCheckPath;
    private String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        loginCheckPath = filterConfig.getInitParameter(Conf.SSO_LOGIN_CHECK_PATH);
        excludedPaths = filterConfig.getInitParameter(Conf.SSO_EXCLUDED_PATHS);

        logger.info("XxlSsoTokenFilter init.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // make url
        String servletPath = req.getServletPath();

        // excluded path check
        if (excludedPaths!=null && excludedPaths.trim().length()>0) {
            for (String excludedPath:excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();

                // 支持ANT表达式
                if (antPathMatcher.match(uriPattern, servletPath)) {
                    // excluded path, allow
                    chain.doFilter(request, response);
                    return;
                }

            }
        }

        // logout filter
        if (logoutPath!=null
                && logoutPath.trim().length()>0
                && logoutPath.equals(servletPath)) {
            // logout
            SsoTokenLoginHelper.logout(req, ssoServer+logoutPath);
            // response
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":"+ ReturnT.SUCCESS_CODE+", \"msg\":\"\"}");
            return;
        }

        // login filter
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(req, ssoServer+loginCheckPath);
        if (xxlUser == null) {
            // response
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":"+Conf.SSO_LOGIN_FAIL_RESULT.getCode()+", \"msg\":\""+ Conf.SSO_LOGIN_FAIL_RESULT.getMsg() +"\"}");
            return;
        }
        // ser sso user
        request.setAttribute(Conf.SSO_USER, xxlUser);
        // already login, allow
        chain.doFilter(request, response);
        return;
    }
}
