/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.foi.nwtis.mdomladov.ejb.sb.UpraviteljJmsZrno;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.helpers.WebSocketHelper;
import org.foi.nwtis.mdomladov.podaci.Statistika;

/**
 *
 * @author Zeus
 */
public class Nwtis1JmsSlusac extends NwtisJmsSlusac {

    public Nwtis1JmsSlusac(UpraviteljJmsZrno jmsZrno) {
        super(jmsZrno);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            try {
                Object obj = objMessage.getObject();
                if (obj instanceof Statistika) {
                    Statistika poruka = (Statistika) objMessage.getObject();
                    jmsZrno.addNwtis1Poruka(poruka);
                    WebSocketHelper wsHelper = new WebSocketHelper("jms1");
                    wsHelper.send(JsonHelper.createJsonObjectString(poruka));
                }
            } catch (JMSException | URISyntaxException ex) {
                Logger.getLogger(Nwtis1JmsSlusac.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
