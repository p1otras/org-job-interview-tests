package org.job.interview.tests.requests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightsRequestSpecificationFactory {

    public static RequestSpecification todayFlights(String apiId, String apiKey) {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.schiphol.nl/public-flights/flights")
                .addHeader("app_id", apiId)
                .addHeader("app_key", apiKey)
                .addHeader("ResourceVersion", "v4")
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .build();
    }

    public static RequestSpecification getPageFlights(String apiId, String apiKey, Date date, int id) {
        return new RequestSpecBuilder()
                .addRequestSpecification(todayFlights(apiId, apiKey))
                .addHeader("scheduleDate", new SimpleDateFormat("yyyy-MM-dd").format(date))
                .addQueryParam("page", id)
                .build();
    }
}
