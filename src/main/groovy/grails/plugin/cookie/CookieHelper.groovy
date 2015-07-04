/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.cookie

import grails.web.api.ServletAttributes
import grails.web.api.WebAttributes
import groovy.util.logging.Commons

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Commons
class CookieHelper implements ServletAttributes, WebAttributes {

    static final int DEFAULT_COOKIE_AGE = 30 * 24 * 60 * 60
    static final boolean COOKIE_DEFAULT_HTTP_ONLY = true

    Cookie createCookie(String name, String value, Integer maxAge, String path, String domain, Boolean secure, Boolean httpOnly) {
        Cookie cookie = new Cookie(name, value)
        cookie.path = getDefaultCookiePath(path)
        cookie.maxAge = getDefaultCookieAge(maxAge)
        // Cookie.setDomain() tries to lowercase domain name and trow NPE if domain is null
        if (domain) {
            cookie.domain = domain
        }
        cookie.secure = getDefaultCookieSecure(secure)
        cookie.httpOnly = getDefaultCookieHttpOnly(httpOnly)
        cookie.version = 1
        cookie
    }

    /**
     * Default expiration age for cookie in seconds. `Max-Age` attribute, integer
     * If it has value `-1` cookie will not stored and removed after browser close.
     * If it has null value or unset, will be used 30 days, i.e. `2592000` seconds
     * Can't has value `0`, because it means that cookie should be removed
     */
    int getDefaultCookieAge(Integer maxAge) {
        maxAge != null ? maxAge : (grailsApplication.config.grails.plugins.cookie.cookieage.default ?: DEFAULT_COOKIE_AGE)
    }

    /*
     * Default path for cookie selection strategy.
     * 'context' - web app context path, i.e. `grails.app.context` option in `Config.groovy`
     * 'root' - root of server, i.e. '/'
     * 'current' - current directory, i.e. controller name
     * If default path is null or unset, it will be used 'context' strategy
     */
    String getDefaultCookiePath(String path) {
        String cookiePath
        if (path) {
            cookiePath = path
        } else if (grailsApplication.config.grails.plugins.cookie.path.defaultStrategy == 'root') {
            cookiePath = '/'
        } else if (grailsApplication.config.grails.plugins.cookie.path.defaultStrategy == 'current') {
            cookiePath = null
        } else {
            cookiePath = request.contextPath
        }
        cookiePath
    }

    /** If default secure is null or unset, it will set all new cookies as secure if current connection is secure */
    boolean getDefaultCookieSecure(Boolean secure) {
        if (secure != null) {
            return secure
        }
        def secureConfigValue = grailsApplication.config.grails.plugins.cookie.secure.default
        secureConfigValue?.toString() != null && !(secureConfigValue instanceof ConfigObject) ?
                secureConfigValue.toString().toBoolean() :
                request.secure
    }

    /** Default HTTP only param that denies accessing to JavaScript's `document.cookie`. If null or unset will be `true` */
    boolean getDefaultCookieHttpOnly(Boolean httpOnly) {
        if (httpOnly != null) {
            return httpOnly
        }
        def httpOnlyConfigValue = grailsApplication.config.grails.plugins.cookie.httpOnly.default
        httpOnlyConfigValue?.toString() != null && !(httpOnlyConfigValue instanceof ConfigObject) ?
                httpOnlyConfigValue.toString().toBoolean() :
                COOKIE_DEFAULT_HTTP_ONLY
    }

    void writeCookieToResponse(HttpServletResponse response, Cookie cookie) {
        response.addCookie cookie
        log.info "cookie set: ${cookie.name} = ${cookie.value}, Max-Age: ${cookie.maxAge}, Path: ${cookie.path}, Domain: ${cookie.domain}, HttpOnly: ${cookie.httpOnly}, Secure: ${cookie.secure}"
    }
}
