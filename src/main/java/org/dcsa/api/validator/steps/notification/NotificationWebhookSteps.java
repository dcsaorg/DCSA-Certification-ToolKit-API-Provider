package org.dcsa.api.validator.steps.notification;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dcsa.api.validator.config.Configuration;
import org.dcsa.api.validator.hooks.TestSetup;
import org.dcsa.api.validator.model.CallbackContext;
import org.dcsa.api.validator.model.TestContext;
import org.dcsa.api.validator.util.JsonUtility;
import org.dcsa.api.validator.util.TestUtility;
import org.dcsa.api.validator.webhook.SparkWebHook;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class NotificationWebhookSteps {
    private CallbackContext callbackContext;
    private SparkWebHook webhook;
    private Scenario scenario;

    public NotificationWebhookSteps() {
        callbackContext = new CallbackContext();
    }

    @Before(order = 2)
    public void setUp(Scenario s) {
        callbackContext = new CallbackContext();
        webhook = new SparkWebHook(callbackContext);
        webhook.startServer();
        this.scenario = s;
        if (TestSetup.TestContexts.get(scenario.getId()) == null)
            TestSetup.TestContexts.put(scenario.getId(), new TestContext());
    }

    @Given("Start Webhook server")
    public void startMockServerAPIServer() {
/*        callbackContext = new CallbackContext();
        webhook = new SparkWebHook(callbackContext);
        webhook.startServer();*/
    }


    @Then("Receive Head request for CallBackURL")
    public void receiveHeadRequestForCallBackURL() {
        try {
            System.out.println("Waiting for callback head request ");
            callbackContext.getHeadRequestLock().await(Configuration.CALLBACK_WAIT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!callbackContext.isHeadRequestReceived()) {
            TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
            testcontext.setReasonOfFailure("Head Request not received");
            Assert.fail("Head Request not received");
        }
    }

    @And("Receive a valid notification")
    public void receiveAValidNotification() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        try {
            System.out.println("Waiting for Notification");
            callbackContext.getNotificationRequestLock().await(Configuration.CALLBACK_WAIT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!callbackContext.isNotificationReceived()) {
            testcontext.setReasonOfFailure("Notification not Received");
            Assert.fail("Notification not Received");
        }
        testcontext.getMessage().add("Notification Received \n");
        testcontext.getMessage().add(callbackContext.getHeaders().toString());
        String receivedSignature = callbackContext.getHeaders().get("Notification-Signature");
        if (receivedSignature == null) {
            testcontext.setReasonOfFailure("Notification-Signature missing in request header");
            Assert.fail("Notification-Signature missing in request header");
        }
        testcontext.getMessage().add(callbackContext.getNotificationBody());
        String key = "MTIzNDU2Nzg5MGFiY2RlZjEyMzQ1Njc4OTBhYmNkZWY=";
        if (testcontext.getRequestChain().size() > 0)
            key = JsonUtility.extractAttributeValue(testcontext.getRequestChain().get(testcontext.getRequestChain().size() - 1).getBody(), "secret");
        String expectedSignature = (TestUtility.getSignature(key, callbackContext.getNotificationBody()));
        if (!expectedSignature.equals(receivedSignature)) {
            testcontext.setReasonOfFailure("Invalid Signature: Expected=>" + expectedSignature + "\n\n received=>" + receivedSignature);
        }
        Assert.assertEquals(expectedSignature, receivedSignature);
    }

    @And("A valid Callback Url")
    public void aValidCallbackUrl() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        testcontext.setCallbackURL(Configuration.CALLBACK_URI + "/webhook/callback/receive");
    }

    @And("An invalid Callback Url")
    public void aInvalidCallbackUrl() {
        TestContext testcontext = TestSetup.TestContexts.get(scenario.getId());
        testcontext.setCallbackURL(Configuration.CALLBACK_URI + "/webhook/callback/reject");
    }

    @After(order = 1)
    public void cleanUp(Scenario s) {
        webhook.stopServer();
    }


}
