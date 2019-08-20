package com.dynali;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class DynaliClient {
    private static final String ENDPOINT_LIVE = "https://api.dynali.net/nice/";
    private static final String ENDPOINT_DEBUG = "https://debug.dynali.net/nice/";

    public static String retrieveMyIP() throws IOException, DynaliException {
        StringEntity postdata = new StringEntity("{\"action\":\"myip\"}");
        String serializedResponse = call(postdata, false);
        ObjectMapper mapper = new ObjectMapper();
        MyIPResponse deserializedResponse = mapper.readValue(serializedResponse, MyIPResponse.class);
        if (deserializedResponse.getCode() == 200) {
            return deserializedResponse.getData().getIp();
        }
        throw new DynaliException(deserializedResponse.getCode(), deserializedResponse.getMessage());
    }

    private static String call(StringEntity params, boolean useDevEnv) throws IOException {
        HttpPost request = prepareRequest(params, useDevEnv);
        HttpResponse response = handleRequest(request);
        return new BasicResponseHandler().handleResponse(response);
    }

    private static CompletableFuture<String> callASync(StringEntity params, boolean useDevEnv) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return call(params, useDevEnv);
                    } catch (IOException e) {
                        return "failed";
                    }
                }
        );
    }

    private static HttpPost prepareRequest(StringEntity params, boolean useDevEnv) {
        HttpPost request = chooseURL(useDevEnv);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        return request;
    }

    private static HttpPost chooseURL(boolean useDevEnv) {
        return new HttpPost(useDevEnv ? ENDPOINT_DEBUG : ENDPOINT_LIVE);
    }

    private static HttpResponse handleRequest(HttpPost request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

    public static void main(String[] args) throws IOException, DynaliException, InterruptedException {
//        System.out.println(retrieveMyIP());
        callASync(new StringEntity("{\"action\":\"myip\"}"), false).thenAcceptAsync((resultValue) -> {
                    System.out.println(resultValue);
                }
        );
        System.out.println("This message should be before the response");
        while (true) {
            System.out.println("running in fg...");
            sleep(1000);
        }
    }

}