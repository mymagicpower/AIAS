package me.aias.common.utils;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端设备信息
 *
 * @author Calvin
 * @date 2021-06-20
 **/
@Slf4j
public class UserAgentUtil {
    private UserAgent userAgent;
    private String userAgentString;
    private HttpServletRequest request;

    public UserAgentUtil(HttpServletRequest request) {
        this.request = request;
        userAgentString = request.getHeader("User-Agent");
        userAgent = UserAgent.parseUserAgentString(userAgentString);
    }

    /**
     * 获取浏览器类型
     */
    public String getBrowser() {
        if (null == userAgent) {
            return "";
        }
        return userAgent.getBrowser().getName();
    }

    /**
     * 获取操作系统
     */
    public String getOS() {
        if (null == userAgent) {
            return "Unknown Device";
        }
        return userAgent.getOperatingSystem().getName();
    }
}
