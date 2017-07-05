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
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;

/**
 *
 * @author Zeus
 */
public class Nwtis2JmsSlusac extends NwtisJmsSlusac {

    public Nwtis2JmsSlusac(UpraviteljJmsZrno jmsZrno) {
        super(jmsZrno);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            try {
                Object obj = objMessage.getObject();
                if (obj instanceof Jms2Poruka) {
                    Jms2Poruka poruka = (Jms2Poruka) objMessage.getObject();
                    jmsZrno.addNwtis2Poruka(poruka);
                    WebSocketHelper wsHelper = new WebSocketHelper("jms2");
                    wsHelper.send(JsonHelper.createJsonObjectString(poruka));
                }
            } catch (JMSException | URISyntaxException ex) {
                Logger.getLogger(Nwtis2JmsSlusac.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
