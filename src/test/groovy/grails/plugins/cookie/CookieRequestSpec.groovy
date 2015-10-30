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
import javax.servlet.http.HttpServletRequest

abstract class CookieRequestSpec extends Specification {
    protected HttpServletRequest request = new MockHttpServletRequest()
    protected obj

    def setup() {
        def mockWebRequest = new GrailsWebRequest(request, new MockHttpServletResponse(), new MockServletContext())
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
}
