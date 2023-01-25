package com.example.aluminumquiltphrase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import static org.springframework.http.HttpStatus.*;

// This class is to configure a retryable RestTemplate (in case of failures)
// Cited inspiration: https://www.baeldung.com/spring-retry
// Cited inspiration: https://shankulk.com/auto-retries-in-rest-api-clients-using-spring-retry-c78cacb0cc29
@Configuration
public class RetryConfig {

    @Value("${retry.maxAttempts}")
    private int maxAttempts;

    private final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(maxAttempts);
    private final NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();

    @Bean
    public RetryTemplate retryTemplate() {
        // Retry template using built in Spring features to retry on 400 and 500 errors
        RetryTemplate retryTemplate = new RetryTemplate();
        ExceptionClassifierRetryPolicy policy = new ExceptionClassifierRetryPolicy();
        policy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy());
        retryTemplate.setRetryPolicy(policy);
        return retryTemplate;
    }

    // Retries a maximum of maxAttempts
    private Classifier<Throwable, RetryPolicy> configureStatusCodeBasedRetryPolicy() {
        return throwable -> {
            if (throwable instanceof HttpStatusCodeException) {
                HttpStatusCodeException exception = (HttpStatusCodeException) throwable;
                return getRetryPolicyForStatus(exception.getStatusCode());
            }
            return simpleRetryPolicy;
        };
    }

    // Retry if 400 or 500
    private RetryPolicy getRetryPolicyForStatus(HttpStatusCode httpStatusCode) {
        if (httpStatusCode.isError()){
            return simpleRetryPolicy;
        }
        return neverRetryPolicy;
    }

}
