/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.rest.klijenti.KorisnikRESTKlijent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Zeus
 */
@Named(value = "prijavaKorisnika")
@RequestScoped
public class PrijavaKorisnika extends ApstraktnoZrno implements Serializable {

    private String korisnickoIme;

    private String lozinka;

    private final KorisnikRESTKlijent korisnikREST;

    public PrijavaKorisnika() {
        this.korisnikREST = new KorisnikRESTKlijent();
    }

    public void login(ActionEvent event) throws IOException {
        String msgText = null;
        String msgTitle = jeziciBundle.getString("login_pogreska_naslov");
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_WARN;
        boolean loggedIn = false;

        if (isAllSet()) {
            Korisnik korisnik = JsonHelper
                    .parseKorisnik(korisnikREST.getKorisnikRESTResource(korisnickoIme));
            if (korisnik == null || !korisnik.getKorisnickaLozinka().equals(lozinka)) {
                msgText = jeziciBundle.getString("login_neispravni_kredencijali");
            } else {
                loggedIn = true;
                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) exContext.getSession(false);
                exContext.redirect("privatno/pregledKorisnika.xhtml");
                session.setAttribute("korisnik", korisnik);
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
