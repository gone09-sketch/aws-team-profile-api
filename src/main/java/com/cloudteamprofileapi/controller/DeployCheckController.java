package com.cloudteamprofileapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DeployCheckController {

    @GetMapping("/api/deploy-check")
    public Map<String, String> deployCheck() {
        return Map.of(
                "status", "success",
                "message", "CI/CD deployment reflected",
                "version", "deploy-check-v1"
        );
    }
}
