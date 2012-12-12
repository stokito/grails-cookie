class CookieGrailsPlugin {
    // the plugin version
    def version = "0.4"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Dale Wiggins"
    def authorEmail = "dale@dalew.com"
    def title = "Cookie Plugin"
    def description = '''\\
Makes dealing with cookies easy.  Provides an injectable service and tag to easily get, set, and delete cookies with one line
'''
	def cookieService = new com.dalew.CookieService()
	
	def observe = ['controllers']

    // URL to the plugin's documentation
    def documentation = "https://github.com/dalew75/grails-cookie"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
	def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
	def scm = [ url: "https://github.com/dalew75/grails-cookie" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
		extendReqResp()
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        extendReqResp()
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
	
	void extendReqResp() {
		javax.servlet.http.HttpServletRequest.metaClass.getCookie = { String name ->
			return cookieService.get(name)
		}
		javax.servlet.http.HttpServletResponse.metaClass.setCookie = { String name, String value, Integer maxAge ->
			return cookieService.set(name,value,maxAge)
		}
		javax.servlet.http.HttpServletResponse.metaClass.deleteCookie = { String name ->
			return cookieService.delete(name)
		}
	}
	
}
