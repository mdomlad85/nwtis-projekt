/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import org.foi.nwtis.mdomladov.podaci.Izbornik;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.foi.nwtis.mdomladov.helpers.SocketHelper;
import org.foi.nwtis.mdomladov.iznimke.NeautoriziranPogreska;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Zeus
 */
@Named(value = "kontrolnaPlocaZrno")
@ViewScoped
public class KontrolnaPlocaZrno extends ApstraktnoZrno {

    private final SocketHelper socketHelper;

    private List<Izbornik> uredjajiSelectList;

    private String odabraniUredjajId;

    private String naziv;

    private String adresa;
    
    private int id;

    private String displayUredjaje = DISPLAY_NONE;

    private final static String DISPLAY_NONE = "display:none;";

    private final static String DISPLAY_BLOCK = "display:block;";

    /**
     * Creates a new instance of KontrolnaPlocaZrno
     */
    public KontrolnaPlocaZrno() {
        socketHelper = new SocketHelper();
        displayUredjaje = DISPLAY_NONE;
        odabraniUredjajId = "";
    }

    public void stopLoader() {
        RequestContext.getCurrentInstance().execute("PF('bui').hide();");
    }

    public void managerAction(String akcija) {
        String msg = socketHelper.posaljiNaredbu(akcija, SocketHelper.DEFAULT);
        setMessage(msg);
    }

    public void masterAction(String akcija) {
        String msg = socketHelper.posaljiNaredbu(akcija, SocketHelper.IOT_MASTER);
        if (akcija.toLowerCase().equals(SocketHelper.IOT_MASTER_LIST)) {
            napuniUredjajeSelectList(msg);
            displayUredjaje = DISPLAY_BLOCK;
        } else {
            displayUredjaje = DISPLAY_NONE;
            setMessage(msg);
        }
    }

    public void uredjajAction(String akcija) {
        String msg = String.format(jeziciBundle.getString("server_komande_pogreska"), akcija);
        if (odabraniUredjajId != null && !odabraniUredjajId.isEmpty()) {
            String dodatak = String.format("%s %s", SocketHelper.IOT, odabraniUredjajId);
            msg = socketHelper.posaljiNaredbu(akcija, dodatak);
            if (akcija.equals(SocketHelper.IOT_REMOVE)) {
                String uredjaji = socketHelper.posaljiNaredbu(SocketHelper.IOT_MASTER_LIST, SocketHelper.IOT_MASTER);
                napuniUredjajeSelectList(uredjaji);
            }
        }

        setMessage(msg);
    }

    public void dodajUredjaj() {
        String msg = String.format(jeziciBundle.getString("server_komande_pogreska"), SocketHelper.IOT_ADD);
        if (adresa != null && !adresa.isEmpty() && naziv != null && !naziv.isEmpty() && id != 0) {
            try {
                msg = socketHelper.posaljiNoviUredjajNaredbu(id, naziv, adresa);
            String uredjaji = socketHelper.posaljiNaredbu(SocketHelper.IOT_MASTER_LIST, SocketHelper.IOT_MASTER);
            napuniUredjajeSelectList(uredjaji);
            msg = jeziciBundle.getString("uspjeh_uredjaj_dodan");
            } catch (NeautoriziranPogreska ex) {
                Logger.getLogger(KontrolnaPlocaZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setMessage(String msgText) {
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_INFO;
        FacesMessage msg = new FacesMessage(jeziciBundle
                .getString("kontrolna_ploca_poruka_naslov"), msgText);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void napuniUredjajeSelectList(String msg) {
        uredjajiSelectList = new ArrayList<>();
        Pattern pattern = Pattern.compile(SocketHelper.IOT_MASTER_LIST_PATTERN);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            uredjajiSelectList.add(new Izbornik(matcher.group(2), matcher.group(1)));
        }
    }

    public List<Izbornik> getUredjajiSelectList() {
        return uredjajiSelectList;
    }

    public String getOdabraniUredjajId() {
        return odabraniUredjajId;
    }

    public void setOdabraniUredjajId(String odabraniUredjajId) {
        this.odabraniUredjajId = odabraniUredjajId;
    }

    public String getDisplayUredjaje() {
        return displayUredjaje;
    }

    public void setDisplayUredjaje(String displayUredjaje) {
        this.displayUredjaje = displayUredjaje;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
