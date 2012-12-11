package com.dalew


class CookieTagLib {
	static namespace = "cookie"
	
	def cookieService
	
	def get = { attrs ->
		def value = cookieService.get(attrs.name)
		out << value
    }
	
}
