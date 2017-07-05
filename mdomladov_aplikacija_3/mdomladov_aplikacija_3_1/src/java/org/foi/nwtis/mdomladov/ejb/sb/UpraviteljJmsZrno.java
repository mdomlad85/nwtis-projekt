/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.JMSException;
import org.foi.nwtis.mdomladov.helpers.QueueHelper;
import org.foi.nwtis.mdomladov.helpers.SerializatorHelper;
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;
import org.foi.nwtis.mdomladov.podaci.Statistika;
import org.foi.nwtis.mdomladov.web.slusaci.Nwtis1JmsSlusac;
import org.foi.nwtis.mdomladov.web.slusaci.Nwtis2JmsSlusac;

/**
 *
 * @author Marko Domladovac
 */
@ConcurrencyManagement(CONTAINER)
@Singleton
@Startup
public class UpraviteljJmsZrno {

    private final QueueHelper nwtis1Queue;

    private final QueueHelper nwtis2Queue;

    private ArrayList<Statistika> nwtis1Poruke;

    private ArrayList<Jms2Poruka> nwtis2Poruke;

    private String putanjaDatoteke;

    public UpraviteljJmsZrno() {
        nwtis1Poruke = new ArrayList<>();
        nwtis2Poruke = new ArrayList<>();
        nwtis1Queue = new QueueHelper(QueueHelper.QUEUE_NWTIS_1);
        nwtis2Queue = new QueueHelper(QueueHelper.QUEUE_NWTIS_2);
        nwtis1Queue.setSlusacPorukeIzReda(new Nwtis1JmsSlusac(this));
        nwtis2Queue.setSlusacPorukeIzReda(new Nwtis2JmsSlusac(this));
    }

    @PreDestroy
    private void destroy() {
        spremiPodatke();
        try {
            nwtis1Queue.closeResources();
            nwtis2Queue.closeResources();
        } catch (JMSException ex) {
            Logger.getLogger(UpraviteljJmsZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Statistika> getNwtis1Poruke() {
        return nwtis1Poruke;
    }

    public void obrisiNwtis2Poruku(int porukaId) {
        Iterator<Jms2Poruka> i = nwtis2Poruke.iterator();
        while (i.hasNext()) {
            Jms2Poruka p = i.next();
            if (p.getBrojJmsPoruke() == porukaId) {
                i.remove();
                break;
            }
        }
    }

    public void addNwtis1Poruka(Statistika poruka) {
        nwtis1Poruke.add(poruka);
    }

    public void setNwtis1Poruke(ArrayList<Statistika> nwtis1Poruke) {
        this.nwtis1Poruke = nwtis1Poruke;
    }

    public void obrisiNwtis1Poruku(int porukaId) {
        Iterator<Statistika> i = nwtis1Poruke.iterator();
        while (i.hasNext()) {
            Statistika p = i.next();
            if (p.getBrojac() == porukaId) {
                i.remove();
                break;
            }
        }
    }

    public void addNwtis2Poruka(Jms2Poruka poruka) {
        nwtis2Poruke.add(poruka);
    }

    public ArrayList<Jms2Poruka> getNwtis2Poruke() {
        return nwtis2Poruke;
    }

    public void setNwtis2Poruke(ArrayList<Jms2Poruka> nwtis2Poruke) {
        this.nwtis2Poruke = nwtis2Poruke;
    }

    public void setPutanjaDatoteke(String putanja) {
        this.putanjaDatoteke = putanja;
        ucitajPodatke();
    }

    private void ucitajPodatke() {
        Serializable[] arr = SerializatorHelper.loadFile(putanjaDatoteke);

        if (arr != null) {
            for (Serializable item : arr) {
                if (item instanceof Statistika) {
                    addNwtis1Poruka((Statistika) item);
                } else if (item instanceof Jms2Poruka) {
                    addNwtis2Poruka((Jms2Poruka) item);
                }
            }
        }
    }

    private void spremiPodatke() {
        int nw1size = nwtis1Poruke.size();
        int nw2size = nwtis2Poruke.size();
        Serializable[] arr
                = new Serializable[nw1size + nw2size];

        for (int i = 0; i < nw1size; i++) {
            arr[i] = (Serializable) nwtis1Poruke.get(i);
        }

        for (int i = 0; i < nw2size; i++) {
            arr[i + nw1size] = (Serializable) nwtis2Poruke.get(i);
        }
        SerializatorHelper.saveFile(arr, putanjaDatoteke);
    }
}
