/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.xml.datatype.XMLGregorianCalendar;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;

/**
 * Pomoćno zrno sa metodama 
 * za pomoć pri prikazu
 * 
 * @author Marko Domladovac
 */
@ManagedBean
@Named(value = "pomocnoZrno")
@ApplicationScoped
public class PomocnoZrno {
    
    public static String FORMAT_DATUMA = "dd.MM.yyyy";
    
    public static String FORMAT_VREMENA = "HH:mm:ss";
    
    private String locale = "hr";
    
    private int velicinaStranica;
    
    private static APP_Konfiguracija konfiguracija;

    /**
     * Konstruktor PomocnoZrno
     */
    public PomocnoZrno() {
        if(konfiguracija == null){
            ServletContext context = (ServletContext) FacesContext
                    .getCurrentInstance().getExternalContext().getContext();
            String filePath = context.getRealPath("WEB-INF")
                    + File.separator + context.getInitParameter("konfiguracija");
            try {
                konfiguracija = new APP_Konfiguracija(filePath);
                velicinaStranica = konfiguracija.velicinaStranice();
            } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(PomocnoZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String formatDate(XMLGregorianCalendar datum) {
        if (datum != null) {
            Date dateObject = datum.toGregorianCalendar().getTime();
            SimpleDateFormat dt1 = new SimpleDateFormat(
                    String.format("%s %s", FORMAT_DATUMA, FORMAT_VREMENA));
            return dt1.format(dateObject);
        }
        return null;
    }

    public String getFormatDatuma() {
        return FORMAT_DATUMA;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getVelicinaStranica() {
        return velicinaStranica;
    }
}
