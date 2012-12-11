package com.dalew

import javax.servlet.http.Cookie
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.context.request.RequestContextHolder

class CookieService {

    boolean transactional = false
	
	def grailsApplication
	
	/* Gets the value of the named cookie.  Returns null if does not exist */
    
	String get(String name) {
		Cookie cookie = getCookie(name)
		if ( cookie ) {
			def value = cookie.getValue()
			log.info "Found cookie \"${name}\", value = \"${value}\""
            return value
		}
		else {
			log.info "No cookie found with name: \"${name}\""
			return null
		}
    }

    /* Sets the cookie with name to value, with maxAge in seconds */
    void set(String name,String value,Integer age = null) {
		age = age ?: grailsApplication.config.grails.plugins.cookie.cookieage.default ?: 2592000 // default 30 days
		log.info "Setting cookie \"${name}\" to: \"${value}\" with age: ${age} seconds"
        setCookie(name, value, age)
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
	
	/** ***********************************************************/
    
	private Cookie getCookie(String name) {
		assert name
		
        Cookie[] cookies = RequestContextHolder.currentRequestAttributes().request.getCookies()
		
		if ( cookies ) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i]
				}
			}
		}
		return null
    }

}