/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb;

import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.rest.klijenti.KorisnikRESTKlijent;

/**
 *
 * @author Zeus
 */
@Stateful
@LocalBean
public class ZrnoAutorizacije {

    private final KorisnikRESTKlijent korisnikREST;

    public ZrnoAutorizacije() {
        korisnikREST = new KorisnikRESTKlijent();
    }

    public Korisnik autenticirajKorisnika(final String korisnickoIme, final String lozinka) {
        Korisnik korisnik = JsonHelper
                .parseKorisnik(korisnikREST.getKorisnikRESTResource(korisnickoIme));
        if (korisnik == null || !korisnik.getKorisnickaLozinka().equals(lozinka)) {
            korisnik = null;
        }

        return korisnik;
    }
}
