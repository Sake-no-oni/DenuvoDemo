package com.demo.DenuvoDemo.alerts;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Andrii Filimonov
 */
public class AlertService {
    
    MapAlertDao storage;
    
    public AlertService(MapAlertDao mapAlertDao) {
        this.storage = mapAlertDao;
    }
    
    public UUID raiseAlert() {
        return storage.addAlert(new Date());
    }
    
    public Date getAlertTime(UUID id) {
        return storage.getAlert(id);
    }
}
