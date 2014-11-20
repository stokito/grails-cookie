/*
 * Copyright 2012 the original author or authors
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
package com.dalew

import org.codehaus.groovy.grails.web.util.WebUtils

import javax.servlet.http.Cookie

/**
 * @author <a href='mailto:dale@dalew.com'>Dale Wiggins</a>
 * @author <a href='mailto:stokito@gmail.com'>Sergey Ponomarev</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class CookieService {
    static final int DEFAULT_COOKIE_AGE = 30 * 24 * 60 * 60
    static final String COOKIE_DEFAULT_PATH = '/'
    static final boolean COOKIE_DEFAULT_SECURE = false
    static final boolean COOKIE_DEFAULT_HTTP_ONLY = false
    static final int COOKIE_AGE_TO_DELETE = 0
    // 30 days in seconds
    boolean transactional = false
    def grailsApplication

    /**
     * Gets the value of the named cookie.
     * @param name Case-sensitive cookie name
     * @return Returns cookie value or null if cookie does not exist
     */
    String getCookie(String name) {
        assert name
        String cookieValue = findCookie(name)?.value
        if (cookieValue != null) {
            log.info "Found cookie \"${name}\", value = \"${cookieValue}\""
            return cookieValue
        } else {
            log.info "No cookie found with name: \"${name}\""
            return null
        }
    }

    /**
     * Gets the named cookie
     * @param name Case-sensitive cookie name
     * @return null if cookie not found
     */
    Cookie findCookie(String name) {
        assert name
        def cookies = WebUtils.retrieveGrailsWebRequest().currentRequest.cookies
        if (!cookies) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        Cookie cookie = cookies.find { it.name == name }
        return cookie
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param name Cookie name. Can't be blank or null and is case-sensitive
     * @param value Cookie value. Can be blank but not null.
     * @param maxAge Age to store cookie in seconds; if negative, means the cookie is not stored; if zero, deletes the cookie. Optional
     * @param path A path to which the client should return the cookie. The cookie is visible to all the pages in the directory. For example, <i>/catalog</i>, which makes the cookie visible to all directories on the server under <i>/catalog</i>. See RFC 2109
     * @param domain Domain name by RFC 2109. It begins with a dot (.example.com) and means that the cookie is visible to servers in a specified DNS zone (for example, www.example.com, but not a.b.example.com).
     *               By default, cookies are only returned to the server that sent them.
     * @param secure Indicates to the browser whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL.
     * @param httpOnly HttpOnly cookies are not supposed to be exposed to client-side JavaScript code, and may therefore help mitigate certain kinds of cross-site scripting attacks.
     */
    void setCookie(String name, String value, Integer maxAge = null, String path = COOKIE_DEFAULT_PATH, String domain = null, boolean secure = COOKIE_DEFAULT_SECURE, boolean httpOnly = COOKIE_DEFAULT_HTTP_ONLY) {
        Cookie cookie = createCookie(name, value, maxAge, path, domain, secure, httpOnly)
        setCookie(cookie)
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param args Named params eg <code>[name: 'cookie_name', value: 'some_val', secure: true] </code>
     */
    void setCookie(Map args) {
        Cookie cookie = createCookie(args.name, args.value, args.maxAge, args.path, args.domain, args.secure, args.httpOnly)
        setCookie(cookie)
    }

    /**
     * Sets the cookie
     * @see #setCookie(String, String, Integer)
     */
    void setCookie(Cookie cookie) {
        assert cookie
        log.info "Setting cookie \"${cookie.name}\" to: \"${cookie.value}\" with maxAge: ${cookie.maxAge} seconds"
        writeCookieToResponse(cookie)
    }

    /** Deletes the named cookie */
    void deleteCookie(String name, String domain = null) {
        assert name
        log.info "Removing cookie \"${name}\""
        Cookie cookie = createCookie(name, null, COOKIE_AGE_TO_DELETE, null, null, null, null)
        if (domain) {
            cookie.domain = domain
        }
        writeCookieToResponse(cookie)
    }

    private Cookie createCookie(String name, String value, Integer maxAge, String path, String domain, Boolean secure, Boolean httpOnly) {
        Cookie cookie = new Cookie(name, value)
        cookie.path = path ?: COOKIE_DEFAULT_PATH
        cookie.maxAge = maxAge != null ? maxAge : defaultCookieAge
        // Cookie.setDomain() tries to lowercase domain name and trow NPE if domain is null
        if (domain) {
            cookie.domain = domain
        }
        cookie.secure = secure != null ? secure : COOKIE_DEFAULT_SECURE
        cookie.httpOnly = httpOnly != null ? httpOnly : COOKIE_DEFAULT_HTTP_ONLY
        cookie.version = 1
        return cookie
    }

    private void writeCookieToResponse(Cookie cookie) {
        WebUtils.retrieveGrailsWebRequest().currentResponse.addCookie(cookie)
        log.info "cookie added: ${cookie.name} = ${cookie.value}"
    }

    private int getDefaultCookieAge() {
        return grailsApplication.config.grails?.plugins?.cookie?.cookieage?.default ?: DEFAULT_COOKIE_AGE
    }
}