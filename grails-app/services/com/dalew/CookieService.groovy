package com.dalew

import javax.servlet.http.Cookie
import org.codehaus.groovy.grails.web.util.WebUtils

class CookieService {

    /** Default cookie age is 30 days */
    private static final int DEFAULT_COOKIE_AGE = 2592000 // seconds

    boolean transactional = false

    def grailsApplication

    /**
     * Gets the value of the named cookie.
     * @return Returns null if does not exist
     */
    String getCookieValue(String name) {
        String cookieValue = getCookie(name)?.value
        if (cookieValue == null) {
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
     * @deprecated Use {@link #getCookieValue(String)} instead
     */
    String get(String name) {
        return getCookieValue(name)
    }

    /** Gets the named cookie */
    Cookie getCookie(String name) {
        assert name
        def cookies = WebUtils.retrieveGrailsWebRequest().currentRequest.getCookies()
        if (!cookies || !name) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        return cookies.find { it.name == name };
    }

    /** Sets the cookie with name to value, with age in seconds */
    void setCookie(String name, String value, Integer age = null) {
        age = age ?: getDefaultCookieAge()
        log.info "Setting cookie \"${name}\" to: \"${value}\" with age: ${age} seconds"
        writeCookieToResponse(name, value, age)
    }

    /**
     * Sets the cookie with name to value, with age in seconds
     * @deprecated Use {@link #setCookie(String, String, Integer)} instead
     */
    void set(String name, String value, Integer age = null) {
        setCookie(name, value, age)
    }

    int getDefaultCookieAge() {
        return grailsApplication.config.grails.plugins.cookie.cookieage.default ?: DEFAULT_COOKIE_AGE
    }

    /** Deletes the named cookie */
    void deleteCookie(String name) {
        log.info "Removing cookie \"${name}\""
        writeCookieToResponse(name, null, 0)
    }

    /**
     * Deletes the named cookie
     * @deprecated Use {@link #deleteCookie(String)} instead
     */
    void delete(String name) {
        deleteCookie(name)
    }

    /** Set or replace cookie */
    private void writeCookieToResponse(String name, String value, Integer age) {
        Cookie cookie = new Cookie(name, value)
        cookie.setPath('/')
        cookie.setMaxAge(age)
        WebUtils.retrieveGrailsWebRequest().getCurrentResponse().addCookie(cookie)
        log.info "cookie added: ${name} = ${value}"
    }

}