import grails.plugins.Plugin

class GrailsCookieGrailsPlugin extends Plugin {
    def grailsVersion = "3.0.0 > *"
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
            [name: 'Christian Oestreich', email: 'acetrike@gmail.com'],
            [name: 'Uday Pratap Singh', email: 'uday.singh@tothenew.com']
    ]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/stokito/grails-cookie/issues']
    def scm = [url: 'https://github.com/stokito/grails-cookie']
}
