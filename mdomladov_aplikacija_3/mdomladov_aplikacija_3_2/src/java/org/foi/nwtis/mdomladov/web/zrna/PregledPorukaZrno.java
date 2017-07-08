/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.mdomladov.ejb.sb.UpraviteljJmsZrno;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Statistika;

/**
 *
 * @author Marko Domladovac
 */
@Named(value = "pregledPorukaZrno")
@RequestScoped
public class PregledPorukaZrno {
    
    @EJB
    private UpraviteljJmsZrno jmsSingleton;

    /**
     * Creates a new instance of PregledPorukaZrno
     */
    public PregledPorukaZrno() {
    }
    
    public List<Statistika> getPoruke(){
        return jmsSingleton.getNwtis1Poruke();
    }
    
    public void obrisi(int porukaId){
        jmsSingleton.obrisiNwtis1Poruku(porukaId);
    }

    public void dodaj() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String jsonPoruka = params.get("poruka") != null ? params.get("poruka") : "";
        Statistika poruka = JsonHelper.parseJms1Poruka(params.get("poruka"));
        if (poruka != null) {
            jmsSingleton.addNwtis1Poruka(poruka);
        }
    }
}
