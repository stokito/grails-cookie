package com.dalew

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.Cookie

@TestFor(CookieService)
class RequestSpec extends CookieServiceSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    def setup() {
        obj = service
    }

    def cleanup() {
    }

}
