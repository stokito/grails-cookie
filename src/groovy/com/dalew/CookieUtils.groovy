package com.dalew

import grails.util.Holders

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CookieUtils {
    static void extendReqResp() {
        HttpServletRequest.metaClass.getCookie = { String name ->
            return Holders.applicationContext.cookieService.getCookie(name)
        }
        HttpServletResponse.metaClass.setCookie = { String name, String value, Integer maxAge = null ->
            return Holders.applicationContext.cookieService.setCookie(name, value, maxAge)
        }
        HttpServletResponse.metaClass.deleteCookie = { String name, String domain = null ->
            return Holders.applicationContext.cookieService.deleteCookie(name, domain)
        }
    }
}
