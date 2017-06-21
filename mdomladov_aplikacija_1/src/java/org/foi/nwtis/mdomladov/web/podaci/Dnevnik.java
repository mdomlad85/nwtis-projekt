/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.podaci;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Marko Domladovac
 */
public class Dnevnik {
    
    private Integer id;
    
    private String korisnik;
    
    private String url;
    
    private String ipadresa;
    
    private Date vrijeme;
    
    private int trajanje;
    
    private int status;
    
    private String vrsta;

    /**
     *
     */
    public Dnevnik() {
    }

    /**
     *
     * @param id
     */
    public Dnevnik(Integer id) {
        this.id = id;
    }

    /**
     *
     * @param id
     * @param korisnik
     * @param url
     * @param ipadresa
     * @param trajanje
     * @param status
     */
    public Dnevnik(Integer id, String korisnik, String url, String ipadresa, int trajanje, int status) {
        this.id = id;
        this.korisnik = korisnik;
        this.url = url;
        this.ipadresa = ipadresa;
        this.trajanje = trajanje;
        this.status = status;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     *
     * @param korisnik
     */
    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public String getIpadresa() {
        return ipadresa;
    }

    /**
     *
     * @param ipadresa
     */
    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    /**
     *
     * @return
     */
    public Date getVrijeme() {
        return vrijeme;
    }

    /**
     *
     * @param vrijeme
     */
    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    /**
     *
     * @return
     */
    public int getTrajanje() {
        return trajanje;
    }

    /**
     *
     * @param trajanje
     */
    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dnevnik)) {
            return false;
        }
        Dnevnik other = (Dnevnik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.mdomladov.podaci.Dnevnik[ id=" + id + " ]";
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
    
    public enum Vrsta {
        KOMANDA,
        WS
    }
    
    private static HashMap<Vrsta, String> vrste;
    
    static {
        vrste = new HashMap<>();
        vrste.put(Vrsta.WS, "WS");
        vrste.put(Vrsta.KOMANDA, "KOMANDA");
    }

    public static HashMap<Vrsta, String> getVrste() {
        return vrste;
    }
    
    
}
