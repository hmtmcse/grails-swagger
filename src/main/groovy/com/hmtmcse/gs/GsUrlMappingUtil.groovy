package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsApiVersionActionsData
import com.hmtmcse.gs.data.GsControllerActionData
import grails.util.Holders
import org.grails.core.DefaultGrailsControllerClass

import java.util.regex.Matcher
import java.util.regex.Pattern

class GsUrlMappingUtil {

    static List<GsControllerActionData> getUrlMappingData() {
        List<GsControllerActionData> gsControllerActionDataList = new ArrayList<>()
        GsControllerActionData gsControllerActionData
        Holders.grailsApplication.controllerClasses.each{ DefaultGrailsControllerClass controller ->
            if (controller.name.startsWith(GsConfigService.controllerStartWith())){
                println(controller.name)
                println(controller.actions)
                gsControllerActionData =  processControllerActionRegex(controller)
                if (gsControllerActionData){
                    gsControllerActionDataList.add(gsControllerActionData)
                }
            }
        }
        return gsControllerActionDataList
    }

    static GsControllerActionData processControllerActionRegex(DefaultGrailsControllerClass controller){
        Pattern pattern = Pattern.compile(GsConfigService.controllerStartWith() + "(\\w+)(V(\\d+))")
        Matcher matcher = pattern.matcher(controller.name)
        GsControllerActionData gsControllerActionData = null
        GsAction gsAction
        if (matcher.find()){
            if (matcher.groupCount()  == 3){
                gsControllerActionData = new GsControllerActionData()
                gsControllerActionData.controllerName = controller.name
                gsControllerActionData.url = matcher.group(1)
                gsControllerActionData.apiVersion = matcher.group(2)
                controller.actions.each { String action ->
                    println(action)
                    if (action.startsWith(GsConstant.GET)){
                        gsAction = parseAction(action, GsConstant.GET)
                    }else if (action.startsWith(GsConstant.POST)){
                        gsAction = parseAction(action, GsConstant.POST)
                    }else  if (action.startsWith(GsConstant.DELETE)){
                        gsAction = parseAction(action, GsConstant.DELETE)
                    }else if (action.startsWith(GsConstant.PUT)){
                        gsAction = parseAction(action, GsConstant.PUT)
                    }else if (action.startsWith(GsConstant.HEAD)){
                        gsAction = parseAction(action, GsConstant.HEAD)
                    }else{
                        gsAction = null
                    }
                    if (gsAction){
                        gsControllerActionData.addAction(gsAction)
                    }
                }
            }
        }
        return gsControllerActionData
    }

    static GsAction parseAction(String actionName, String method){
        GsAction gsAction = null
        Pattern pattern = Pattern.compile(method + "([A-Z]\\w+)")
        Matcher matcher = pattern.matcher(actionName)
        if (matcher.find() && matcher.groupCount() == 1){
            gsAction = new GsAction()
            gsAction.httpMethod = method
            gsAction.name = matcher.group(1)
        }
        return gsAction
    }

}
