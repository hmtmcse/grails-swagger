package com.hmtmcse.gs

class BootStrap {

    def init = { servletContext ->
        println("Called Plugin Bootstrap")
    }
    def destroy = {
    }
}
