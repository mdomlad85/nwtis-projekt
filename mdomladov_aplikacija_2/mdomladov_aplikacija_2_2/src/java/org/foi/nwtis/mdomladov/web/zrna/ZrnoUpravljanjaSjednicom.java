/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.dretve.MqttDretva;
import org.foi.nwtis.mdomladov.podaci.Korisnik;

/**
 *
 * @author Zeus
 */
@Named(value = "zrnoUpravljanjaSjednicom")
@SessionScoped
public class ZrnoUpravljanjaSjednicom implements Serializable {
    
    public static final String MQTT_WORKER = "MQTT_WORKER";

    /**
     * Creates a new instance of ZrnoUpravljanjaSjednicom
     */
    public ZrnoUpravljanjaSjednicom() {
    }
    
    @PostConstruct
    public void sessionInitialized() {
    }

    @PreDestroy
    public void sessionDestroyed() {
        System.out.println("ZrnoUpravljanjaSjednicom.sessionDestroyed()");        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        MqttDretva mqttWorker = (MqttDretva) session.getAttribute(MQTT_WORKER);
        if(mqttWorker != null){
            mqttWorker.interrupt();
        }
    }
    
}
