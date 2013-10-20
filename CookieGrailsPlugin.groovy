class CookieGrailsPlugin {
    /** the plugin version */
    def version = '0.51'
    /** the version or versions of Grails the plugin is designed for */
    def grailsVersion = '2.1 > *'
    /** the other plugins this plugin depends on */
    def dependsOn = [:]
    /** resources that are excluded from plugin packaging */
    def pluginExcludes = [
            'grails-app/views/error.gsp'
    ]

    def author = 'Dale Wiggins'
    def authorEmail = 'dale@dalew.com'
    def title = 'Cookie Plugin'
    def description = 'Makes dealing with cookies easy. Provides an injectable service and expands request with methods to easily get, set, and delete cookies with one line'

    def cookieService = new com.dalew.CookieService()

    def observe = ['controllers']

    /** URL to the plugin's documentation */
    def documentation = 'https://github.com/dalew75/grails-cookie'

    /** License: one of 'APACHE', 'GPL2', 'GPL3' */
    def license = 'APACHE'

    /** Any additional developers beyond the author specified above. */
    def developers = [[name: 'Sergey Ponomarev', email: 'stokito@gmail.com']]

    /** Location of the plugin's issue tracker. */
    def issueManagement = [system: 'github', url: 'https://github.com/dalew75/grails-cookie/issues']

    /** Online location of the plugin's browseable source code. */
    def scm = [url: 'https://github.com/dalew75/grails-cookie']

	def doWithDynamicMethods = { applicationContext ->
		extendReqResp()
    }
	
    def onChange = { event ->
        extendReqResp()
    }

    void extendReqResp() {
        javax.servlet.http.HttpServletRequest.metaClass.getCookie = { String name ->
            return cookieService.getCookie(name)
        }
        javax.servlet.http.HttpServletResponse.metaClass.setCookie = { String name, String value, Integer maxAge = null ->
            return cookieService.setCookie(name, value, maxAge)
        }
        javax.servlet.http.HttpServletResponse.metaClass.deleteCookie = { String name ->
            return cookieService.deleteCookie(name)
        }
    }

}
