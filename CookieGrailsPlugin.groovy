/*
 * Copyright 2014 the original author or authors
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

import grails.plugin.cookie.CookieUtils

class CookieGrailsPlugin {
    def version = '1.4'
    def grailsVersion = '2.4.0 > *'
    def author = 'Sergey Ponomarev'
    def authorEmail = 'stokito@gmail.com'
    def title = 'Cookie Plugin'
    def description = 'Makes dealing with cookies easy. Provides an injectable service and expands request with methods to easily get, set, and delete cookies with one line'
    def observe = ['controllers']
    def documentation = 'https://github.com/stokito/grails-cookie'
    def license = 'APACHE'
    def developers = [
            [name: 'Dale Wiggins', email: 'dale@dalew.com', role: 'original author'],
            [name: 'Sergey Ponomarev', email: 'stokito@gmail.com', role: 'current maintainer'],
            [name: 'Alexey Zhokhov', email: 'donbeave@gmail.com'],
            [name: 'Christian Oestreich', email: 'acetrike@gmail.com']
    ]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/stokito/grails-cookie/issues']
    def scm = [url: 'https://github.com/stokito/grails-cookie']

    def doWithDynamicMethods = { applicationContext ->
        CookieUtils.extendReqResp()
    }
}