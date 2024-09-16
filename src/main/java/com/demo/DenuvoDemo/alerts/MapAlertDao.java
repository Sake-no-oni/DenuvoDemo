package com.demo.DenuvoDemo.alerts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Andrii Filimonov
 */
public class MapAlertDao implements AlertDAO {
    
    private final Map<UUID, Date> alerts = new HashMap<>();

    @Override
    public UUID addAlert(Date time) {
        UUID id = UUID.randomUUID();
        alerts.put(id, time);
        return id;
    }

    @Override
    public Date getAlert(UUID id) {
        return alerts.get(id);
    }
    
}
