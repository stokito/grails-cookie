package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class CookieServiceResponseSpec extends CookieResponseSpec {
    @Override
    def setup() {
        obj = service
    }
}
