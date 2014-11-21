package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class RequestSpec extends CookieGetSpec {
    def setupSpec() {
        CookieUtils.extendReqResp()
    }

    def setup() {
        obj = service
    }

    def cleanup() {
    }

}
