package grails.plugin.cookie

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification
import spock.lang.Unroll
import static grails.plugin.cookie.CookieUtils.COOKIE_DEFAULT_HTTP_ONLY


@TestFor(CookieService)
class CookieServiceDefaultsSpec extends Specification {

    @Unroll
    void "getDefaultCookieAge(): #defaulAge #maxAge #expectedMaxAge #comment"() {
        given:
        service.grailsApplication.config.grails.plugins.cookie.cookieage.default = defaulAge
        expect:
        service.getDefaultCookieAge(maxAge) == expectedMaxAge
        where:
        defaulAge | maxAge | expectedMaxAge | comment
        -1        | 42     | 42             | 'max gae directly'
        777       | null   | 777            | 'If max age is null it will be used from config'
        null      | null   | 2592000        | 'If config unset, will be used 30 days'
    }

    @Unroll
    void "getDefaultCookiePath(): #defaultStrategy #path #expectedPath #comment"() {
        given:
        service.grailsApplication.config.grails.app.context = '/ctx'
        service.grailsApplication.config.grails.plugins.cookie.defaultStrategy = defaultStrategy
        expect:
        service.getDefaultCookiePath(path) == expectedPath
        where:
        defaultStrategy | path    | expectedPath | comment
        null            | null    | '/ctx'       | 'context option will used if not set'
        'context'       | null    | '/ctx'       | ''
        'root'          | null    | '/'          | ''
        'current'       | null    | null         | ''
        null            | '/path' | '/path'      | ''
    }

    @Unroll
    void "getDefaultCookieSecure(): #requestSecure #defaultSecure #secure #expectedSecure #comment"() {
        given:
        def request = new MockHttpServletRequest()
        request.secure = requestSecure
        def mockWebRequest = new GrailsWebRequest(request, new MockHttpServletResponse(), new MockServletContext())
        WebUtils.storeGrailsWebRequest(mockWebRequest)
        service.grailsApplication.config.grails.plugins.cookie.secure.default = defaultSecure
        expect:
        service.getDefaultCookieSecure(secure) == expectedSecure
        where:
        requestSecure | defaultSecure | secure | expectedSecure | comment
        false         | null          | true   | true           | ''
        false         | null          | false  | false          | ''
        false         | true          | null   | true           | ''
        false         | false         | null   | false          | ''
        false         | null          | null   | false          | ''
        true          | null          | null   | true           | ''
    }

    @Unroll
    void "getDefaultCookieHttpOnly(): #defaultHttpOnly #httpOnly #expectedHttpOnly #comment"() {
        given:
        service.grailsApplication.config.grails.plugins.cookie.httpOnly.default = defaultHttpOnly
        expect:
        service.getDefaultCookieHttpOnly(httpOnly) == expectedHttpOnly
        where:
        defaultHttpOnly | httpOnly | expectedHttpOnly         | comment
        null            | true     | true                     | ''
        null            | false    | false                    | ''
        true            | null     | true                     | ''
        false           | null     | false                    | ''
        null            | null     | COOKIE_DEFAULT_HTTP_ONLY | ''
    }
}
