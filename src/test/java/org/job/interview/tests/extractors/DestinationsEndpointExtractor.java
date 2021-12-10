package org.job.interview.tests.extractors;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.job.interview.tests.models.Departures;
import org.job.interview.tests.models.Destination;
import org.job.interview.tests.requests.DestinationsRequestSpecificationFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor(staticName = "of")
public class DestinationsEndpointExtractor {

    private static final int SECOND_PAGE = 2;
    private final String apiId;
    private final String apiKey;

    public <T> List<T> extractData(Function<Destination, T> recordMapper) {
        return extractData(destination -> true, recordMapper);
    }

    public <T> List<T> extractData(Predicate<? super Destination> recordFilter, Function<Destination, T> recordMapper) {
        Response response = given()
                .log()
                .ifValidationFails()
                .spec(DestinationsRequestSpecificationFactory.destinations(apiId, apiKey))
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
        List<T> destinations = Collections.synchronizedList(response.body()
                .as(Departures.class)
                .getDestinations()
                .stream()
                .filter(recordFilter)
                .map(recordMapper)
                .collect(Collectors.toList()));
        if (pagesCount > 1) {
            IntStream.range(SECOND_PAGE, pagesCount + 1)
                    .parallel()
                    .boxed()
                    .forEach(page -> destinations.addAll(getPageDestinations(recordFilter, recordMapper, page)));
        }
        return destinations
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private <T> List<T> getPageDestinations(Predicate<? super Destination> recordFilter, Function<Destination, T> recordMapper, Integer page) {
        return given()
                .log()
                .ifValidationFails()
                .spec(DestinationsRequestSpecificationFactory.destinationsPage(apiId, apiKey, page))
                .get()
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .response()
                .as(Departures.class)
                .getDestinations()
                .stream()
                .filter(recordFilter)
                .map(recordMapper)
                .collect(Collectors.toList());
    }
}
