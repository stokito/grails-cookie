package com.dalew

import javax.servlet.http.Cookie
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpServletRequest

class CookieService {

    /** Default cookie age is 30 days */
    private static final int DEFAULT_COOKIE_AGE = 2592000

    boolean transactional = false
	
	def grailsApplication
	
    /**
     * Gets the value of the named cookie.
     * @return Returns null if does not exist
     **/
    String get(String name) {
        String cookieValue = getCookie(name)?.value
        if (cookieValue == null) {
            log.info "Found cookie \"${name}\", value = \"${cookieValue}\""
            return cookieValue
        } else {
            log.info "No cookie found with name: \"${name}\""
            return null
        }
    }

    /** Sets the cookie with name to value, with age in seconds */
    void set(String name, String value, Integer age = null) {
		age = age ?: getDefaultCookieAge()
		log.info "Setting cookie \"${name}\" to: \"${value}\" with age: ${age} seconds"
        setCookie(name, value, age)
    }

    int getDefaultCookieAge() {
        return grailsApplication.config.grails.plugins.cookie.cookieage.default ?: DEFAULT_COOKIE_AGE
    }

    /* Deletes the named cookie. */
	void delete(String name) {
        log.info "Removing cookie \"${name}\""
        setCookie(name,null,0)
    }

    /** ***********************************************************/
	
	private void setCookie(String name,String value,Integer age) {
		Cookie cookie = new Cookie(name, value)
		cookie.setPath('/')
        cookie.setMaxAge(age)
        WebUtils.retrieveGrailsWebRequest().getCurrentResponse().addCookie(cookie)
		log.info "cookie added: ${name} = ${value}"
    }

    /** Gets the named cookie */
    Cookie getCookie(String name, HttpServletRequest request = null) {
        assert name
        if (!request) {
            request = WebUtils.retrieveGrailsWebRequest().currentRequest
        }
        def cookies = request.getCookies()
        if (!cookies || !name) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        return cookies.find { it.name == name };
    }
	
}