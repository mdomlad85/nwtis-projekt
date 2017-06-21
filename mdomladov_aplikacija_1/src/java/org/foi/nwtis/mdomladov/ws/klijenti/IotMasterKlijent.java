/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ws.klijenti;

/**
 * Klasa služi za komunikaciju s servisom
 * http://nwtis.foi.hr:8080/DZ3_Master/IoT_Master?wsdl
 * Ima samo dijelove za rad s IoT grupom kako bi se odvojila
 * poslovna logika
 * 
 * @author Marko Domladovac
 */
public class IotMasterKlijent {
    
    private final String korisnickoIme;
    
    private final String korisnickaLozinka;

    public IotMasterKlijent(String korisnik, String lozinka) {
        this.korisnickoIme = korisnik;
        this.korisnickaLozinka = lozinka;
    }  

    public Boolean registrirajGrupuIoT() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.registrirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public Boolean deregistrirajGrupuIoT() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.deregistrirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public Boolean aktivirajGrupuIoT() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.aktivirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public Boolean blokirajGrupuIoT() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.blokirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public boolean ucitajSveUredjajeGrupe() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.ucitajSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public Boolean obrisiSveUredjajeGrupe() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.obrisiSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public StatusKorisnika dajStatusGrupeIoT() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port = 
                service.getIoTMasterPort();
        
        return port.dajStatusGrupeIoT(korisnickoIme, korisnickaLozinka);
    }

    public java.util.List<Uredjaj> dajSveUredjajeGrupe() {
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service service = 
                new org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster_Service();
        
        org.foi.nwtis.mdomladov.ws.klijenti.IoTMaster port =
                service.getIoTMasterPort();
        
        return port.dajSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }
    
    
}
