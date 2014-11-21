package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class CookieServiceResponseSpec extends CookieResponseSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    def setup() {
        obj = service
    }

    def cleanup() {
    }

}
