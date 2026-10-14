package com.example.offline_firstfieldoperationsapp.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {

    private var authToken: String? = null

    fun setAuthToken(token : String?){
        this.authToken = token
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.getAuthToken()

        val requestBuilder = originalRequest.newBuilder()
        if(!token.isNullOrEmpty()){
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        val response = chain.proceed(requestBuilder.build())

        if(response.code == 401){
            tokenManager.clearSession()
        }
        return response
    }
}