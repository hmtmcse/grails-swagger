package com.hmtmcse.gs

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }


        group("/api", {

        })

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
