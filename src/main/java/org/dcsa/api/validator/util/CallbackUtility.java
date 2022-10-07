package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Log
public class CallbackUtility {

    public static RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        customRequestFactory.setHttpClient(httpClient);
        return new RestTemplate(customRequestFactory);
    }

    public static boolean performHttpHead(String callbackUrl){
        boolean result;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("HEAD NOTIFICATION SENT");
        try {
            HttpStatus statusCode = getRestTemplate().exchange(callbackUrl, HttpMethod.HEAD, entity, String.class)
                    .getStatusCode();
            log.info("GOT HEAD RESPONSE STATUS: " + statusCode);
            result = true;
        } catch (Exception e) {
            log.warning("THE CALLBACK URL RESPONDED "+e.getMessage().toUpperCase());
            result = false;
        }
        return result;
    }

    public static boolean performPostRequest(String notificationBody, String callbackUrl){
        boolean result = false;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(notificationBody,headers);
        log.info("POST NOTIFICATION SENT");
        try {
            HttpStatus statusCode=getRestTemplate().exchange(callbackUrl, HttpMethod.POST,entity,String.class).getStatusCode();
            if(statusCode == HttpStatus.CREATED) {
                result = true;
                log.info("NOTIFICATION RECEIVED RESPONSE: "+statusCode);
            }else if( statusCode == HttpStatus.METHOD_NOT_ALLOWED){
                result = false;
                log.warning("ERROR NOTIFICATION RECEIVED RESPONSE: "+statusCode);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

}
