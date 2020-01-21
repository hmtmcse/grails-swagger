package com.hmtmcse.gs.controllers

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class ApiTestingV2InterceptorSpec extends Specification implements InterceptorUnitTest<ApiTestingV2Interceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test apiTestingV2 interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"apiTestingV2")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
