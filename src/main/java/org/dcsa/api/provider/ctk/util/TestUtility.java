package org.dcsa.api.provider.ctk.util;


import org.dcsa.api.provider.ctk.init.AppProperty;
import org.dcsa.api.provider.ctk.model.TNTEventSubscriptionTO;
import org.dcsa.api.provider.ctk.model.enums.OsType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class TestUtility {

    private static OsType osType;
    public static String NEWMAN_TICK = "√";
    public static String getConfigCallbackUuid() {
        TNTEventSubscriptionTO configTNTEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
        String[] splitStr = configTNTEventSubscriptionTO.getCallbackUrl().split("/");
        String uuid = "";
        if (splitStr.length > 1) {
            uuid = splitStr[splitStr.length - 1];
        }
        return uuid;
    }

    public static String getConfigCallbackUrl() {
        TNTEventSubscriptionTO configTNTEventSubscriptionTO = TestUtility.getConfigTNTEventSubscriptionTO();
        return configTNTEventSubscriptionTO.getCallbackUrl();
    }

    public static String getConfigTNTEventSubscriptionTOStr() {
        String eventSubscriptionJson = FileUtility.loadFileAsString(new FileSystemResource("").getFile().getAbsolutePath() + File.separator + AppProperty.EVENT_PATH);
        return eventSubscriptionJson;
    }

    public static TNTEventSubscriptionTO getConfigTNTEventSubscriptionTO() {
        String eventSubscriptionJson = FileUtility.loadFileAsString(new FileSystemResource("").getFile().getAbsolutePath() + File.separator + AppProperty.EVENT_PATH);
        TNTEventSubscriptionTO tntEventSubscriptionTO = JsonUtility.getObjectFromJson(TNTEventSubscriptionTO.class, eventSubscriptionJson);
        return tntEventSubscriptionTO;
    }

    public static String getConfigEventSubscriptionJson() {
        return FileUtility.loadFileAsString(new FileSystemResource("").getFile().getAbsolutePath() + File.separator + AppProperty.EVENT_PATH);
    }

    public static OsType getOperatingSystem() {
        String os = System.getProperty("os.name");
        OsType osType = OsType.WINDOWS;
        if (os.contains("Win")) {
            osType = OsType.WINDOWS;
        } else if (os.contains("Linux")) {
            osType = OsType.LINUX;
        }
        if (isRunningInsideDocker()) {
            osType = OsType.DOCKER;
        }
        System.out.println("Using execution system: " + osType.name());
        return osType;
    }

    public static boolean isRunningInsideDocker() {
        if(Files.exists(Path.of("/.dockerenv"))){
            return true;
        }else{
            return false;
        }
    }

    public static RestTemplate getRestTemplate() {
        RestTemplate restClient = new RestTemplate(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        // Add one interceptor like in your example, except using anonymous class.
        restClient.setInterceptors(Collections.singletonList((request, body, execution) -> execution.execute(request, body)));

        return restClient;
    }

    public static void setOsType(OsType osType) {
        TestUtility.osType = osType;
        if (osType == OsType.WINDOWS) {
            NEWMAN_TICK = "√";
        } else if (osType == OsType.LINUX) {
            NEWMAN_TICK = "✓";
        } else if (osType == OsType.DOCKER) {
            NEWMAN_TICK = "���";
        }
    }
}
