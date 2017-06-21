/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.foi.nwtis.mdomladov.rest.klijenti.KorisnikRESTKlijent;

/**
 *
 * @author Zeus
 */
@Named(value = "korisnikZrno")
@RequestScoped
public class KorisnikZrno extends ApstraktnoZrno {

    protected String korisnickoIme;

    protected String ime;

    protected String prezime;

    protected String email;

    protected String lozinka;

    protected String ponovljenaLozinka;

    protected final KorisnikRESTKlijent korisnikREST;

    /**
     * Creates a new instance of KorisnikZrno
     */
    public KorisnikZrno() {
        super();
        korisnikREST = new KorisnikRESTKlijent();
    }
    

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPonovljenaLozinka() {
        return ponovljenaLozinka;
    }

    public void setPonovljenaLozinka(String ponovljenaLozinka) {
        this.ponovljenaLozinka = ponovljenaLozinka;
    }

    protected boolean isAllSet() {
        return isAllSetWithoutPass()
                && isPassSet();
    }
    
     protected boolean isAllSetWithoutPass() {
        return korisnickoIme != null && !korisnickoIme.isEmpty()
                && ime != null && !ime.isEmpty()
                && prezime != null && !prezime.isEmpty()
                && email != null && !email.isEmpty();
    }

    protected boolean isPassSet() {
        return lozinka != null && !lozinka.isEmpty()
                && ponovljenaLozinka != null && !ponovljenaLozinka.isEmpty();
    }

    protected boolean isEmailValidan() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    protected boolean korisnickoImeJedinstveno() {
        String korisnikJson = korisnikREST.getKorisnikRESTResource(korisnickoIme);

        return korisnikJson == null
                || korisnikJson.isEmpty()
                || korisnikJson.contentEquals("{\"id\":0}");
    }
}
