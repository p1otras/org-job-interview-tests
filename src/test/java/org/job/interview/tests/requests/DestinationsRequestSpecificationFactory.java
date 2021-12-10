package org.job.interview.tests.requests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class DestinationsRequestSpecificationFactory {

    public static RequestSpecification destinations(String apiId, String apiKey) {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.schiphol.nl/public-flights/destinations")
                .addHeader("app_id", apiId)
                .addHeader("app_key", apiKey)
                .addHeader("ResourceVersion", "v4")
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .build();
    }

    public static RequestSpecification destinationsPage(String apiId, String apiKey, int id) {
        return new RequestSpecBuilder()
                .addRequestSpecification(destinations(apiId, apiKey))
                .addQueryParam("page", id)
                .build();
    }
}
