package org.job.interview.tests.models;

import lombok.Data;

@Data
public class Destination {
    private String city;
    private String country;
    private String iata;
    private PublicName publicName;
}