package com.service.login.auth.service;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Service
public class ApiServiceImpl implements ApiService {


    private RestTemplate restTemplate;



    @Override
    public Map<String, Object> fetchCountry() {
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("ip", fetchIPAddress());

            ResponseEntity<String> response = restTemplate.postForEntity("COUNTRY_CURRENCY_URL", requestParams, String.class);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                return new ObjectMapper().readValue(response.getBody(), HashMap.class);
            } else {
                return new HashMap<>();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new HashMap<>();
        }
    }

    public String fetchIPAddress() {
        // Retrieve the current request attributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRemoteAddr();
        }

        return null; // or throw an exception, depending on your error handling strategy
    }

    @Transactional
    public void executeAfterCommit(Runnable runnable) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            public void afterCommit() {
                runnable.run();
            }
        });
    }



}
