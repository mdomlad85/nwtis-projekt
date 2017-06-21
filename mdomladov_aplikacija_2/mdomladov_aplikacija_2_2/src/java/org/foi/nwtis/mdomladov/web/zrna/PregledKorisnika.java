/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.rest.klijenti.KorisnikRESTKlijent;

/**
 *
 * @author Marko Domladovac
 */
@ManagedBean
@RequestScoped
public class PregledKorisnika {

    private final KorisnikRESTKlijent korisnikREST;
    
    private List<Korisnik> korisnici;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledKorisnika() {
        korisnikREST = new KorisnikRESTKlijent();
        this.korisnici = JsonHelper.parseKorisnici(korisnikREST.getJson());
    } 

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }
    
    
}
