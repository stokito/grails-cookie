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
    /** Default cookie age is 30 days */
    private static final int DEFAULT_COOKIE_AGE = 2592000 // seconds
    boolean transactional = false
    def grailsApplication

    /**
     * Gets the value of the named cookie.
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
     * Gets the value of the named cookie.
     * @return Returns null if does not exist
     * @deprecated Use {@link #getCookie(String)} instead. Will be removed in version 1.0
     */
    @Deprecated
    String get(String name) {
        return getCookie(name)
    }

    /**
     * Gets the named cookie
     * @return null if cookie not found
     */
    Cookie findCookie(String name) {
        assert name
        def cookies = WebUtils.retrieveGrailsWebRequest().currentRequest.getCookies()
        if (!cookies || !name) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        Cookie cookie = cookies.find { it.name == name }
        return cookie
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param name Cookie name. Can't be blank or null.
     * @param value Cookie value. Can be blank but not null.
     * @param maxAge Age to store cookie in seconds. Optional
     */
    void setCookie(String name, String value, Integer maxAge = null) {
        assert name
        assert value != null
        maxAge = maxAge ?: getDefaultCookieAge()
        Cookie cookie = createCookie(name, value, maxAge)
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

    /**
     * Sets the cookie with name to value, with age in seconds
     * @deprecated Use {@link #setCookie(String, String, Integer)} instead. Will be removed in version 1.0
     */
    @Deprecated
    void set(String name, String value, Integer age = null) {
        setCookie(name, value, age)
    }

    int getDefaultCookieAge() {
        return grailsApplication?.config?.grails?.plugins?.cookie?.cookieage?.default ?: DEFAULT_COOKIE_AGE
    }

    /** Deletes the named cookie */
    void deleteCookie(String name, String domain = null) {
        assert name
        log.info "Removing cookie \"${name}\""
        Cookie cookie = createCookie(name, null, 0)
        if (domain) {
            cookie.domain = domain
        }
        writeCookieToResponse(cookie)
    }

    /**
     * Deletes the named cookie
     * @deprecated Use {@link #deleteCookie(String)} instead. Will be removed in version 1.0
     */
    @Deprecated
    void delete(String name) {
        deleteCookie(name)
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value)
        cookie.setPath('/')
        cookie.setMaxAge(maxAge)
        return cookie
    }

    private void writeCookieToResponse(Cookie cookie) {
        WebUtils.retrieveGrailsWebRequest().getCurrentResponse().addCookie(cookie)
        log.info "cookie added: ${cookie.name} = ${cookie.value}"
    }

}