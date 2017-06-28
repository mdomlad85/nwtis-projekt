/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import okhttp3.Response;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.web.filteri.KontrolaPristupa;

/**
 *
 * @author Zeus
 */
@Named(value = "uredivanjeKorisnika")
@RequestScoped
public class UredivanjeKorisnika extends KorisnikZrno {
    
    private Korisnik korisnik;

    @PostConstruct
    private void init() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) exContext.getSession(false);
        korisnik = (Korisnik) session.getAttribute(KontrolaPristupa.KORISNIK_ATTRIBUTE);
        korisnickoIme = korisnik.getKorisnickoIme();
        prezime = korisnik.getPrezime();
        email = korisnik.getEmail();
        ime = korisnik.getIme();
    }

    public void uredi(ActionEvent event) {
        String msgText;
        String msgTitle = jeziciBundle.getString("registracija_pogreska_naslov");
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_WARN;

        if (!isAllSetWithoutPass()) {
            msgText = jeziciBundle.getString("registracija_nepotpuna_forma");
        } else if (!ponovljenaLozinka.equals(lozinka) && isPassSet()) {
            msgText = jeziciBundle.getString("registracija_pogreska_ponovljenaLozinka");
        } else if (!isEmailValidan()) {
            msgText = jeziciBundle.getString("registracija_pogreska_email");
        } else if (!korisnik.getKorisnickoIme().equals(korisnickoIme) && !korisnickoImeJedinstveno()) {
            msgText = jeziciBundle.getString("registracija_pogreska_korisnickoIme");
        } else {
            try {
                Korisnik editUser = new Korisnik(ime, prezime, korisnickoIme, lozinka, email);
                if(korisnik.getId() == 0){
                    String korisnikJson = korisnikREST.getKorisnikRESTResource(korisnik.getKorisnickoIme());
                    Korisnik dbKorisnik = JsonHelper.parseKorisnik(korisnikJson);
                    if(dbKorisnik != null){
                        korisnik.setId(dbKorisnik.getId());
                    }
                }
                editUser.setId(korisnik.getId());
                
                if(!isPassSet()){
                    editUser.setKorisnickaLozinka(korisnik.getKorisnickaLozinka());
                }
                
                Response putJson = korisnikREST.putJson(editUser);
                if (putJson.isSuccessful()) {
                    msgText = jeziciBundle.getString("registracija_uspjeh");
                    msgTitle = jeziciBundle.getString("registracija_uspjeh_naslov");
                    msgFlag = FacesMessage.SEVERITY_INFO;
                    ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                    HttpSession session = (HttpSession) exContext.getSession(false);
                    exContext.redirect("pregledKorisnika.xhtml");
                    session.setAttribute(KontrolaPristupa.KORISNIK_ATTRIBUTE, editUser);
                } else {
                    msgText = jeziciBundle.getString("registracija_pogreska_admin");
                }
            } catch (IOException ex) {
                Logger.getLogger(UredivanjeKorisnika.class.getName()).log(Level.SEVERE, null, ex);
                msgText = jeziciBundle.getString("registracija_pogreska_admin");
            }
        }

        FacesMessage message = new FacesMessage(msgFlag, msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
