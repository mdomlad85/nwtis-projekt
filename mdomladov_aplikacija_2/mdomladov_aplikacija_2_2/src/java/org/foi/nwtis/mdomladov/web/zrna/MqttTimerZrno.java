/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.mdomladov.ejb.eb.Mqtt;
import org.foi.nwtis.mdomladov.ejb.sb.facades.MqttFacade;
import org.foi.nwtis.mdomladov.podaci.MqttKolekcija;
import org.foi.nwtis.mdomladov.web.slusaci.MqttSlusac;

/**
 *
 * @author Zeus
 */
@Stateless
public class MqttTimerZrno {
    
    @EJB
    MqttFacade mqttDb;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void myTimer() {
       try {
            synchronized (MqttSlusac.class) {
                MqttKolekcija mqttHolder = MqttSlusac.getMessages();
                if (mqttHolder != null) {
                    List<Mqtt> poruke = mqttHolder.getMqtt();
                    poruke.forEach((mqtt) -> {
                        mqtt.setId(Integer.SIZE);
                        mqttDb.create(mqtt);
                    });
                    poruke.removeAll(poruke);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MqttTimerZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
