/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.foi.nwtis.mdomladov.dretve.ObradaPoruka;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;

/**
 * Kreiranje Singleton SB pokreće dretvu (konfiguracijom se određuje pravilni
 * vremenski interval rada (jedinica je sekunda), npr. 5 sec, 20 sec, 100 sec,
 * ...) koja provjerava u poštanskom sandučiću (adresa poslužitelja, korisničko
 * ime i lozinka definiraju se u konfiguracijskoj datoteci) pristiglu poštu.
 * Brisanje Singleton SB prekida dretvu i zaustavlja ju.
 *
 * @author Marko Domladovac
 */
@Singleton
@Startup
public class UpravljackoZrno {

    private static int brojac;

    private ObradaPoruka dretva;

    private static APP_Konfiguracija konfiguracija;

    /**
     * Inicijalizirana od strane slusaca jer 
     * je potreba konfiguracija
     * 
     * @param konf 
     */
    public void init(APP_Konfiguracija konf) {
        konfiguracija = konf;
        dretva = new ObradaPoruka();
        dretva.start();
    }

    @PreDestroy
    private void destroy() {
        if (dretva != null) {
            dretva.interrupt();
        }
    }

    public static APP_Konfiguracija getKonfiguracija() {
        return konfiguracija;
    }

    public static int getBrojac() {
        return brojac;
    }

    public static int getBrojacIPovecaj() {
        return brojac++;
    }
}
