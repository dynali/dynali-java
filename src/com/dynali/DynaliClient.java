package com.dynali;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class DynaliClient {
    private static final String ENDPOINT_LIVE = "https://api.dynali.net/nice/";
    private static final String ENDPOINT_DEBUG = "https://debug.dynali.net/nice/";
    private static final String POSTDATA_MYIP = "{\"action\":\"myip\"}";

    public static String fetchMyIP() throws IOException, DynaliException {
        var postdata = new StringEntity(POSTDATA_MYIP);
        String serializedResponse = fetchResponse(postdata, false);
        var mapper = new ObjectMapper();
        MyIPResponse deserializedResponse = mapper.readValue(serializedResponse, MyIPResponse.class);
        if (deserializedResponse.getCode() == 200)
            return deserializedResponse.getData().getIp();
        else throw new DynaliException(deserializedResponse.getCode(), deserializedResponse.getMessage());
    }

    private static String fetchResponse(StringEntity params, boolean devEnv) throws IOException {
        HttpPost request = prepareRequest(params, devEnv);
        HttpResponse response = handleRequest(request);
        return new BasicResponseHandler().handleResponse(response);
    }

    private static HttpPost prepareRequest(StringEntity params, boolean devEnv) {
        HttpPost request = chooseURL(devEnv);
        request.addHeader("content-type", "application/x-www-form-urlencoded");
        request.setEntity(params);
        return request;
    }

    private static HttpPost chooseURL(boolean devEnv) {
        return new HttpPost(devEnv ? ENDPOINT_DEBUG : ENDPOINT_LIVE);
    }

    private static HttpResponse handleRequest(HttpPost request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

    public static void main(String[] args) throws IOException, DynaliException {
        System.out.println(fetchMyIP());
    }


}