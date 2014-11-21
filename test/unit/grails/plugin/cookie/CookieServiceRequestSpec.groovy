package grails.plugin.cookie

import grails.test.mixin.TestFor

@TestFor(CookieService)
class CookieServiceRequestSpec extends CookieRequestSpec {
    @Override
    def setup() {
        obj = service
    }
}
