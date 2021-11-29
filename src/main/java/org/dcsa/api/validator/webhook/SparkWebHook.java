package org.dcsa.api.validator.webhook;

import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.model.CallbackContext;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

public class SparkWebHook {
    CallbackContext callbackContext;
    public SparkWebHook(CallbackContext callbackContext)
    {
        this.callbackContext=callbackContext;
    }

    public void startServer()
    {
        callbackContext.init();
        Spark.port(Configuration.CALLBACK_PORT);
        Spark.post("/webhook/callback/receive", (req, res) -> {
            res.status(200);
            res.header("Content-Type","application/json");
            callbackContext.setNotificationReceived(true);
            callbackContext.setNotificationBody(req.body());
            Map<String,String> headers=new HashMap<>();
            for(String header:req.headers())
            {
                headers.put(header,req.headers(header));
            }
            callbackContext.setHeaders(headers);
            callbackContext.getNotificationRequestLock().countDown();
            return "{\"status\":\"OK\"}";
        });

        Spark.head("/webhook/callback/reject", (req, res) -> {
            res.status(400);
            res.header("Content-Type","application/json");
            callbackContext.setHeadRequestReceived(true);
            callbackContext.getHeadRequestLock().countDown();
            return "Head request rejected!";
        });

        Spark.head("/webhook/callback/receive", (req, res) -> {
            System.out.println(Thread.currentThread().getName());
            res.status(200);
            res.header("Content-Type","application/json");
            callbackContext.setHeadRequestReceived(true);
            callbackContext.getHeadRequestLock().countDown();
            return "Head received!";
        });
        Spark.awaitInitialization();
        System.out.println("Spark Server started");
    }
    public  void stopServer(){
        Spark.stop();
        Spark.awaitStop();
        callbackContext.init();
    }
}
