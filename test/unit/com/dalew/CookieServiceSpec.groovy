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

abstract class CookieServiceSpec extends Specification {

    def response = new MockHttpServletResponse()
    def request = new MockHttpServletRequest()
    def obj

    def setupSpec() {
    }

    def setup() {
        def mockWebRequest = new GrailsWebRequest(request, response, new MockServletContext())
        WebUtils.storeGrailsWebRequest(mockWebRequest)
    }

    def cleanup() {
        WebUtils.clearGrailsWebRequest()
    }

    @Unroll
    def "findCookie() return cookie and is case-sensitive by name: #name #expectedCookieName"() {
        given:
        def cookie = new Cookie('cookie_name', 'cookie_val')
        request.cookies = [cookie]
        expect:
        obj.findCookie(name)?.name == expectedCookieName
        where:
        name          | expectedCookieName
        'cookie_name' | 'cookie_name'
        'CoOkIe_NaMe' | null
    }

    @Unroll
    def "getCookie() return cookie value and is case-sensitive by name: #name #expectedValue"() {
        given:
        request.cookies = [new Cookie('cookie_name', 'cookie_val')]
        expect:
        obj.getCookie(name) == expectedValue
        where:
        name          | expectedValue
        'cookie_name' | 'cookie_val'
        'CoOkIe_NaMe' | null
    }

    @Unroll
    void "setCookie(): #name #value #maxAge #path #domain #secure #httpOnly"() {
        given:
        obj.setCookie(args)
        def cookie = response.cookies[0]
        expect:
        cookie.name == name
        cookie.value == value
        cookie.maxAge == maxAge
        cookie.path == path
        cookie.domain == domain
        cookie.secure == secure
        cookie.httpOnly == httpOnly
        cookie.version == 1
        where:
        args                                                                   | name          | value        | maxAge  | path    | domain         | secure | httpOnly
        ['cookie_name', 'cookie_val']                                          | 'cookie_name' | 'cookie_val' | 2592000 | '/'     | null           | false  | false
        ['cookie_name', 'cookie_val', 42]                                      | 'cookie_name' | 'cookie_val' | 42      | '/'     | null           | false  | false
        ['cookie_name', 'cookie_val', 42, '/path']                             | 'cookie_name' | 'cookie_val' | 42      | '/path' | null           | false  | false
        ['cookie_name', 'cookie_val', 42, '/path', '.example.com', true, true] | 'cookie_name' | 'cookie_val' | 42      | '/path' | '.example.com' | true   | true
    }

    @Unroll
    void "setCookie() named params: #name #value #maxAge #path #domain #secure #httpOnly"() {
        given:
        obj.setCookie(args)
        def cookie = response.cookies[0]
        expect:
        cookie.name == name
        cookie.value == value
        cookie.maxAge == maxAge
        cookie.path == path
        cookie.domain == domain
        cookie.secure == secure
        cookie.httpOnly == httpOnly
        cookie.version == 1
        where:
        args                                                                                                                        | name          | value        | maxAge  | path    | domain         | secure | httpOnly
        [name: 'cookie_name', value: 'cookie_val']                                                                                  | 'cookie_name' | 'cookie_val' | 2592000 | '/'     | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42]                                                                      | 'cookie_name' | 'cookie_val' | 42      | '/'     | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path']                                                       | 'cookie_name' | 'cookie_val' | 42      | '/path' | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path', domain: '.example.com', secure: true, httpOnly: true] | 'cookie_name' | 'cookie_val' | 42      | '/path' | '.example.com' | true   | true
    }

    @Unroll
    void "setCookie(Cookie) doesn't set defaults: #maxAge #path #domain #secure #httpOnly"() {
        given:
        Cookie cookie = new Cookie('cookie_name', 'some_val')
        cookie.path = path
        cookie.maxAge = maxAge
        cookie.path = path
        if (domain) {
            cookie.domain = domain
        }
        cookie.secure = secure
        cookie.httpOnly = httpOnly
        cookie.version = 0
        obj.setCookie(cookie)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == 'some_val'
        cookieThatWasSet.maxAge == maxAge
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.secure == secure
        cookieThatWasSet.httpOnly == httpOnly
        cookieThatWasSet.version == 0
        where:
        maxAge  | path    | domain         | secure | httpOnly
        -1      | null    | null           | false  | false
        42      | '/'     | null           | false  | false
        42      | '/path' | null           | false  | false
        42      | '/path' | '.example.com' | true   | true
    }

    @Unroll
    def "deleteCookie() sets new cookie with same name but expired age: #name #path #domain"() {
        given:
        obj.deleteCookie(args)
        def cookie = response.cookies[0]
        expect:
        cookie.name == name
        cookie.value == null
        cookie.path == path
        cookie.domain == domain
        cookie.maxAge == 0
        cookie.version == 1
        where:
        args                                     | name          | path    | domain
        ['cookie_name']                          | 'cookie_name' | '/'     | null
        ['cookie_name', '/path']                 | 'cookie_name' | '/path' | null
        ['cookie_name', '/path', '.example.com'] | 'cookie_name' | '/path' | '.example.com'

    }

    @Unroll
    def "deleteCookie(Cookie) sets new cookie with same name but expired age: #name #pathIncomming #pathOucomming #domain"() {
        given:
        Cookie cookieToDelete = new Cookie(name, 'some_val')
        cookieToDelete.path = pathIncomming
        if (domain) {
            cookieToDelete.domain = domain
        }
        obj.deleteCookie(cookieToDelete)
        def cookie = response.cookies[0]
        expect:
        cookie.name == name
        cookie.value == null
        cookie.path == pathOucomming
        cookie.domain == domain
        cookie.maxAge == 0
        cookie.version == 1
        where:
        name          | pathIncomming | pathOucomming | domain
        'cookie_name' | null          | '/'           | null
        'cookie_name' | '/'           | '/'           | null
        'cookie_name' | '/path'       | '/path'       | null
        'cookie_name' | '/path'       | '/path'       | '.example.com'
    }
}
