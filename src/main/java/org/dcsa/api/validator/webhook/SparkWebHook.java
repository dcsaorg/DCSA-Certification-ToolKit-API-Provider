package org.dcsa.api.validator.webhook;

import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.models.CallbackContext;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class SparkWebHook {
    CallbackContext callbackContext;

    public void startServer() {
        // callbackContext.init();
        System.out.println("Server Started");
        Spark.port(Configuration.CALLBACK_PORT);

        Spark.post(Configuration.CALLBACK_PATH+"/:uuid", (req, res) -> {
            res.status(200);
            if (req.params(":uuid").equals("456eacf9-8cda-412b-b801-4a41be7a6c35")) {
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

            res.header("Content-Type", "application/json");
            if (req.params(":uuid").equals("456eacf9-8cda-412b-b801-4a41be7a6c35")) {
                res.status(200);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            else if (req.params(":uuid").equals("307deecf-e599-4ff2-bf5a-fd47c171b8c4")) {
                res.status(400);
                callbackContext.setHeadRequestReceived(true);
                callbackContext.getHeadRequestLock().countDown();
            }
            return "Head received!";
        });
        Spark.awaitInitialization();
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
