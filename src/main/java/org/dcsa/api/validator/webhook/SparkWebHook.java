package org.dcsa.api.validator.webhook;

import org.apache.http.HttpStatus;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.util.TestUtility;
import spark.Service;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Service.ignite;

public class SparkWebHook {
    static CallbackContext callbackContext;

    static boolean isNotificationSent;
    static Service http;
    public void startServer() {
        http = ignite()
                .port(Configuration.CALLBACK_PORT)
                .threadPool(20);

        if(callbackContext == null){
            callbackContext = new CallbackContext();
        }

        http.post(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
            res.status(HttpStatus.SC_NO_CONTENT);
            System.out.println("POST NOTIFICATION RECEIVED");
            if (req.params(":uuid").equals(TestUtility.getConfigCallbackUuid())) {
                System.out.println("POST NOTIFICATION RECEIVED WITH UUID: "+req.params(":uuid"));
                res.header("Content-Type", "application/json");
                callbackContext.setNotificationReceived(true);
                callbackContext.setNotificationBody(req.body());
                Map<String, String> headers = new HashMap<>();
                for (String header : req.headers()) {
                    headers.put(header, req.headers(header));
                }
                callbackContext.setHeaders(headers);
                callbackContext.getNotificationRequestLock().countDown();
                isNotificationSent = true;
                res.body("Testing");
            }else {
                System.out.println("Wrong UUID return 404");
                res.status(HttpStatus.SC_METHOD_NOT_ALLOWED);
            }
            return res;
        });

        http.head(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
            System.out.println("HEAD NOTIFICATION RECEIVED");
            res.header("Content-Type", "application/json");
            if (req.params(":uuid").equals(TestUtility.getConfigCallbackUuid())) {
                System.out.println("HEAD NOTIFICATION RECEIVED WITH UUID: "+req.params(":uuid"));
                res.status(HttpStatus.SC_NO_CONTENT);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            else {
                res.status(HttpStatus.SC_METHOD_NOT_ALLOWED);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            return res;
        });

        http.get("/notificationSent", (req, res) -> isNotificationSent);

        http.awaitInitialization();

        System.out.println("Spark notification callback server started and listing on "+Configuration.CALLBACK_PORT);
    }

    public static void stopServer() {
        http.stop();
        if (callbackContext != null)
            callbackContext.init();
        System.out.println("Spark notification callback server Stopped");
    }

    public void setContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

}
