/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ws.klijenti;

/**
 *
 * @author Marko Domladovac
 */
public class MeteoKlijent {

    public static String dajAdresuZauredjaj(int uredjajId) {
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajAdresuZauredjaj(uredjajId);
    }

    public static MeteoPodaci dajZadnjeMeteoPodatkeZaUredjaj(int uredjajId) {
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeMeteoPodatkeZaUredjaj(uredjajId);
    }

    public static java.util.List<org.foi.nwtis.mdomladov.ws.klijenti.MeteoPodaci> dajZadnjihNMeteoPodataka(int uredjajId, int brojMeteoPodataka) {
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.mdomladov.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjihNMeteoPodataka(uredjajId, brojMeteoPodataka);
    }
}
