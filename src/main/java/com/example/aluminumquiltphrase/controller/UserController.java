package com.example.aluminumquiltphrase.controller;


import com.example.aluminumquiltphrase.entity.UserInfo;
import com.example.aluminumquiltphrase.service.UserService;
import com.google.common.base.Throwables;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private LoadingCache<String, UserInfo> loadingCache;


    // Calls UserService based to retrieve user info provided in the URL path
    @GetMapping("/username/{username}")
    public ResponseEntity<UserInfo> getUserInfoByUsername(@PathVariable String username) {
        UserInfo userInfo = null;
        try {
            userInfo = loadingCache.get(username.toLowerCase());
        } catch (Exception e) {
            if (e.getCause() instanceof HttpClientErrorException.NotFound) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userInfo);
    }
}
