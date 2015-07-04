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

import groovy.util.logging.Commons

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Commons
class RequestResponseCookieExtension {

    static final int COOKIE_AGE_TO_DELETE = 0

    private static final helper = new CookieHelper()

    static Cookie findCookie(HttpServletRequest request, String name) {
        assert name
        request.cookies?.find { it.name == name }
    }

    static String getCookie(HttpServletRequest request, String name) {
        assert name
        String cookieValue = findCookie(request, name)?.value
        log.info(cookieValue ? "Found cookie \"${name}\", value = \"${cookieValue}\"" : "No cookie found with name: \"${name}\"")
        cookieValue
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param name Cookie name. Can't be blank or null and is case-sensitive
     * @param value Cookie value.
     * @param maxAge Age to store cookie in seconds; if negative, means the cookie is not stored; if zero, deletes the cookie.
     * @param path A path to which the client should return the cookie. The cookie is visible to all the pages in the directory. For example, <i>/catalog</i>, which makes the cookie visible to all directories on the server under <i>/catalog</i>. See RFC 2109
     * @param domain Domain name by RFC 2109. It begins with a dot (.example.com) and means that the cookie is visible to servers in a specified DNS zone (for example, www.example.com, but not a.b.example.com).
     * @param secure Indicates to the browser whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL.
     * @param httpOnly "HTTP Only" cookies are not supposed to be exposed to client-side JavaScript code, and may therefore help mitigate XSS attack.
     */
    static Cookie setCookie(HttpServletResponse response, String name, String value, Integer maxAge = null, String path = null, String domain = null, Boolean secure = null, Boolean httpOnly = null) {
        setCookie response, helper.createCookie(name, value, maxAge, path, domain, secure, httpOnly)
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param args Named params eg <code>[name: 'cookie_name', value: 'some_val', secure: true] </code>
     */
    static Cookie setCookie(HttpServletResponse response, Map args) {
        assert args
        setCookie response, helper.createCookie(args.name, args.value, args.maxAge, args.path, args.domain, args.secure, args.httpOnly)
    }

    /** Sets the cookie. Note: it doesn't set defaults */
    static Cookie setCookie(HttpServletResponse response, Cookie cookie) {
        assert cookie
        log.info 'Setting cookie'
        helper.writeCookieToResponse response, cookie
        cookie
    }

    /** Deletes the named cookie */
    static Cookie deleteCookie(HttpServletResponse response, String name, String path = null, String domain = null) {
        assert name
        log.info 'Removing cookie'
        Cookie cookie = helper.createCookie(name, null, COOKIE_AGE_TO_DELETE, path, domain, null, null)
        helper.writeCookieToResponse response, cookie
        cookie
    }

    /** Deletes the named cookie */
    static Cookie deleteCookie(HttpServletResponse response, Cookie cookie) {
        assert cookie
        deleteCookie response, cookie.name, cookie.path, cookie.domain
    }
}
