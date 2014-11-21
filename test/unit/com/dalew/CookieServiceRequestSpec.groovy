package com.dalew

import grails.test.mixin.TestFor

@TestFor(CookieService)
class CookieServiceRequestSpec extends CookieRequestSpec {
    @Override
    def setup() {
        obj = service
    }
}
