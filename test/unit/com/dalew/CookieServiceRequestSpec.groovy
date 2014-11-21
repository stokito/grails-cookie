package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class CookieServiceRequestSpec extends CookieRequestSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    def setup() {
        obj = service
    }

    def cleanup() {
    }

}
