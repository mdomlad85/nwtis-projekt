/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.mdomladov.ejb.eb.Dnevnik;
import org.foi.nwtis.mdomladov.ejb.sb.facades.DnevnikFacade;

/**
 *
 * @author Marko Domladovac
 */
@ManagedBean
@RequestScoped
public class PregledDnevnika {

    @EJB
    private DnevnikFacade dnevnikDb;
    
    private List<Dnevnik> dnevnici;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledDnevnika() {
        
    } 
    
    @PostConstruct
    private void init(){
        this.dnevnici = dnevnikDb.findAll();
    }

    public List<Dnevnik> getDnevnici() {
        return dnevnici;
    }

    public void setDnevnici(List<Dnevnik> dnevnici) {
        this.dnevnici = dnevnici;
    }
       
    
}
