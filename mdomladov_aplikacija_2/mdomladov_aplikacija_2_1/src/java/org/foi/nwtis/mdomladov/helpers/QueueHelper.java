/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Marko Domladovac
 */
public class QueueHelper {

    private static final String CONN_FACTORY_NAME = "jms/NWTiSConnectionFactory";

    public static final String QUEUE_NWTIS_1 = "jms/NWTiS_mdomladov_1";

    public static final String QUEUE_NWTIS_2 = "jms/NWTiS_mdomladov_2";
    
    private Queue queue;

    protected Connection conn;

    protected Session session;

    protected String queueName;

    protected int brojac;

    public void send(Serializable message) {
        try {
            openResources();
            MessageProducer mp = session.createProducer(queue);
            ObjectMessage om = session.createObjectMessage(message);
            mp.send(om);
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(QueueHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeResources();
            } catch (JMSException ex) {
                Logger.getLogger(QueueHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public QueueHelper(String queueName) {
        this.queueName = queueName;
    }

    protected void openResources() throws JMSException, NamingException {
        InitialContext c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup(CONN_FACTORY_NAME);
        conn = cf.createConnection();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = (Queue) c.lookup(queueName);
    }

    protected void closeResources() throws JMSException {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
            }
        }
        if (conn != null) {
            conn.close();
        }
    }

    public int getBrojPorukaIzReda() {
        int brojPoruka = -1;
        try {
            openResources();
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration enu = browser.getEnumeration();
            brojPoruka = 0;
            while (enu.hasMoreElements()) {
                Object obj = enu.nextElement();
                brojPoruka++;
            }
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(QueueHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeResources();
            } catch (JMSException ex) {
                Logger.getLogger(QueueHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return brojPoruka;
    }
}
