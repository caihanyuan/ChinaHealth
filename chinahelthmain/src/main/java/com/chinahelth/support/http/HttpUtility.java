package com.chinahelth.support.http;

import java.util.Map;

public class HttpUtility {

    private static HttpUtility httpUtility = new HttpUtility();

    private HttpUtility() {
    }

    public static HttpUtility getInstance() {
        return httpUtility;
    }

    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param) {
        return new JavaHttpUtility().executeNormalTask(httpMethod, url, param);
    }
}

