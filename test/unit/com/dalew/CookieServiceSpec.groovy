package com.dalew

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification

import javax.servlet.http.Cookie

@TestFor(CookieService)
class CookieServiceSpec extends Specification {
    def response = new MockHttpServletResponse()
    def request = new MockHttpServletRequest()

    def setup() {
        def mockWebRequest = new GrailsWebRequest(request, response, new MockServletContext())
        WebUtils.storeGrailsWebRequest(mockWebRequest)
    }

    def cleanup() {
        WebUtils.clearGrailsWebRequest()
    }

    def "findCookie()"() {
        given:
        def cookie = new Cookie('some_cookie_name', 'cookie_value')
        request.cookies = [cookie]
        expect: service.findCookie('some_cookie_name') == cookie
    }

    def "getCookie() return cookie value"() {
        given: request.cookies = [new Cookie('some_cookie_name', 'cookie_value')]
        expect: service.getCookie('some_cookie_name') == 'cookie_value'
    }

    def "getCookie() is case-sensitive"() {
        given: request.cookies = [new Cookie('some_cookie_name', 'cookie_value')]
        expect: service.getCookie('SoMe_CoOkIe_NaMe') == null
    }

    void "setCookie()"() {
        when: service.setCookie('some_cookie_name', 'cookie_value')
        then:
        response.cookies[0].name == 'some_cookie_name'
        response.cookies[0].value == 'cookie_value'
        response.cookies[0].maxAge == 2592000
    }

    def "deleteCookie() sets new cookie with same name but expired age"() {
        when: service.deleteCookie('some_cookie_name') == null
        then:
        response.cookies[0].name == 'some_cookie_name'
        response.cookies[0].value == null
        response.cookies[0].maxAge == 0
    }
}
