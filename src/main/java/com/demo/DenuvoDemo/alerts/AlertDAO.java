package com.demo.DenuvoDemo.alerts;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Andrii Filimonov
 */
interface AlertDAO {
    
    public UUID addAlert(Date time);
    
    public Date getAlert(UUID id);
    
}
