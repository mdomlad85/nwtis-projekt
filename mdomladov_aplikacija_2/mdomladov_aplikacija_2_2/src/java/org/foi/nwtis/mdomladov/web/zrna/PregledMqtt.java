/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mdomladov.ejb.eb.Mqtt;
import org.foi.nwtis.mdomladov.ejb.sb.facades.MqttFacade;

/**
 *
 * @author Marko Domladovac
 */
@ManagedBean
@RequestScoped
public class PregledMqtt {

    @EJB
    private MqttFacade mqttDb;

    private List<Mqtt> poruke;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledMqtt() {

    }

    @PostConstruct
    private void init() {
        this.poruke = mqttDb.findAll();
    }

    public List<Mqtt> getPoruke() {
        return poruke;
    }

    public void setPoruke(List<Mqtt> poruke) {
        this.poruke = poruke;
    }

    public void obrisi(int id) {
        Iterator<Mqtt> listIterator = poruke.listIterator();
        while (listIterator.hasNext()) {
            Mqtt p = listIterator.next();
            if (p.getId() == id) {
                listIterator.remove();
                break;
            }
        }
        
        Mqtt mqtt = mqttDb.find(id);

        if (mqtt != null) {
            mqttDb.remove(mqtt);
        }
        
    }
}
