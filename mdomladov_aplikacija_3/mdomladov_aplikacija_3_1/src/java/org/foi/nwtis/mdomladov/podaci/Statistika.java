/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.podaci;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Dretva na kraju svakog ciklusa šalje email poruku u text/plain formatu na
 * adresu (određena konfiguracijom, npr. admin@nwtis.nastava.foi.hr), uz predmet
 * koji započinje statičkim dijelom (određen konfiguracijom, npr.
 *
 * @author Marko Domladovac
 */
public class Statistika implements Serializable {

    /**
     *
     * Ukupan broj poruka
     */
    public int ukupnoPoruka;

    /**
     *
     * Ukupan broj ispravnih poruka
     */
    public int ukupnoIspravnihPoruka;
    
    private int brojac;

    private long startTime;

    private long stopTime;

    /**
     *
     * Konstruktor
     */
    public Statistika() {
        ukupnoPoruka = 0;
        ukupnoIspravnihPoruka = 0;
        brojac = 0;
    }

    /**
     *
     * @param startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @param stopTime
     */
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public int getUkupnoPoruka() {
        return ukupnoPoruka;
    }

    public void setUkupnoPoruka(int ukupnoPoruka) {
        this.ukupnoPoruka = ukupnoPoruka;
    }

    public int getUkupnoIspravnihPoruka() {
        return ukupnoIspravnihPoruka;
    }

    public void setUkupnoIspravnihPoruka(int ukupnoIspravnihPoruka) {
        this.ukupnoIspravnihPoruka = ukupnoIspravnihPoruka;
    }

    public int getBrojac() {
        return brojac;
    }

    public void setBrojac(int brojac) {
        this.brojac = brojac;
    }
    
    

    /**
     *
     * Statistika poruka) iza kojeg dolazi redni broj poruke u formatu #.##0, a
     * u sadržaju ima sljedeće elemente:
     *
     * Obrada započela u: vrijeme_1 (dd.MM.yyyy hh.mm.ss.zzz)
     * Obrada završila u: vrijeme_2 (dd.MM.yyyy hh.mm.ss.zzz)
     *
     * Trajanje obrade u ms: n 
     * Broj poruka: n - odnosi se na jedan ciklus 
     * Broj dodanih IOT: n 
     * Broj mjerenih  TEMP: n 
     * Broj izvršenih EVENT: n 
     * Broj pogrešaka: n
     *
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("   Obrada započela u: (%s)\n\r", formatirajDatum(startTime)));
        sb.append(String.format("   Obrada završila u: (%s)\n\r", formatirajDatum(stopTime)));
        sb.append(String.format("Trajanje obrade u ms: (%d)\n\r", stopTime - startTime));
        sb.append(String.format("         Broj poruka: (%d)\n\r", ukupnoPoruka));
        sb.append(String.format("   Broj NWTiS poruka: (%d)\n\r", ukupnoIspravnihPoruka));        

        return sb.toString();
    }

    private String formatirajDatum(long timestamp) {
        Date datum = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");

        return sdf.format(datum);
    }

}
