package com.wukong.provider.config.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Component
@Slf4j
public class RestConnection {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    /**
     * post
     * @param url
     * @param entity
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity entity, Class<T> responseType) {
        ResponseEntity<T> responseEntity =  restTemplate.postForEntity(restConfig.accessUrl(url), createHttpEntity(entity), responseType);
        return responseEntity;
    }

    /**
     * post
     * @param url
     * @param entity
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity entity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restConfig.accessUrl(url), HttpMethod.PUT, createHttpEntity(entity), responseType);
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restConfig.accessUrl(url), HttpMethod.GET, createHttpEntity(null), responseType);
    }


    public <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restConfig.accessUrl(url), HttpMethod.DELETE, createHttpEntity(null), responseType);
    }

    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restConfig.accessUrl(url), HttpMethod.DELETE, createHttpEntity(requestEntity), responseType);
    }

    /**
     * 通用header构建
     * @return
     */
    public HttpHeaders commonHeader() {
        return createHeader(MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    public HttpHeaders createHeader(String contentType, String accept) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        headers.add(HttpHeaders.ACCEPT, accept);
        return headers;
    }

    /**
     * 产生带token的请求头
     * @param entity
     * @return
     */
    private HttpEntity createHttpEntity(HttpEntity entity) {
        HttpHeaders headers = commonHeader();
        if (entity == null) {
            return new HttpEntity(headers);
        }
        return new HttpEntity(entity.getBody(), headers);
    }
}
