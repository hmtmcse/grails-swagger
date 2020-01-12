package com.hmtmcse.gs

import grails.plugins.*

class GrailsSwaggerGrailsPlugin extends Plugin {
    def grailsVersion = "3.3.8 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def version = "1.0.0"

    def title = "Grails Swagger API Definition"
    def author = "H.M.Touhid Mia (HMTMCSE)"
    def authorEmail = "hmtm.cse@gmail.com"
    def description = '''\
Grails Swagger API Definition plugin, will allow everyone to define their API end-point and how they will react.
In this plugin there is lots of build-in option for CRUD, filtering, search and sorting. And obviously there is lots
of are where can put custom codes or processor.   
'''
    def profiles = ['web']
    def documentation = "https://github.com/hmtmcse/grails-swagger/wiki"
    def scm = [ url: "https://github.com/hmtmcse/grails-swagger" ]
    def organization = [ name: "HMTM Foundation", url: "http://www.hmtmcse.com/" ]
    def issueManagement = [ system: "GitHub", url: "https://github.com/hmtmcse/grails-swagger/issues" ]
    def license = "MIT"

    Closure doWithSpring() {
        { ->
            gsConfigHolder(GsConfigHolder)
        }
    }

}
