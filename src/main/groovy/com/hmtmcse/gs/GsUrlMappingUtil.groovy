package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsControllerActionData
import grails.util.Environment
import grails.util.Holders
import org.grails.core.DefaultGrailsControllerClass
import java.util.regex.Matcher
import java.util.regex.Pattern

class GsUrlMappingUtil {

    private static List<GsControllerActionData> gsUrlMappingHolder = new ArrayList<>()

    static List<GsControllerActionData> getUrlMappingData() {
        if (gsUrlMappingHolder.size() == 0 || Environment.PRODUCTION != Environment.current){
            gsUrlMappingHolder = new ArrayList<>()
            GsControllerActionData gsControllerActionData
            Holders.grailsApplication.controllerClasses.each{ DefaultGrailsControllerClass controller ->
                if (controller.name.startsWith(GsConfigHolder.controllerStartWith())){
                    gsControllerActionData =  processControllerActionRegex(controller)
                    if (gsControllerActionData){
                        gsControllerActionData.setActions(gsControllerActionData.actions.reverse())
                        gsControllerActionData.controllerClass = controller
                        gsControllerActionData.packageName = controller.packageName
                        gsControllerActionData.relativeURL = "/${apiPrefix()}/${gsControllerActionData.apiVersion}/${gsControllerActionData.url}/"
                        gsUrlMappingHolder.add(gsControllerActionData)
                    }
                }
            }
        }
        return gsUrlMappingHolder
    }

    static String apiPrefix(){
        return GsConfigHolder.controllerStartWith()?.uncapitalize()
    }

    private static GsControllerActionData processControllerActionRegex(DefaultGrailsControllerClass controller){
        Pattern pattern = Pattern.compile(GsConfigHolder.controllerStartWith() + "(\\w+)(V(\\d+))")
        Matcher matcher = pattern.matcher(controller.name)
        GsControllerActionData gsControllerActionData = null
        GsAction gsAction
        if (matcher.find()){
            if (matcher.groupCount()  == 3){
                gsControllerActionData = new GsControllerActionData()
                gsControllerActionData.controllerName = controller.name?.uncapitalize()
                gsControllerActionData.controllerRealName = controller.name
                gsControllerActionData.url = matcher.group(1)?.uncapitalize()
                gsControllerActionData.controllerUrlName = matcher.group(1)?.uncapitalize()
                gsControllerActionData.url = controllerCustomUrl(gsControllerActionData.url)
                gsControllerActionData.apiVersion = matcher.group(2)?.uncapitalize()
                controller.actions.each { String action ->
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


    private static String controllerCustomUrl(String controller) {
        if (GsConfigHolder.controllerCustomUrlRegex() != null) {
            Pattern pattern = Pattern.compile(GsConfigHolder.controllerCustomUrlRegex())
            Matcher matcher = pattern.matcher(controller)
            String url = ""
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    url += matcher.group(i)?.uncapitalize() + "/"
                }
            }
            return !url.equals("") ? GsUtil.removeLastChar(url, "/") : controller
        }
        return controller
    }

    private static GsAction parseAction(String actionName, String method){
        GsAction gsAction = null
        Pattern pattern = Pattern.compile(method + "([A-Z]\\w+)")
        Matcher matcher = pattern.matcher(actionName)
        if (matcher.find() && matcher.groupCount() == 1){
            gsAction = new GsAction()
            gsAction.httpMethod = method
            gsAction.actionRealName = actionName
            gsAction.name = matcher.group(1)?.uncapitalize()
        }
        return gsAction
    }

}
