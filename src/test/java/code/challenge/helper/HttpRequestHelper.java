package code.challenge.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class HttpRequestHelper {

    public Response post(String url, Object requestParams){
        RestAssured.baseURI = url;
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(requestParams);

        return httpRequest.post();
    }

    public Response get(String url){
        RestAssured.baseURI = url;
        RequestSpecification httpRequest = RestAssured.given();

        return httpRequest.get();
    }

    public Response delete(String url){
        RestAssured.baseURI = url;
        RequestSpecification httpRequest = RestAssured.given();

        return httpRequest.delete();
    }
}
