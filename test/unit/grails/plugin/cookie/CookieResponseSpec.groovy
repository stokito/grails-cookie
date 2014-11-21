package grails.plugin.cookie

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

abstract class CookieResponseSpec extends Specification {
    protected HttpServletResponse response = new MockHttpServletResponse()
    protected obj

    def setup() {
        def mockWebRequest = new GrailsWebRequest(new MockHttpServletRequest(), response, new MockServletContext())
        WebUtils.storeGrailsWebRequest(mockWebRequest)
    }

    def cleanup() {
        WebUtils.clearGrailsWebRequest()
    }

    @Unroll
    void "setCookie() with args as list: #name #value #maxAge #path #domain #secure #httpOnly"() {
        given:
        obj.setCookie(args)
        def cookie = response.cookies[0]
        expect:
        cookie.name == 'cookie_name'
        cookie.value == 'cookie_val'
        cookie.maxAge == maxAge
        cookie.path == path
        cookie.domain == domain
        cookie.secure == secure
        cookie.httpOnly == httpOnly
        cookie.version == 1
        where:
        args                                                                   | maxAge  | path    | domain         | secure | httpOnly
        ['cookie_name', 'cookie_val']                                          | 2592000 | '/'     | null           | false  | false
        ['cookie_name', 'cookie_val', 42]                                      | 42      | '/'     | null           | false  | false
        ['cookie_name', 'cookie_val', 42, '/path']                             | 42      | '/path' | null           | false  | false
        ['cookie_name', 'cookie_val', 42, '/path', '.example.com', true, true] | 42      | '/path' | '.example.com' | true   | true
    }

    @Unroll
    void "setCookie(): #maxAge #path #domain #secure #httpOnly"() {
        given:
        obj.setCookie('cookie_name', 'cookie_val', maxAge, path, domain, secure, httpOnly)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == 'cookie_val'
        cookieThatWasSet.maxAge == expectedMaxAge
        cookieThatWasSet.path == expectedPath
        cookieThatWasSet.domain == domain
        cookieThatWasSet.secure == secure
        cookieThatWasSet.httpOnly == httpOnly
        cookieThatWasSet.version == 1
        where:
        maxAge | expectedMaxAge | path    | expectedPath | domain         | secure | httpOnly
        null   | 2592000        | null    | '/'          | null           | false  | false
        42     | 42             | null    | '/'          | null           | false  | false
        42     | 42             | '/path' | '/path'      | null           | false  | false
        42     | 42             | '/path' | '/path'      | '.example.com' | true   | true
    }

    @Unroll
    void "setCookie() named params: #maxAge #path #domain #secure #httpOnly"() {
        given:
        obj.setCookie(args)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == 'cookie_val'
        cookieThatWasSet.maxAge == maxAge
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.secure == secure
        cookieThatWasSet.httpOnly == httpOnly
        cookieThatWasSet.version == 1
        where:
        args                                                                                                                        | maxAge  | path    | domain         | secure | httpOnly
        [name: 'cookie_name', value: 'cookie_val']                                                                                  | 2592000 | '/'     | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42]                                                                      | 42      | '/'     | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path']                                                       | 42      | '/path' | null           | false  | false
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path', domain: '.example.com', secure: true, httpOnly: true] | 42      | '/path' | '.example.com' | true   | true
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
        maxAge | path    | domain         | secure | httpOnly
        -1     | null    | null           | false  | false
        42     | '/'     | null           | false  | false
        42     | '/path' | null           | false  | false
        42     | '/path' | '.example.com' | true   | true
    }

    @Unroll
    def "deleteCookie() with args as list, sets new cookie with same name but expired age: #path #domain"() {
        given:
        obj.deleteCookie(args)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        args                                     | path    | domain
        ['cookie_name']                          | '/'     | null
        ['cookie_name', '/path']                 | '/path' | null
        ['cookie_name', '/path', '.example.com'] | '/path' | '.example.com'
    }

    @Unroll
    def "deleteCookie() sets new cookie with same name but expired age: #path #domain"() {
        given:
        obj.deleteCookie('cookie_name', path, domain)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == pathExpected
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        path    | pathExpected | domain
        null    | '/'          | null
        '/path' | '/path'      | null
        '/path' | '/path'      | '.example.com'
    }

    @Unroll
    def "deleteCookie(Cookie) sets new cookie with same name but expired age: #path #pathExpected #domain"() {
        given:
        Cookie cookieToDelete = new Cookie('cookie_name', 'some_val')
        cookieToDelete.path = path
        if (domain) {
            cookieToDelete.domain = domain
        }
        obj.deleteCookie(cookieToDelete)
        def cookieThatWasSet = response.cookies[0]
        expect:
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == pathExpected
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        path    | pathExpected | domain
        null    | '/'          | null
        '/'     | '/'          | null
        '/path' | '/path'      | null
        '/path' | '/path'      | '.example.com'
    }
}
