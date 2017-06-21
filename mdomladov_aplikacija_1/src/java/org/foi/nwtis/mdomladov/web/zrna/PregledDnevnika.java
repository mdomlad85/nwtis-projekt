/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mdomladov.dal.DnevnikDAL;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik;

/**
 * Zrno koje se koristi za pregled dnevnika
 * 
 * @author Marko Domladovac
 */
@ManagedBean
@Named(value = "pregledDnevnika")
@RequestScoped
public class PregledDnevnika implements Serializable {
    
    private DnevnikDAL db;
    
    private List<Dnevnik> dnevnici;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
        dnevnici = new ArrayList<>();
    }
    
    @PostConstruct
    private void init() {
        db = new DnevnikDAL();
        dnevnici = db.dajDnevnikZaVrstu(Dnevnik.Vrsta.KOMANDA);
    }

    public List<Dnevnik> getDnevnici() {
        return dnevnici;
    }

    public void setDnevnici(List<Dnevnik> dnevnici) {
        this.dnevnici = dnevnici;
    }
    
}
