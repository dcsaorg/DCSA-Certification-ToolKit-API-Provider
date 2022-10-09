package org.dcsa.api.validator.webhook;

import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.model.TNTEventSubscriptionTO;
import org.dcsa.api.validator.util.TestUtility;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class SparkWebHook {
    CallbackContext callbackContext;

    public void startServer() {
        Spark.port(Configuration.CALLBACK_PORT);

        Spark.post(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
            TNTEventSubscriptionTO tntEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
            String uuid = tntEventSubscriptionTO.getCallbackUrl().substring(tntEventSubscriptionTO.getCallbackUrl().lastIndexOf("/")+1);
            res.status(201);
            if (req.params(":uuid").equals(uuid)) {
                res.header("Content-Type", "application/json");
                callbackContext.setNotificationReceived(true);
                callbackContext.setNotificationBody(req.body());
                Map<String, String> headers = new HashMap<>();
                for (String header : req.headers()) {
                    headers.put(header, req.headers(header));
                }
                callbackContext.setHeaders(headers);
                callbackContext.getNotificationRequestLock().countDown();
            }
            return "{\"status\":\"OK\"}";
        });

        Spark.head(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
            TNTEventSubscriptionTO tntEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
            String uuid = tntEventSubscriptionTO.getCallbackUrl().substring(tntEventSubscriptionTO.getCallbackUrl().lastIndexOf("/")+1);
            res.header("Content-Type", "application/json");
            if (req.params(":uuid").equals(uuid)) {
                res.status(201);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            else {
                res.status(400);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            return "{\"status\":\"OK\"}";
        });
        Spark.awaitInitialization();
        System.out.println("Server Started");
    }

    public void stopServer() {
        Spark.stop();
        Spark.awaitStop();
        if (callbackContext != null)
            callbackContext.init();
        System.out.println("Server Stopped");
    }

    public void setContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }
}
