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
package grails.plugin.cookie

import org.codehaus.groovy.grails.web.util.WebUtils

import javax.servlet.http.Cookie

import static grails.plugin.cookie.CookieUtils.*

/**
 * @author <a href='mailto:dale@dalew.com'>Dale Wiggins</a>
 * @author <a href='mailto:stokito@gmail.com'>Sergey Ponomarev</a>
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class CookieService {
    @SuppressWarnings("GroovyUnusedDeclaration")
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
        log.info(cookieValue ? "Found cookie \"${name}\", value = \"${cookieValue}\"" : "No cookie found with name: \"${name}\"")
        return cookieValue
    }

    /**
     * Gets the named cookie
     * @param name Case-sensitive cookie name
     * @return null if cookie not found
     */
    Cookie findCookie(String name) {
        assert name
        return WebUtils.retrieveGrailsWebRequest().currentRequest.cookies?.find { it.name == name }
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
        return setCookie(createCookie(name, value, maxAge, path, domain, secure, httpOnly))
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @param args Named params eg <code>[name: 'cookie_name', value: 'some_val', secure: true] </code>
     */
    Cookie setCookie(Map args) {
        assert args
        return setCookie(createCookie(args.name, args.value, args.maxAge, args.path, args.domain, args.secure, args.httpOnly))
    }

    /** Sets the cookie. Note: it doesn't set defaults */
    Cookie setCookie(Cookie cookie) {
        assert cookie
        log.info 'Setting cookie'
        writeCookieToResponse(cookie)
        return cookie
    }

    /** Deletes the named cookie */
    Cookie deleteCookie(String name, String path = null, String domain = null) {
        assert name
        log.info 'Removing cookie'
        Cookie cookie = createCookie(name, null, COOKIE_AGE_TO_DELETE, path, domain, null, null)
        writeCookieToResponse(cookie)
        return cookie
    }

    /** Deletes the named cookie */
    Cookie deleteCookie(Cookie cookie) {
        assert cookie
        return deleteCookie(cookie.name, cookie.path, cookie.domain)
    }

    private Cookie createCookie(String name, String value, Integer maxAge, String path, String domain, Boolean secure, Boolean httpOnly) {
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
        return cookie
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeCookieToResponse(Cookie cookie) {
        WebUtils.retrieveGrailsWebRequest().currentResponse.addCookie(cookie)
        log.info "cookie set: ${cookie.name} = ${cookie.value}, Max-Age: ${cookie.maxAge}, Path: ${cookie.path}, Domain: ${cookie.domain}, HttpOnly: ${cookie.httpOnly}, Secure: ${cookie.secure}"
    }

    /**
     * Default expiration age for cookie in seconds. `Max-Age` attribute, integer
     * If it has value `-1` cookie will not stored and removed after browser close.
     * If it has null value or unset, will be used 30 days, i.e. `2592000` seconds
     * Can't has value `0`, because it means that cookie should be removed
     */
    int getDefaultCookieAge(Integer maxAge) {
        return maxAge != null ? maxAge : (grailsApplication.config.grails.plugins.cookie.cookieage.default ?: DEFAULT_COOKIE_AGE)
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
            cookiePath = WebUtils.retrieveGrailsWebRequest().currentRequest.contextPath
        }
        return cookiePath
    }

    /** If default secure is null or unset, it will set all new cookies as secure if current connection is secure */
    boolean getDefaultCookieSecure(Boolean secure) {
        if (secure != null) {
            return secure
        }
        def secureConfigValue = grailsApplication.config.grails.plugins.cookie.secure.default
        return secureConfigValue != null && !(secureConfigValue instanceof ConfigObject) ?
                secureConfigValue.toString().toBoolean() :
                WebUtils.retrieveGrailsWebRequest().currentRequest.secure
    }

    /** Default HTTP only param that denies accessing to JavaScript's `document.cookie`. If null or unset will be `true` */
    boolean getDefaultCookieHttpOnly(Boolean httpOnly) {
        if (httpOnly != null) {
            return httpOnly
        }
        def httpOnlyConfigValue = grailsApplication.config.grails.plugins.cookie.httpOnly.default
        return httpOnlyConfigValue != null && !(httpOnlyConfigValue instanceof ConfigObject) ?
                httpOnlyConfigValue.toString().toBoolean() :
                COOKIE_DEFAULT_HTTP_ONLY
    }

}