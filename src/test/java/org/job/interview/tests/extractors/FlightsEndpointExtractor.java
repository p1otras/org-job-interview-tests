package org.job.interview.tests.extractors;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.job.interview.tests.models.Flight;
import org.job.interview.tests.models.Flights;
import org.job.interview.tests.requests.FlightsRequestSpecificationFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor(staticName = "of")
public class FlightsEndpointExtractor {

    private static final int SECOND_PAGE = 2;
    private final String apiId;
    private final String apiKey;

    public List<Flight> extractTodayFlights() {
        Response response = given()
                .log()
                .ifValidationFails()
                .spec(FlightsRequestSpecificationFactory.todayFlights(apiId, apiKey))
                .when()
                .get()
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        String linksHeader = response.header("link");
        int pagesCount = PaginationRetriver.pagesCount(linksHeader);
        List<Flight> flights = Collections.synchronizedList(new ArrayList<>(response.body()
                .as(Flights.class)
                .getFlights()));
        if (pagesCount > 1) {
            IntStream.range(SECOND_PAGE, pagesCount + 1)
                    .parallel()
                    .boxed()
                    .forEach(page -> flights.addAll(extractPageFlights(new Date(), page)));
        }
        return flights;
    }

    private List<Flight> extractPageFlights(Date data, int page) {
        return given()
                .log()
                .ifValidationFails()
                .spec(FlightsRequestSpecificationFactory.getPageFlights(apiId, apiKey, data, page))
                .get()
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .response()
                .as(Flights.class)
                .getFlights();
    }
}
