/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.podaci;

import java.util.ArrayList;
import org.foi.nwtis.mdomladov.ejb.eb.Mqtt;

/**
 *
 * @author Marko Domladovac
 */
public class MqttKolekcija {
    
    private ArrayList<Mqtt> mqtt;

    public MqttKolekcija() {
        this.mqtt = new ArrayList<>();
    }    

    public synchronized ArrayList<Mqtt> getMqtt() {
        return mqtt;
    }

    public synchronized void setMqtt(ArrayList<Mqtt> mqtt) {
        this.mqtt = mqtt;
    }

    public synchronized void addMqtt(Mqtt mqt) {

        mqtt.add(mqt);

    }

    public synchronized void removeMqtt(Mqtt mqt) {
        mqtt.remove(mqt);
    }
}
