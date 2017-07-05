/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.foi.nwtis.mdomladov.ejb.eb.Mqtt;
import org.foi.nwtis.mdomladov.helpers.QueueHelper;
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;
import org.foi.nwtis.mdomladov.podaci.MqttKolekcija;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

/**
 *
 * @author Zeus
 */
public class MqttSlusac implements Listener {

    private long count;

    private static MqttKolekcija messages;

    private final int slotSize;

    private long startTime;

    private QueueHelper queueHelper;

    public MqttSlusac(int slotSize) {
        messages = new MqttKolekcija();
        count = 0;
        this.slotSize = slotSize;
        this.startTime = System.currentTimeMillis();
        queueHelper = new QueueHelper(QueueHelper.QUEUE_NWTIS_2);
    }

    @Override
    public void onConnected() {
        System.out.println("Otvorena veza na MQTT");
    }

    @Override
    public void onDisconnected() {
        System.out.println("Prekinuta veza na MQTT");
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {

        synchronized(MqttSlusac.class){
            messages.addMqtt(createMqttObject(msg.utf8().toString()));
        }
        count++;
        if (count % slotSize == 0) {
            System.out.println("Popunjen je slot");
            queueHelper.send(createJmsMessage());
            this.startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onFailure(Throwable thrwbl) {
        System.out.println("Problem u vezi na MQTT: " + thrwbl.getMessage());
    }

    private Mqtt createMqttObject(String msg) {
        JsonReader reader = Json.createReader(new StringReader(msg));
        JsonObject jo = reader.readObject();
        Mqtt mqtt = new Mqtt();
        mqtt.setIotId(jo.getJsonNumber("IoT").intValue());
        mqtt.setTekst(jo.getString("tekst"));
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:sss");
        try {
            mqtt.setVrijemeReceived(sdf.parse(jo.getString("vrijeme")));
        } catch (ParseException ex) {
            mqtt.setVrijemeReceived(new Date());
            Logger.getLogger(MqttSlusac.class.getName()).log(Level.SEVERE, null, ex);
        }
        mqtt.setStatus(jo.getString("status"));
        mqtt.setTrajanje((int) (System.currentTimeMillis() - startTime));

        return mqtt;
    }

    private Serializable createJmsMessage() {

        Jms2Poruka poruka = new Jms2Poruka();
        poruka.setBrojJmsPoruke(queueHelper.getBrojPorukaIzReda() + 1);
        poruka.setBrojPorukaSlot(slotSize);
        poruka.setPocetakSlota(startTime);
        poruka.setKrajSlota(System.currentTimeMillis());
        
        return poruka;
    }

    public static MqttKolekcija getMessages() {
        return messages;
    }
}
