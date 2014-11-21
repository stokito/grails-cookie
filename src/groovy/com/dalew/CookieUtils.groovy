package com.dalew

import grails.util.Holders

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static com.dalew.CookieUtils.COOKIE_DEFAULT_PATH
import static com.dalew.CookieUtils.COOKIE_DEFAULT_HTTP_ONLY
import static com.dalew.CookieUtils.COOKIE_DEFAULT_SECURE


class CookieUtils {
    /** 30 days in seconds */
    static final int DEFAULT_COOKIE_AGE = 30 * 24 * 60 * 60
    static final String COOKIE_DEFAULT_PATH = '/'
    static final boolean COOKIE_DEFAULT_SECURE = false
    static final boolean COOKIE_DEFAULT_HTTP_ONLY = false
    static final int COOKIE_AGE_TO_DELETE = 0

    static void extendReqResp() {
        HttpServletRequest.metaClass.findCookie = { String name ->
            return Holders.applicationContext.cookieService.findCookie(name)
        }
        HttpServletRequest.metaClass.getCookie = { String name ->
            return Holders.applicationContext.cookieService.getCookie(name)
        }
        HttpServletResponse.metaClass.setCookie = { ...args ->
            assert args.size() == 1
            Holders.applicationContext.cookieService.setCookie(args[0])
            return null
        }
        HttpServletResponse.metaClass.deleteCookie = { ...args ->
            assert args.size() == 1
            Holders.applicationContext.cookieService.deleteCookie(args[0])
            return null
        }
    }
}
