/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import javax.jms.Message;
import javax.jms.MessageListener;
import org.foi.nwtis.mdomladov.ejb.sb.UpraviteljJmsZrno;

/**
 *
 * @author Marko Domladovac
 */
public abstract class NwtisJmsSlusac implements MessageListener {
    
    protected final UpraviteljJmsZrno jmsZrno;

    public NwtisJmsSlusac(UpraviteljJmsZrno jmsZrno) {
        this.jmsZrno = jmsZrno;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Abstract class");
    }
    
}
