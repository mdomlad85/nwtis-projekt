/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import org.foi.nwtis.mdomladov.podaci.Izbornik;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.mdomladov.iznimke.JezikNijePodrzanException;

/**
 *
 * @author Zeus
 */
@Named(value = "menuZrno")
@ApplicationScoped
public class MenuZrno extends ApstraktnoZrno {

    private static final Izbornik HR = new Izbornik("Croatian", "hr");
    private static final Izbornik EN = new Izbornik("English", "en");
    private static final Izbornik DE = new Izbornik("Deutsch", "de");

    /**
     *
     */
    public static final HashMap<String, Izbornik> JEZICI;

    static {
        JEZICI = new HashMap<>();
        JEZICI.put(HR.getVrijednost(), HR);
        JEZICI.put(EN.getVrijednost(), EN);
        JEZICI.put(DE.getVrijednost(), DE);
    }
    ;
    
    private Locale locale;

    /**
     * Creates a new instance of MenuZrno
     */
    public MenuZrno() {
        locale = new Locale(HR.getVrijednost());
    }

    /**
     *
     * @return
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     *
     * @param language
     * @throws JezikNijePodrzanException
     * @throws java.io.IOException
     */
    public void setLanguage(String language) throws JezikNijePodrzanException, IOException {
        Izbornik jezik = JEZICI.get(language);
        if (jezik != null) {
            locale = new Locale(language);
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
             FacesContext.getCurrentInstance().getExternalContext()
                     .redirect(((HttpServletRequest) FacesContext
                             .getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        } else {
            throw new JezikNijePodrzanException();
        }
    }

    /**
     *
     * @return
     */
    public Izbornik[] getJezici() {
        Izbornik[] jezici = null;

        if (locale != null) {
            String[] labele = jeziciBundle.getString("index_jeziciNaziv").split(",");
            String[] vrijednosti = jeziciBundle.getString("index_jeziciIso").split(",");
            jezici = new Izbornik[vrijednosti.length];
            for (int i = 0; i < vrijednosti.length; i++) {
                jezici[i] = new Izbornik(labele[i], vrijednosti[i]);
            }
        } else {
            jezici = new Izbornik[JEZICI.size()];
            jezici = JEZICI.values().toArray(jezici);
        }
        return jezici;
    }

}
