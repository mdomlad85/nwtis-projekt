/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.podaci;

/**
 *
 * @author dkermek
 */
public class Lokacija {

    private String latitude;
    private String longitude;
    private String adresa;    

    public Lokacija() {
    }

    public Lokacija(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Lokacija(String latitude, String longitude, String adresa) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresa = adresa;
    }
    
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", latitude, longitude);
    }   

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
}
