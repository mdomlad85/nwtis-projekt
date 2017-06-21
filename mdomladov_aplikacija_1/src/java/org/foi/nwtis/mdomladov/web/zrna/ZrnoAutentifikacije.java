/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.dal.KorisnikDAL;
import org.foi.nwtis.mdomladov.web.podaci.Korisnik;

/**
 *
 * @author Zeus
 */
@ManagedBean
@RequestScoped
public class ZrnoAutentifikacije {

    private String korisnickoIme;

    private String lozinka;

    private String porukaPogreske;

    /**
     * Creates a new instance of ZrnoAutentifikacije
     */
    public ZrnoAutentifikacije() {
    }

    public void ulogirajSe() {
        if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
            porukaPogreske = "Molimo unesite podatke za prijavu!";
        } else {
            KorisnikDAL db = new KorisnikDAL();
            Korisnik korisnik = db.getKorisnik(korisnickoIme, lozinka);

            if (korisnik != null) {
                try {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpSession session = (HttpSession) facesContext
                            .getExternalContext()
                            .getSession(true);
                    session.setAttribute("korisnik", korisnik);
                    facesContext.getExternalContext().dispatch("/faces/index.xhtml");
                } catch (IOException ex) {
                    porukaPogreske = "Došlo je do pogreške! Molimo obratite se administratoru.";
                }
            } else {
                porukaPogreske = "Pogrešni podaci za prijavu! Molimo pokušajte ponovno.";
            }
        }
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

    public String getPorukaPogreske() {
        return porukaPogreske;
    }

    public void setPorukaPogreske(String porukaPogreske) {
        this.porukaPogreske = porukaPogreske;
    }

}
