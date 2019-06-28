package com.hmtmcse.gs

import com.hmtmcse.caa.jwt.JavaJWT

import javax.servlet.http.HttpServletRequest


class JwtAuthService {

    def getToken(String issuer, Integer expireInMinutes = 30, Map claims = [:]) {
        JavaJWT javaJWT = JavaJWT.hmackInstance(JavaJWT.ALGORITHM.HMAC256, GsConfigHolder.JWT_HMAC_SECRET).tokenValidUntilUTCMinutes(expireInMinutes)
        if (claims){
            claims.each {String key, def value ->
                javaJWT.privateClaims(key, value)
            }
        }
        return javaJWT.token(issuer)
    }


    def isValidToken(String token) {
        JavaJWT javaJWT = JavaJWT.hmackInstance(JavaJWT.ALGORITHM.HMAC256, GsConfigHolder.JWT_HMAC_SECRET)
        try {
            javaJWT.tokenValidate(token)
            return true
        }catch(Exception e){
            return false
        }
    }


    def isAuthenticated(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization")
        if (!authorizationHeader || !authorizationHeader.startsWith("Bearer")){
            return false
        }
        String[] splittedBearer = authorizationHeader.split("Bearer")
        if (splittedBearer.length < 2){
            return false
        }
        return isValidToken(splittedBearer[1].trim())
    }


}
