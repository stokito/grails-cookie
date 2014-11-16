This plugin makes dealing with cookies easy. Provides an injectable service and tag to easily get, set, and delete cookies with one line.

## Installation

To install the cookie plug-in just add to `BuildConfig.groovy`:
```groovy
compile ':cookie:0.52'
```

## Configuration

You can configure in `Config.groovy` how long the default cookie age will be (in seconds) when not explicitly supplied while setting a cookie.
```groovy
grails.plugins.cookie.cookieage.default = 86400 // if not specified default in code is 30 days
```

## Usage

You have two ways to work with cookies:
* The cookie plug-in extends the `request` and `response` objects found in controllers, filters, etc to allow the following.
* The cookie plug-in provides a [CookieService](./grails-app/services/com/dalew/CookieService.groovy) that can be used anywhere in your Grails application.

Example of setting a new cookie:
```groovy
// This sets a cookie with the name `username` to the value `cookieUser123` with a expiration set to a week, defined in seconds
response.setCookie('username', 'cookieUser123', 604800)
// will use default age from Config (or 30 days if not defined)
response.setCookie('username', 'cookieUser123')
// using service
def cookieService // define field for DI
...
cookieService.setCookie('username', 'cookieUser123', 604800)
```

To get the cookie value:
```groovy
request.getCookie('username') // returns 'cookieUser123'
// using service
def cookieService // define field for DI
...
cookieService.getCookie('username') // returns 'cookieUser123'
```

To delete the cookie (actually it set new expired cookie with same name):
```groovy
response.deleteCookie('username') // deletes the 'username' cookie
// using service
def cookieService // define field for DI
...
cookieService.deleteCookie('username')
```
You can check out [Demo project](https://github.com/stokito/grails-cookie-demo)

## Notes and Changelog
Since v0.5 few things was deprecated and will be removed in version v1.0:
* Tag `<cookie:get/>`. Use standard `<g:cookie/>` tag instead.
* Methods `get()`, `set()`, `delete()` from CookieService. They are replaced with corresponding `getCookie()`, `setCookie()`, `deleteCookie()`.

In the v0.3 release a big issue was fixed that now sets the cookie's path to the root `/` context.
Otherwise it was setting the path to the same as the controller/service that triggered it.
Most users I believe will want this behavior. If setting the path is desired, that can be accomodated.
Please contact me or do a pull request if you'd like that.
