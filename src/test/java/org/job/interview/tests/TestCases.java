package org.job.interview.tests;

import groovy.lang.Tuple;
import groovy.lang.Tuple2;
import org.job.interview.tests.configuration.TestCasesConfiguration;
import org.job.interview.tests.configuration.TestPropertiesResolver;
import org.job.interview.tests.extractors.DestinationsEndpointExtractor;
import org.job.interview.tests.extractors.FlightsEndpointExtractor;
import org.job.interview.tests.models.Flight;
import org.job.interview.tests.requests.DestinationsRequestSpecificationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@ContextConfiguration(classes = TestCasesConfiguration.class)
class TestCases {
    @Autowired
    private TestPropertiesResolver testPropertiesResolver;

    @Test
    void test1() {
        //given
        DestinationsEndpointExtractor destinationsEndpointExtractor = DestinationsEndpointExtractor.of(testPropertiesResolver.getAppId(), testPropertiesResolver.getAppKey());

        //when
        List<Tuple2<String, String>> destinations = destinationsEndpointExtractor.extractData(destination -> Tuple.tuple(destination.getCountry(), destination.getCity()))
                .stream()
                .sorted(Comparator.comparing(Tuple2::getV1, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        //then
        assertThat(destinations).contains(Tuple.tuple("Australia", "Sydney"));
    }

    @Test
    void test2() {
        //given
        DestinationsEndpointExtractor destinationsEndpointExtractor = DestinationsEndpointExtractor.of(testPropertiesResolver.getAppId(), testPropertiesResolver.getAppKey());

        //when
        List<Tuple2<String, String>> destinations = destinationsEndpointExtractor.extractData(destination -> destination.getCountry() != null && destination.getCountry().equals("Australia"), destination -> Tuple.tuple(destination.getCity(), destination.getIata()));
        List<String> cities = new ArrayList<>();
        List<String> iatas = new ArrayList<>();
        destinations
                .forEach(destination -> {
                    cities.add(destination.getV1());
                    iatas.add(destination.getV2());
                });

        //then
        assertThat(cities).doesNotContainNull();
        assertThat(iatas).doesNotContainNull();
    }

    @Test
    void test3() {
        //given
        FlightsEndpointExtractor flightsEndpointExtractor = FlightsEndpointExtractor.of(testPropertiesResolver.getAppId(), testPropertiesResolver.getAppKey());

        //when
        List<Flight> todayFlights = flightsEndpointExtractor.extractTodayFlights();

        //then
        assertThat(todayFlights).extracting(flight -> flight.getAircraftType().getIataMain()).isNotNull();
    }

    @Test
    void test4() {
        given()
                .spec(DestinationsRequestSpecificationFactory.destinations(testPropertiesResolver.getAppId(), "incorrect"))
                .when()
                .get()
                .then()
                .log()
                .ifValidationFails()
                .statusCode(403)
                .body(containsString("Authentication failed"));
    }
}
