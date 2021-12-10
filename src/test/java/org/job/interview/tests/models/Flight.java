package org.job.interview.tests.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {
    private AircraftType aircraftType;
    private Route route;
    private String scheduleTime;
}
