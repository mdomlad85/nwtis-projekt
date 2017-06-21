/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ws.klijenti;

/**
 * Klasa služi za komunikaciju s servisom
 * http://nwtis.foi.hr:8080/DZ3_Master/IoT_Master?wsdl
 * Ima samo dijelove za rad s IoT uređajem kako bi se odvojila
 * poslovna logika
 * 
 * @author Marko Domladovac
 */
public class IotKlijent {
    
    private final String korisnickoIme;
    
    private final String korisnickaLozinka;

    public IotKlijent(String korisnik, String lozinka) {
        this.korisnickoIme = korisnik;
        this.korisnickaLozinka = lozinka;
    } 

    public Boolean dodajNoviUredjajGrupi(int idUredjaj, String nazivUredjaj, String adresaUredjaj) {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.dodajNoviUredjajGrupi(korisnickoIme, korisnickaLozinka, 
                idUredjaj, nazivUredjaj, adresaUredjaj);
    }

    public boolean aktivirajUredjajGrupe(int idUredjaj) {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.aktivirajUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public boolean blokirajUredjajGrupe(int idUredjaj) {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.blokirajUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public boolean obrisiUredjajGrupe(int idUredjaj) {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.obrisiUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public StatusUredjaja dajStatusUredjajaGrupe(int idUredjaj) {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.dajStatusUredjajaGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }
    
    
}
