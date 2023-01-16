package org.dcsa.api.validator.webservice.controller;

import org.dcsa.api.validator.webhook.SparkWebHook;
import org.dcsa.api.validator.webservice.ApiProviderApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartController {

    @GetMapping("/restart")
    public String restart() {
        SparkWebHook.stopServer();
        ApiProviderApplication.restart();
        return "PROVIDER CTK IS RESTARTED";
    }
}
