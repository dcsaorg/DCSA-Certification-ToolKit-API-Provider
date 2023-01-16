package org.dcsa.api.validator.webhook;

import org.apache.http.HttpStatus;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.util.TestUtility;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class SparkWebHook {
    static CallbackContext callbackContext;

    public void startServer() {
        Spark.port(Configuration.CALLBACK_PORT);
        if(callbackContext == null){
            callbackContext = new CallbackContext();
        }

        Spark.post(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
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
            }else {
                System.out.println("Wrong UUID return 404");
                res.status(HttpStatus.SC_METHOD_NOT_ALLOWED);
            }
            return res;
        });

        Spark.head(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
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
        Spark.awaitInitialization();
        System.out.println("Spark notification callback server started");
    }

    public static void stopServer() {
        Spark.stop();
        Spark.awaitStop();
        if (callbackContext != null)
            callbackContext.init();
        System.out.println("Spark notification callback server Stopped");
    }

    public void setContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}
