package org.rliz.cfm.recorder.common.rest

import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

fun httpRequestFactory(timeout: Int = 3000): ClientHttpRequestFactory {
    val requestFactory = HttpComponentsClientHttpRequestFactory()
    requestFactory.setConnectTimeout(timeout)
    return requestFactory
}

fun restCall(requestFactory: ClientHttpRequestFactory = httpRequestFactory()): RestTemplate =
        RestTemplate(requestFactory)
