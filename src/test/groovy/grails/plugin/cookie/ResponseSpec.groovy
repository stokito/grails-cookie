package grails.plugin.cookie

import grails.test.mixin.TestFor

@TestFor(CookieService)
class ResponseSpec extends CookieResponseSpec {

    @Override
    def setup() {
        obj = response
    }
}
