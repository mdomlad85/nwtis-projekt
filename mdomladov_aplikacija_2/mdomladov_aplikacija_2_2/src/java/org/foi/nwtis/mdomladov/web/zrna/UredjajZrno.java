/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.IOException;
import java.io.Serializable;
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
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Zeus
 */
@Named(value = "uredjajZrno")
@RequestScoped
public class UredjajZrno extends ApstraktnoZrno implements Serializable {

    private List<Uredjaj> uredjaji;

    private UredjajRESTKlijent uredjajREST;

    private Uredjaj uredjaj;

    /**
     * Creates a new instance of UredjajZrno
     */
    public UredjajZrno() {
        uredjaj = new Uredjaj();
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
    

    public void onRowEdit(RowEditEvent event) {
        String msgTitle = jeziciBundle.getString("uredjaj_uredi_pogreska_naslov");
        String msgText = jeziciBundle.getString("uredjaj_uredi_pogreska_tekst");
        Uredjaj uredjaj = (Uredjaj) event.getObject();
        try {
            Response response = uredjajREST.putJson(uredjaj);
            msgTitle = jeziciBundle.getString("uredjaj_uredi_uspjesno_naslov");
            msgText = jeziciBundle.getString("uredjaj_uredi_uspjesno_tekst");
        } catch (ClientErrorException | IOException ex) {
            Logger.getLogger(UredjajZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesMessage msg = new FacesMessage(msgTitle, msgText);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Uredjaj) event.getObject()).getNaziv());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
