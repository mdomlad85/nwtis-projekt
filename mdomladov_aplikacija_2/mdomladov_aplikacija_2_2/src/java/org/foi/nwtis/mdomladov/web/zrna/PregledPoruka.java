/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import org.foi.nwtis.mdomladov.helpers.EmailHelper;
import org.foi.nwtis.mdomladov.podaci.Izbornik;
import org.foi.nwtis.mdomladov.podaci.Poruka;

/**
 *
 * @author Marko Domladovac
 */
@ManagedBean
@RequestScoped
public class PregledPoruka extends EmailHelper {

    private String adresaPosluzitelja;

    private String korisnickoIme;

    private String lozinka;

    private ArrayList<Izbornik> preuzeteMape;

    private static String izabranaMapa;

    private HashMap<String, Integer> ukupnoPoMapi;

    private ArrayList<Poruka> preuzetePoruke;

    private int ukupanBrojPoruka;
    private static final String MAPA_SEPARATOR = " - ";

    /**
     * Creates a new instance of PregledPoruka
     */
    public PregledPoruka() {
        username = konfiguracija.getUsernameView();
        password = konfiguracija.getPasswordView();
        preuzmiMape();
        if (izabranaMapa == null && preuzeteMape.size() > 0) {
            izabranaMapa = preuzeteMape.get(0).getVrijednost();
        }
        preuzmiPoruke();
    }

    private void preuzmiMape() {
        preuzeteMape = new ArrayList<>();
        ukupnoPoMapi = new HashMap<>();

        try {

            Folder[] mape = dohvatiMape();
            for (Folder mapa : mape) {
                ukupnoPoMapi.put(mapa.getName(), mapa.getMessageCount());
                ukupanBrojPoruka += mapa.getMessageCount();
                preuzeteMape.add(new Izbornik(mapa.getName() + MAPA_SEPARATOR
                        + mapa.getMessageCount(), mapa.getName()));
            }
            closeResources();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preuzmiPoruke() {
        preuzetePoruke = new ArrayList<>();

        int brojPoruka = ukupnoPoMapi.get(izabranaMapa);

        Message[] poruke = dohvatiPorukeZaMapu(izabranaMapa, null, null, null);

        for (Message message : poruke) {
            preuzetePoruke.add(Poruka
                    .createFromMessageObj(message));
        }
        Collections.reverse(preuzetePoruke);

        closeResources();
    }

    public void obrisi(String idStr) {
        Iterator<Poruka> listIterator = preuzetePoruke.listIterator();
        while (listIterator.hasNext()) {
            Poruka p = listIterator.next();
            if (p.getId().equals(idStr)) {
                listIterator.remove();
                break;
            }
        }
        int count = dohvatiUkupanBrojPorukaZaMapu(izabranaMapa);
        for (Izbornik izbornik : preuzeteMape) {
            if(izbornik.getVrijednost().equals(izabranaMapa)){
                izbornik.setLabela(String.format("%s - %s", izabranaMapa, --count));
            }
        }
        
        int id = Integer.parseInt(idStr);
        obrisi(izabranaMapa, id);
    }

    /**
     *
     */
    public void promjenaMape() {
        int brojPoruka = ukupnoPoMapi.get(izabranaMapa);
        preuzmiPoruke();
    }

    /**
     *
     * @return
     */
    public String getAdresaPosluzitelja() {
        return adresaPosluzitelja;
    }

    /**
     *
     * @param adresaPosluzitelja
     */
    public void setAdresaPosluzitelja(String adresaPosluzitelja) {
        this.adresaPosluzitelja = adresaPosluzitelja;
    }

    /**
     *
     * @return
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     *
     * @param korisnickoIme
     */
    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    /**
     *
     * @return
     */
    public String getLozinka() {
        return lozinka;
    }

    /**
     *
     * @param lozinka
     */
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     *
     * @return
     */
    public ArrayList<Izbornik> getPreuzeteMape() {
        return preuzeteMape;
    }

    /**
     *
     * @param preuzeteMape
     */
    public void setPreuzeteMape(ArrayList<Izbornik> preuzeteMape) {
        this.preuzeteMape = preuzeteMape;
    }

    /**
     *
     * @return
     */
    public String getIzabranaMapa() {
        return izabranaMapa;
    }

    /**
     *
     * @param izabranaMapa
     */
    public void setIzabranaMapa(String izabranaMapa) {
        this.izabranaMapa = izabranaMapa;
    }

    /**
     *
     * @return
     */
    public ArrayList<Poruka> getPreuzetePoruke() {
        return preuzetePoruke;
    }

    /**
     *
     * @param preuzetePoruke
     */
    public void setPreuzetePoruke(ArrayList<Poruka> preuzetePoruke) {
        this.preuzetePoruke = preuzetePoruke;
    }

    /**
     *
     * @return
     */
    public int getUkupanBrojPoruka() {
        return ukupanBrojPoruka;
    }

    /**
     *
     * @param ukupanBrojPorukaMapa
     */
    public void setUkupanBrojPoruka(int ukupanBrojPorukaMapa) {
        this.ukupanBrojPoruka = ukupanBrojPorukaMapa;
    }

}
