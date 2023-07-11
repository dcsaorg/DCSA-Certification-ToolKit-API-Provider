package org.dcsa.api.provider.ctk.controller;

import org.dcsa.api.provider.ctk.CtkApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestartController {
    @GetMapping("/restart")
    public String restart() {
        CtkApplication.restart();
        return "PROVIDER CTK IS RESTARTED";
    }
}
