/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.dretve.MqttDretva;
import org.foi.nwtis.mdomladov.ejb.sb.ZrnoAutorizacije;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.web.filteri.KontrolaPristupa;

/**
 *
 * @author Zeus
 */
@Named(value = "prijavaKorisnika")
@RequestScoped
public class PrijavaKorisnika extends ApstraktnoZrno implements Serializable {
    
    @EJB
    private ZrnoAutorizacije zrnoAutorizacije;

    private String korisnickoIme;

    private String lozinka;

    public PrijavaKorisnika() {
    }

    public void login(ActionEvent event) throws IOException {
        String msgText = null;
        String msgTitle = jeziciBundle.getString("login_pogreska_naslov");
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_WARN;
        boolean loggedIn = false;

        if (isAllSet()) {
            Korisnik korisnik = zrnoAutorizacije.autenticirajKorisnika(korisnickoIme, lozinka);
            if (korisnik == null) {
                msgText = jeziciBundle.getString("login_neispravni_kredencijali");
            } else {
                loggedIn = true;
                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) exContext.getSession(false);
                exContext.redirect("privatno/pregledKorisnika.xhtml");
                session.setAttribute(KontrolaPristupa.KORISNIK_ATTRIBUTE, korisnik);
                MqttDretva mqttWorker = new MqttDretva(korisnickoIme, lozinka);
                session.setAttribute(ZrnoUpravljanjaSjednicom.MQTT_WORKER, mqttWorker);
                mqttWorker.start();
            }
        } else {
            msgText = jeziciBundle.getString("login_nepotpuna_forma");
        }

        FacesMessage message = new FacesMessage(msgFlag, msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    private boolean isAllSet() {
        return korisnickoIme != null && !korisnickoIme.isEmpty()
                && lozinka != null && !lozinka.isEmpty();
    }

}
