/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import org.foi.nwtis.mdomladov.dretve.ObradaPoruka;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;

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
@LocalBean
public class UpravljackoZrno {
    
    private static int brojac;

    private ObradaPoruka dretva;

    private static APP_Konfiguracija konfiguracija;

    @PostConstruct
    private void init() {
        try {
            String filepath2 = this.getClass().getResource("NWTiS.db.config_1.xml").getFile();
            String filepath = "E:\\Faks\\FOI\\Moje\\DS_1_GOD\\Ljetni\\NWTIS\\Projekt\\Programski_kod\\mdomladov_aplikacija_2\\mdomladov_aplikacija_2_1\\NWTiS.db.config_1.xml";
            konfiguracija = new APP_Konfiguracija(filepath);
            dretva = new ObradaPoruka();
            //dretva.start();
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(UpravljackoZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public static int getBrojacIPovecaj(){
        return brojac++;
    }
}
