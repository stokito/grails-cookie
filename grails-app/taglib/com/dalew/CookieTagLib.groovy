package com.dalew


class CookieTagLib {

	static namespace = 'cookie'
	
	def cookieService

    /**
     * Prints the value of a cookie.
     * Usage:
     * <code>
     * <cookie:get name='username' />
     * </code>
     *
     * @deprecated Is obsolete since we can use standard <g:cookie/>
     * @emptyTag
     * @attr name REQUIRED the cookie name
     */
    def get = { attrs ->
		def value = cookieService.get(attrs.name)
		out << value
    }
	
}
