package grails.plugin.cookie

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.util.GrailsWebMockUtil
import spock.lang.Specification
import spock.lang.Unroll

import static grails.plugin.cookie.CookieHelper.COOKIE_DEFAULT_HTTP_ONLY

@TestMixin(GrailsUnitTestMixin)
class CookieHelperDefaultsSpec extends Specification {

    def helper = new CookieHelper()

    def setupSpec() {
        GrailsWebMockUtil.bindMockWebRequest()
    }

    def cleanup() {
        helper.grailsApplication.config.grails.plugins.cookie.cookieage.default = null
        helper.grailsApplication.config.grails.plugins.cookie.httpOnly.default = null
        helper.grailsApplication.config.grails.plugins.cookie.secure.default = null
    }

    @Unroll
    void "getDefaultCookieAge(): #defaulAge #maxAge #expectedMaxAge #comment"() {
        given:
        if (defaulAge != null) {
            helper.grailsApplication.config.grails.plugins.cookie.cookieage.default = defaulAge
        }
        expect:
        helper.getDefaultCookieAge(maxAge) == expectedMaxAge
        where:
        defaulAge | maxAge | expectedMaxAge | comment
        null      | 42     | 42             | 'max directly'
        777       | null   | 777            | 'If max age is null it will be used from config'
        null      | null   | 2592000        | 'If config unset, will be used 30 days'
    }

    @Unroll
    void "getDefaultCookiePath(): #defaultStrategy #path #expectedPath #comment"() {
        given:
        helper.request.contextPath = ctx
        if (defaultStrategy != null) {
            helper.grailsApplication.config.grails.plugins.cookie.path.defaultStrategy = defaultStrategy
        }
        expect:
        helper.getDefaultCookiePath(path) == expectedPath
        where:
        ctx    | defaultStrategy | path    | expectedPath | comment
        '/ctx' | null            | null    | '/ctx'       | 'context strategy will used if defaultStrategy not set'
        '/ctx' | 'context'       | null    | '/ctx'       | 'context option will used if not set'
        null   | 'root'          | null    | '/'          | ''
        null   | 'current'       | null    | null         | ''
        null   | null            | '/path' | '/path'      | ''
    }

    @Unroll
    void "getDefaultCookieSecure(): #requestSecure #defaultSecure #secure #expectedSecure #comment"() {
        given:
        helper.request.secure = requestSecure
        if (defaultSecure != null) {
            helper.grailsApplication.config.grails.plugins.cookie.secure.default = defaultSecure
        }
        expect:
        helper.getDefaultCookieSecure(secure) == expectedSecure
        where:
        requestSecure | defaultSecure | secure | expectedSecure | comment
        false         | null          | true   | true           | ''
        false         | null          | false  | false          | ''
        false         | true          | null   | true           | ''
        false         | false         | null   | false          | ''
        false         | null          | null   | false          | ''
        true          | null          | null   | true           | ''
        false         | 'true'        | null   | true           | ''
        false         | 'y'           | null   | true           | ''
        false         | '1'           | null   | true           | ''
        false         | 'false'       | null   | false          | ''
        false         | 'no'          | null   | false          | ''
        false         | '0'           | null   | false          | ''
        false         | ''            | null   | false          | ''
        false         | ' '           | null   | false          | ''
    }

    @Unroll
    void "getDefaultCookieHttpOnly(): #defaultHttpOnly #httpOnly #expectedHttpOnly #comment"() {
        given:
        if (defaultHttpOnly != null) {
            helper.grailsApplication.config.grails.plugins.cookie.httpOnly.default = defaultHttpOnly
        }
        expect:
        helper.getDefaultCookieHttpOnly(httpOnly) == expectedHttpOnly
        where:
        defaultHttpOnly | httpOnly | expectedHttpOnly         | comment
        null            | true     | true                     | ''
        null            | false    | false                    | ''
        true            | null     | true                     | ''
        false           | null     | false                    | ''
        null            | null     | COOKIE_DEFAULT_HTTP_ONLY | ''
        'true'          | null     | true                     | ''
        'trUe'          | null     | true                     | ''
        'false'         | null     | false                    | ''
        'faLse'         | null     | false                    | ''
        '1'             | null     | true                     | ''
        'y'             | null     | true                     | ''
        'no'            | null     | false                    | ''
        '0'             | null     | false                    | ''
        ''              | null     | false                    | ''
        ' '             | null     | false                    | ''
    }
}
