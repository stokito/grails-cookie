package grails.plugin.cookie

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(CookieService)
class CookieServiceDefaultsSpec extends Specification {

    @Unroll
    void "setCookie() with args as list: #defaulAge #maxAge #expectedMaxAge #comment"() {
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
}
