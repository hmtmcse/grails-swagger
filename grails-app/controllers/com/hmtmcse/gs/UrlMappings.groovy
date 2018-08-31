package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiVersionActionsData
import com.hmtmcse.gs.data.GsUrlMappingData
import grails.util.Holders

class UrlMappings {


    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }


        group("/api", {

        })

        GsUrlMappingUtil.getUrlMappingData().each { GsApiVersionActionsData urls ->
            println(urls.versionPrefix)
        }

        Holders.grailsApplication.mainContext

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
