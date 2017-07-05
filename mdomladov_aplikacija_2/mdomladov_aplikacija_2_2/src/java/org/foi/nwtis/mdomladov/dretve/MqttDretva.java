/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dretve;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.Mqtt_Sucelje;
import org.foi.nwtis.mdomladov.web.slusaci.MqttCallback;
import org.foi.nwtis.mdomladov.web.slusaci.MqttSlusac;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;

/**
 *
 * @author Marko Domladovac
 */
public class MqttDretva extends Thread {

    Mqtt_Sucelje konfiguracija;
    
    String korisnickoIme;
    
    String lozinka;
            
    
    private CallbackConnection connection;

    public MqttDretva(Mqtt_Sucelje postavke, String korisnickoIme, String lozinka) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        konfiguracija = postavke;
    }

    public MqttDretva(String korisnickoIme, String lozinka) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        konfiguracija = (Mqtt_Sucelje) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        try {
            MQTT mqtt = createMqtt();
            connection = mqtt.callbackConnection();
            connection.listener(new MqttSlusac(konfiguracija.GetMSlot()));
            connection.connect(new MqttCallback(getOdrediste(), connection));

            synchronized (this) {
                while (true) {
                    this.wait();
                }
            }
        } catch (URISyntaxException | InterruptedException ex) {
            Logger.getLogger(MqttDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        if(connection != null){
            connection.disconnect(null);
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    private MQTT createMqtt() throws URISyntaxException {
        MQTT mqtt = new MQTT();
        mqtt.setHost(konfiguracija.getMHost(), konfiguracija.getMPort());
        mqtt.setUserName(konfiguracija.getMUsername());
        mqtt.setPassword(konfiguracija.GetMPassword());

        return mqtt;
    }

    private String getOdrediste() {
        return String.format("/%s/%s", konfiguracija.GetMTema(), 
                konfiguracija.getMUsername());
    }

}
