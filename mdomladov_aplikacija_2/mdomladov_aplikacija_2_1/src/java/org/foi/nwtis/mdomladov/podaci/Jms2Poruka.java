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
public class Jms2Poruka implements Serializable {
    
    private int brojJmsPoruke;
    
    private long pocetakSlota;
    
    private long krajSlota;
    
    private int brojPorukaSlot;

    public Jms2Poruka(int brojJmsPoruke, long pocetakSlota, long krajSlota, int brojPorukaSlot) {
        this.brojJmsPoruke = brojJmsPoruke;
        this.pocetakSlota = pocetakSlota;
        this.krajSlota = krajSlota;
        this.brojPorukaSlot = brojPorukaSlot;
    }

    public Jms2Poruka() {
    }

    public int getBrojJmsPoruke() {
        return brojJmsPoruke;
    }

    public void setBrojJmsPoruke(int brojJmsPoruke) {
        this.brojJmsPoruke = brojJmsPoruke;
    }

    public long getPocetakSlota() {
        return pocetakSlota;
    }

    public void setPocetakSlota(long pocetakSlota) {
        this.pocetakSlota = pocetakSlota;
    }

    public long getKrajSlota() {
        return krajSlota;
    }

    public void setKrajSlota(long krajSlota) {
        this.krajSlota = krajSlota;
    }

    public int getBrojPorukaSlot() {
        return brojPorukaSlot;
    }

    public void setBrojPorukaSlot(int brojPorukaSlot) {
        this.brojPorukaSlot = brojPorukaSlot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Broj JMS:");
        sb.append(brojJmsPoruke);
        sb.append("\n");
        sb.append("Pocetak obrade:");
        sb.append(pocetakSlota);
        sb.append("\n");
        sb.append("Završetak obrade:");
        sb.append(krajSlota);
        sb.append("\n");
        sb.append("Broj poruka u MQTT slotu:");
        sb.append(brojPorukaSlot);
        sb.append("\n");
        
        return sb.toString();
    }
    
    
}
