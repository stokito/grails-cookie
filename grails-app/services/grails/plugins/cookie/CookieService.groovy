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
package grails.plugins.cookie

import grails.web.api.ServletAttributes

import javax.servlet.http.Cookie

/**
 * @author <a href='mailto:dale@dalew.com'>Dale Wiggins</a>
 * @author <a href='mailto:stokito@gmail.com'>Sergey Ponomarev</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class CookieService implements ServletAttributes {

    @SuppressWarnings("GroovyUnusedDeclaration")
    boolean transactional = false

    /**
     * Gets the value of the named cookie.
     * @param name Case-sensitive cookie name
     * @return Returns cookie value or null if cookie does not exist
     */
    String getCookie(String name) {
        request.getCookie name
    }

    /**
     * Gets the named cookie
     * @param name Case-sensitive cookie name
     * @return null if cookie not found
     */
    Cookie findCookie(String name) {
        request.findCookie name
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
    Cookie setCookie(String name, String value, Integer maxAge = null, String path = null, String domain = null, Boolean secure = null, Boolean httpOnly = null) {
        response.setCookie name, value, maxAge, path, domain, secure, httpOnly
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param args Named params eg <code>[name: 'cookie_name', value: 'some_val', secure: true] </code>
     */
    Cookie setCookie(Map args) {
        assert args
        response.setCookie args.name, args.value, args.maxAge, args.path, args.domain, args.secure, args.httpOnly
    }

    /** Sets the cookie. Note: it doesn't set defaults */
    Cookie setCookie(Cookie cookie) {
        response.setCookie cookie
    }

    /** Deletes the named cookie */
    Cookie deleteCookie(String name, String path = null, String domain = null) {
        assert name
        response.deleteCookie name, path, domain
    }

    /** Deletes the named cookie */
    Cookie deleteCookie(Cookie cookie) {
        assert cookie
        response.deleteCookie cookie
    }
}