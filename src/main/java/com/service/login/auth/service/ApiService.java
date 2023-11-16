package com.service.login.auth.service;

import java.util.Map;

public interface ApiService {
    public Map<String, Object> fetchCountry();

    String fetchIPAddress();
     void executeAfterCommit(Runnable runnable);
}
