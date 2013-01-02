package com.dalew


class CookieTagLib {

    static namespace = 'cookie'

    def cookieService

    /**
     * Prints the value of a cookie.
     * Usage:
     * <code>
     * &lt;cookie:get name='username' /&gt;
     * </code>
     *
     * @deprecated Is obsolete since we can use standard &lt;g:cookie/&gt;
     * @emptyTag
     * @attr name REQUIRED the cookie name
     */
    def get = { attrs ->
        def value = cookieService.getCookie(attrs.name)
        out << value
    }

}
