/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.podaci;

import java.io.Serializable;

/**
 *
 * @author Zeus
 */
public class Korisnik implements Serializable {
    
    private int id;
    
    private String ime;
    
    private String prezime;
    
    private String korisnickoIme;
    
    private String korisnickaLozinka;
    
    private String email;

    public Korisnik(int id, String ime, String prezime, String korisnickoIme, String korisnickaLozinka, String email) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.korisnickaLozinka = korisnickaLozinka;
        this.email = email;
    }

    public Korisnik(String ime, String prezime, String korisnickoIme, String korisnickaLozinka, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.korisnickaLozinka = korisnickaLozinka;
        this.email = email;
    }

    public Korisnik() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getKorisnickaLozinka() {
        return korisnickaLozinka;
    }

    public void setKorisnickaLozinka(String korisnickaLozinka) {
        this.korisnickaLozinka = korisnickaLozinka;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
