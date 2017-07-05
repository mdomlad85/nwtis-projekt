/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import okhttp3.Response;
import org.foi.nwtis.mdomladov.dretve.MqttDretva;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.web.filteri.KontrolaPristupa;

/**
 *
 * @author Zeus
 */
@Named(value = "registracijaKorisnika")
@RequestScoped
public class RegistracijaKorisnika extends KorisnikZrno {

    public void registriraj(ActionEvent event) {
        String msgText;
        String msgTitle = jeziciBundle.getString("registracija_pogreska_naslov");
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_WARN;

        if (!isAllSet()) {
            msgText = jeziciBundle.getString("registracija_nepotpuna_forma");
        } else if (!ponovljenaLozinka.equals(lozinka)) {
            msgText = jeziciBundle.getString("registracija_pogreska_ponovljenaLozinka");
        } else if (!isEmailValidan()) {
            msgText = jeziciBundle.getString("registracija_pogreska_email");
        } else if (!korisnickoImeJedinstveno()) {
            msgText = jeziciBundle.getString("registracija_pogreska_korisnickoIme");
        } else {
            Korisnik korisnik = new Korisnik(ime, prezime, korisnickoIme, lozinka, email);
            try {
                Response postJson = korisnikREST.postJson(korisnik);
                if (postJson.isSuccessful()) {
                    msgText = jeziciBundle.getString("registracija_uspjeh");
                    msgTitle = jeziciBundle.getString("registracija_uspjeh_naslov");
                    msgFlag = FacesMessage.SEVERITY_INFO;
                    ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                    HttpSession session = (HttpSession) exContext.getSession(false);
                    exContext.redirect("privatno/pregledKorisnika.xhtml");
                    session.setAttribute(KontrolaPristupa.KORISNIK_ATTRIBUTE, korisnik);
                    MqttDretva mqttWorker = new MqttDretva(korisnickoIme, lozinka);
                    session.setAttribute(ZrnoUpravljanjaSjednicom.MQTT_WORKER, mqttWorker);
                    mqttWorker.start();
                } else {
                    msgText = jeziciBundle.getString("registracija_pogreska_admin");
                }
            } catch (IOException ex) {
                Logger.getLogger(RegistracijaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
                msgText = jeziciBundle.getString("registracija_pogreska_admin");
            }
        }

        FacesMessage message = new FacesMessage(msgFlag, msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
