package com.demo.DenuvoDemo.alerts;

import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andrii Filimonov
 */

public class MapAlertDaoTest {
    
    MapAlertDao mapAlertDao;
    
    @BeforeEach
    public void prepare() {
        mapAlertDao = new MapAlertDao();
    }

    @Test
    public void shouldAddAndGetAlert() {
        Date now = new Date();
        UUID alertId = mapAlertDao.addAlert(now);
        Assertions.assertTrue(mapAlertDao.getAlert(alertId).equals(now));
    }
    
    
}
