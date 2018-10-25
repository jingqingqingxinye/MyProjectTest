package com.example.mysdkforandroid.okhttp.okhttp.request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jing on 2018/10/17.
 * @function 封装所有的请求到一个HashMap中
 */

public class RequestParams {

  public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
  public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<>();

  public RequestParams() {
    this((Map<String, String>) null);
  }

  public RequestParams(Map<String, String> source) {
    if (source != null) {
      for (Map.Entry<String, String> entry : source.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
    }

  }

  public RequestParams(final String key, final String value) {
    this(new HashMap<String, String>() {
      {
        put(key, value);
      }
    });
  }

  public void put(String key, String value) {
    if (key != null) {
      fileParams.put(key, value);
    }
  }

  public boolean hasParams() {
    if (urlParams.size() > 0 || fileParams.size() > 0) {
      return true;
    }
    return false;
  }
}
