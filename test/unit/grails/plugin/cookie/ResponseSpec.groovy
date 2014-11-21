package grails.plugin.cookie

import grails.test.mixin.TestFor

@TestFor(CookieService)
class ResponseSpec extends CookieResponseSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    @Override
    def setup() {
        obj = response
    }
}
