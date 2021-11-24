package org.dcsa.api.validator.steps;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webhook.SparkWebHook;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.concurrent.TimeUnit;

public class NotificationWebhookSteps {
    private CallbackContext callbackContext;
    private SparkWebHook webhook;

    public NotificationWebhookSteps() {
        callbackContext = new CallbackContext();
    }

    @Given("Start Webhook server")
    public void startMockServerAPIServer() {
        callbackContext=new CallbackContext();
        webhook =new SparkWebHook(callbackContext);
        webhook.startServer();
    }


    @Then("Receive Head request for CallBackURL")
    public void receiveHeadRequestForCallBackURL() {
       ITestResult i= Reporter.getCurrentTestResult();
        try {
            System.out.println("Waiting for callback head request ");
            callbackContext.getHeadRequestLock().await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!callbackContext.isHeadRequestReceived())
            Assert.fail("Head Request not received");
    }

    @And("Receive a valid notification")
    public void receiveAValidNotification() {
        try {
            System.out.println("Waiting for Notification");
            callbackContext.getNotificationRequestLock().await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!callbackContext.isNotificationReceived()) {
            Assert.fail("Notification not Received");
        }
        String receivedSignature =  callbackContext.getHeaders().get("Notification-Signature");
        if (receivedSignature == null)
           Assert.fail("Notification-Signature missing in request header");
       String expectedSignature=(TestUtility.getSignature("MTIzNDU2Nzg5MGFiY2RlZjEyMzQ1Njc4OTBhYmNkZWY=", callbackContext.getCallBackBody()));
        Assert.assertEquals(expectedSignature, receivedSignature);
    }

    @After
    public void cleanUp() {
        webhook.stopServer();
    }


}
