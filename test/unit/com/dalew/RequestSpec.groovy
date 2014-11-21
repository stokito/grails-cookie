package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class RequestSpec extends CookieRequestSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    @Override
    def setup() {
        obj = request
    }
}
