package org.job.interview.tests.models;

import lombok.Data;

import java.util.List;

@Data
public class Departures {
    private List<Destination> destinations;
}