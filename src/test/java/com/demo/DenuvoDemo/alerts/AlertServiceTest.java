package com.demo.DenuvoDemo.alerts;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andrii Filimonov
 */

public class AlertServiceTest {

    AlertService alertService;
    
    @BeforeEach
    public void prepare() {
        alertService = new AlertService(new MapAlertDao());
    }
    
    @Test
    public void shouldRaiseAndGetAlert() {
        UUID alertID = alertService.raiseAlert();
        Assertions.assertNotNull(alertService.getAlertTime(alertID));
    }
}
