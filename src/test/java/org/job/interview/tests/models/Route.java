package org.job.interview.tests.models;

import lombok.Data;

import java.util.List;

@Data
public class Route {
    private List<String> destinations;
    private String eu;
    private boolean visa;
}
