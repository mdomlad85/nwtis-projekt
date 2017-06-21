/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mdomladov.dal.KorisnikDAL;
import org.foi.nwtis.mdomladov.web.podaci.Korisnik;

/**
 *
 * @author Marko Domladovac
 */
@ManagedBean
@RequestScoped
public class PregledKorisnika {
    
    KorisnikDAL db;
    
    private List<Korisnik> korisnici;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledKorisnika() {
    }    
    
    @PostConstruct
    private void init() {
        db = new KorisnikDAL();
        korisnici = db.getKorisnici();
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }
    
    
}
