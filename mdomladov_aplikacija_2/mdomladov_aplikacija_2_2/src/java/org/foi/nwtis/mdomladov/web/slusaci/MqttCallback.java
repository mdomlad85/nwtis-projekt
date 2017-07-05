/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 *
 * @author Marko Domladovac
 */
public class MqttCallback implements Callback<Void> {

    private final String odrediste;

    private final CallbackConnection conn;

    public MqttCallback(String odrediste, CallbackConnection conn) {
        this.odrediste = odrediste;
        this.conn = conn;
    }

    @Override
    public void onSuccess(Void value) {
        Topic[] topics = {new Topic(odrediste, QoS.AT_LEAST_ONCE)};
        conn.subscribe(topics, new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] qoses) {
                System.out.println("Pretplata na: " + odrediste);
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("Failure: " + value.getMessage());

            }
        });
    }

    @Override
    public void onFailure(Throwable value) {
        System.out.println("Failure: " + value.getMessage());

    }

}
