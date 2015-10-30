package grails.plugin.cookie

import grails.test.mixin.TestFor

@TestFor(CookieService)
class RequestSpec extends CookieRequestSpec {

    @Override
    def setup() {
        obj = request
    }
}
