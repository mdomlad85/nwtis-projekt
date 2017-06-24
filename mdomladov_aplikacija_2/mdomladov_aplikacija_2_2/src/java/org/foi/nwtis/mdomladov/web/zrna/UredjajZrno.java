/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import okhttp3.Response;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Uredjaj;
import org.foi.nwtis.mdomladov.rest.klijenti.UredjajRESTKlijent;
import org.foi.nwtis.mdomladov.ws.klijenti.MeteoKlijent;
import org.foi.nwtis.mdomladov.ws.klijenti.MeteoPodaci;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Zeus
 */
@Named(value = "uredjajZrno")
@RequestScoped
public class UredjajZrno extends ApstraktnoZrno implements Serializable {

    public static final String DISPLAY_BLOCK = "block";
    
    public static final String DISPLAY_NONE = "none";

    private List<Uredjaj> uredjaji;

    private UredjajRESTKlijent uredjajREST;

    private Uredjaj uredjaj;

    private List<MeteoPodaci> meteoPodaci;
    
    private String displayPrognoze;

    /**
     * Creates a new instance of UredjajZrno
     */
    public UredjajZrno() {
        uredjaj = new Uredjaj();
        displayPrognoze = DISPLAY_NONE;
    }

    @PostConstruct
    private void init() {
        napuniUredjaje();
    }

    private void napuniUredjaje() throws ClientErrorException {
        uredjajREST = new UredjajRESTKlijent();
        String uredjajiJson = uredjajREST.getJson();
        uredjaji = JsonHelper.parseUredjaje(uredjajiJson);
    }

    public List<Uredjaj> getUredjaji() {
        return uredjaji;
    }

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }

    public void spremiUredjaj() {
        String msgTitle = jeziciBundle.getString("dodaj_uredjaj_pogreska_naslov");
        String msgText = jeziciBundle.getString("uredjaj_uredi_pogreska_tekst");
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_WARN;
        if (isAllSet()) {
            try {
                Response response = uredjajREST.postJson(uredjaj);
                if (response.isSuccessful()) {
                    msgTitle = jeziciBundle.getString("uredjaj_dodaj_uspjesno_naslov");
                    msgText = String
                            .format(jeziciBundle.getString("uredjaj_dodaj_uspjesno_tekst"), uredjaj.getNaziv());
                    napuniUredjaje();
                     uredjaj = new Uredjaj();
                }
            } catch (ClientErrorException | IOException ex) {
                Logger.getLogger(UredjajZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FacesMessage msg = new FacesMessage(msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowEdit(RowEditEvent event) {
        String msgTitle = jeziciBundle.getString("uredjaj_uredi_pogreska_naslov");
        String msgText = jeziciBundle.getString("uredjaj_uredi_pogreska_tekst");
        Uredjaj editUredjaj = (Uredjaj) event.getObject();
        try {
            Response response = uredjajREST.putJson(editUredjaj);
            if (response.isSuccessful()) {
                msgTitle = jeziciBundle.getString("uredjaj_uredi_uspjesno_naslov");
                msgText = jeziciBundle.getString("uredjaj_uredi_uspjesno_tekst");
            }
        } catch (ClientErrorException | IOException ex) {
            Logger.getLogger(UredjajZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesMessage msg = new FacesMessage(msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage(jeziciBundle.getString("uredjaj_uredi_prekinuto"),
                ((Uredjaj) event.getObject()).getNaziv());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private boolean isAllSet() {
        return uredjaj.getNaziv() != null && !uredjaj.getNaziv().isEmpty()
                && ((uredjaj.getGeoloc().getLatitude() != null && !uredjaj.getGeoloc().getLatitude().isEmpty()
                && uredjaj.getGeoloc().getLongitude() != null && !uredjaj.getGeoloc().getLongitude().isEmpty())
                || uredjaj.getGeoloc().getAdresa() != null && !uredjaj.getGeoloc().getAdresa().isEmpty());
    }

    public List<MeteoPodaci> getMeteoPodaci() {
        return meteoPodaci;
    }

    public void dajAdresu(int uredjajId) {
        FacesMessage msg = new FacesMessage(jeziciBundle.getString("uredjaj_adresa"),
                MeteoKlijent.dajAdresuZauredjaj(uredjajId));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void dajSvePrognoze(int uredjajId){
        displayPrognoze = DISPLAY_BLOCK;
        meteoPodaci = MeteoKlijent.dajZadnjihNMeteoPodataka(uredjajId, 50);
    }
    
    public void dajZadnjeMeteoPodatke(int uredjajId){        
        meteoPodaci = new ArrayList<>();
        meteoPodaci.add(MeteoKlijent.dajZadnjeMeteoPodatkeZaUredjaj(uredjajId));
        displayPrognoze = DISPLAY_BLOCK;
    }

    public String getDisplayPrognoze() {
        return displayPrognoze;
    }

    public void setDisplayPrognoze(String displayPrognoze) {
        this.displayPrognoze = displayPrognoze;
    }
}
