package grails.plugin.cookie

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

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
}
