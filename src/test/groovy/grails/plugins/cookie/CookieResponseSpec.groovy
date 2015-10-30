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
package grails.plugins.cookie

import org.grails.web.servlet.mvc.GrailsWebRequest
import org.grails.web.util.WebUtils
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
    private MockHttpServletRequest request = new MockHttpServletRequest()

    def setup() {
        request.contextPath = '/ctx'
        def mockWebRequest = new GrailsWebRequest(request, response, new MockServletContext())
        WebUtils.storeGrailsWebRequest(mockWebRequest)
    }

    def cleanup() {
        WebUtils.clearGrailsWebRequest()
    }

    @Unroll
    void "setCookie() with args as list: #maxAge #path #domain #secure #httpOnly"() {
        given:
        def cookieThatWasSet = obj.setCookie(args)
        expect:
        cookieThatWasSet == response.cookies[0]
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == 'cookie_val'
        cookieThatWasSet.maxAge == maxAge
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.secure == secure
        cookieThatWasSet.httpOnly == httpOnly
        cookieThatWasSet.version == 1
        where:
        args                                                                    | maxAge  | path    | domain         | secure | httpOnly
        ['cookie_name', 'cookie_val']                                           | 2592000 | '/ctx'  | null           | false  | true
        ['cookie_name', 'cookie_val', 42]                                       | 42      | '/ctx'  | null           | false  | true
        ['cookie_name', 'cookie_val', 42, '/path']                              | 42      | '/path' | null           | false  | true
        ['cookie_name', 'cookie_val', 42, '/path', '.example.com', true, false] | 42      | '/path' | '.example.com' | true   | false
    }

    @Unroll
    void "setCookie(): #maxAge #path #domain #secure #httpOnly"() {
        given:
        def cookieThatWasSet = obj.setCookie('cookie_name', 'cookie_val', maxAge, path, domain, secure, httpOnly)
        expect:
        cookieThatWasSet == response.cookies[0]
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
        null   | 2592000        | '/ctx'  | '/ctx'       | null           | false  | false
        42     | 42             | '/ctx'  | '/ctx'       | null           | false  | false
        42     | 42             | '/path' | '/path'      | null           | false  | false
        42     | 42             | '/path' | '/path'      | '.example.com' | true   | true
    }

    @Unroll
    void "setCookie() named params: #maxAge #path #domain #secure #httpOnly"() {
        given:
        def cookieThatWasSet = obj.setCookie(args)
        expect:
        cookieThatWasSet == response.cookies[0]
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == 'cookie_val'
        cookieThatWasSet.maxAge == maxAge
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.secure == secure
        cookieThatWasSet.httpOnly == httpOnly
        cookieThatWasSet.version == 1
        where:
        args                                                                                                                         | maxAge  | path    | domain         | secure | httpOnly
        [name: 'cookie_name', value: 'cookie_val']                                                                                   | 2592000 | '/ctx'  | null           | false  | true
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42]                                                                       | 42      | '/ctx'  | null           | false  | true
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path']                                                        | 42      | '/path' | null           | false  | true
        [name: 'cookie_name', value: 'cookie_val', maxAge: 42, path: '/path', domain: '.example.com', secure: true, httpOnly: false] | 42      | '/path' | '.example.com' | true   | false
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
        def cookieThatWasSet = obj.setCookie(cookie)
        expect:
        cookieThatWasSet == response.cookies[0]
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
        def cookieThatWasSet = obj.deleteCookie(args)
        expect:
        cookieThatWasSet == response.cookies[0]
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == path
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        args                                     | path    | domain
        ['cookie_name']                          | '/ctx'  | null
        ['cookie_name', '/path']                 | '/path' | null
        ['cookie_name', '/path', '.example.com'] | '/path' | '.example.com'
    }

    @Unroll
    def "deleteCookie() sets new cookie with same name but expired age: #path #domain"() {
        given:
        def cookieThatWasSet = obj.deleteCookie('cookie_name', path, domain)
        expect:
        cookieThatWasSet == response.cookies[0]
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == pathExpected
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        path    | pathExpected | domain
        null    | '/ctx'       | null
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
        def cookieThatWasSet = obj.deleteCookie(cookieToDelete)
        expect:
        cookieThatWasSet == response.cookies[0]
        cookieThatWasSet.name == 'cookie_name'
        cookieThatWasSet.value == null
        cookieThatWasSet.path == pathExpected
        cookieThatWasSet.domain == domain
        cookieThatWasSet.maxAge == 0
        cookieThatWasSet.version == 1
        where:
        path    | pathExpected | domain
        null    | '/ctx'       | null
        '/'     | '/'          | null
        '/path' | '/path'      | null
        '/path' | '/path'      | '.example.com'
    }
}
