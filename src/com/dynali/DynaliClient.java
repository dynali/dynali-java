package com.dynali;

import com.dynali.action.DynaliAction;
import com.dynali.action.DynaliActionDefault;
import com.dynali.action.MyIPAction;
import com.dynali.response.MyIPResponse;
import com.dynali.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class DynaliClient {
    private static final String ENDPOINT_LIVE = "https://api.dynali.net/nice/";
    private static final String ENDPOINT_DEBUG = "https://debug.dynali.net/nice/";
    private DynaliEnvironmentTarget DynaliEnvironment = DynaliEnvironmentTarget.LIVE;

    public enum DynaliEnvironmentTarget {DEBUG, LIVE}

    public String retrieveMyIP() throws IOException, DynaliException {
        MyIPResponse deserializedResponse = (MyIPResponse) executeAction(new MyIPAction(), MyIPResponse.class);
        if (isResponseSuccessful(deserializedResponse.getCode())) {
            return deserializedResponse.getData().getIp();
        }
        throw new DynaliException(deserializedResponse.getCode(), deserializedResponse.getMessage());
    }

    private <R extends Response> Response executeAction(DynaliAction action, Class<R> responseClass) throws IOException, DynaliException {
        String serializedResponse = call(action);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(serializedResponse, responseClass);
    }

    private <R extends Response> CompletableFuture<Response> executeActionASync(DynaliAction action, Class<R> responseClass) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return executeAction(action, responseClass);
                    } catch (IOException | DynaliException e) {
                        return new MyIPResponse(); //TODO: clumsy
                    }
                }
        );
        }

    private String call(DynaliAction action) throws IOException, DynaliException {
        validateAction(action);
        HttpPost request = prepareRequest(action);
        HttpResponse response = handleRequest(request);
        return new BasicResponseHandler().handleResponse(response);
    }

    private CompletableFuture<String> callASync(DynaliAction action) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return call(action);
                    } catch (IOException | DynaliException e) {
                        return "failed";
                    }
                }
        );
    }

    private void validateAction(DynaliAction action) {
        List<String> validationErrors = action.getValidationErrors();
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", validationErrors));
        }
    }

    private HttpPost prepareRequest(DynaliAction action) throws DynaliException {
        HttpPost request = chooseURL();
        request.addHeader("content-type", "application/json");
        StringEntity params = action.toJson();
        request.setEntity(params);
        return request;
    }

    private HttpPost chooseURL() {
        return new HttpPost(DynaliEnvironment == DynaliEnvironmentTarget.DEBUG ? ENDPOINT_DEBUG : ENDPOINT_LIVE);
    }

    private HttpResponse handleRequest(HttpPost request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

    private boolean isResponseSuccessful(int responseCode) {
        return responseCode != 200;
    }

    public void main(String[] args) throws IOException, DynaliException, InterruptedException {
//        System.out.println(retrieveMyIP());
        callASync(new StringEntity("{\"action\":\"myip\"}")).thenAcceptAsync((resultValue) -> {
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