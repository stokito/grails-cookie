/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.cookie

import grails.util.Holders

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestResponseCookieExtension {

    static findCookie(HttpServletRequest request, String name) {
        return Holders.applicationContext.cookieService.findCookie(name)
    }

    static getCookie(HttpServletRequest request, String name) {
        return Holders.applicationContext.cookieService.getCookie(name)
    }

    static setCookie(HttpServletResponse response, Object... args) {
        return args.size() == 1 && ((args[0] instanceof List) || (args[0] instanceof Map) || (args[0] instanceof Cookie)) ?
                Holders.applicationContext.cookieService.setCookie(args[0]) :
                Holders.applicationContext.cookieService.setCookie(args as List)
    }

    static deleteCookie(HttpServletResponse response, Object... args) {
        return args.size() == 1 && ((args[0] instanceof List) || (args[0] instanceof Cookie)) ?
                Holders.applicationContext.cookieService.deleteCookie(args[0]) :
                Holders.applicationContext.cookieService.deleteCookie(args as List)
    }
}
