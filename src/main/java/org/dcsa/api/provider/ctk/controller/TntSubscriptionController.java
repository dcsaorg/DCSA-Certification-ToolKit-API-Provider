package org.dcsa.api.provider.ctk.controller;

import org.dcsa.api.provider.ctk.util.TestUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TntSubscriptionController {

    @PostMapping("/callback/{uuid}")
    public ResponseEntity<?> handlePostNotification(@PathVariable String uuid) {
        System.out.println("POST NOTIFICATION RECEIVED");
        if (uuid.equals(TestUtility.getConfigCallbackUuid())) {
            System.out.println("Subscription callback POST allowed");
            return new ResponseEntity<>("Subscription callback allowed", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Subscription callback not allowed because mismatch uuid", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/callback/{uuid}", method = RequestMethod.HEAD)
    public ResponseEntity<?> handleHeadNotification(@PathVariable String uuid) {
        System.out.println("HEAD NOTIFICATION RECEIVED");
        if (uuid.equals(TestUtility.getConfigCallbackUuid())) {
            System.out.println("Subscription callback HEAD allowed");
            return new ResponseEntity<>("Subscription callback allowed", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Subscription callback not allowed because mismatch uuid", HttpStatus.FORBIDDEN);
        }
    }
}
