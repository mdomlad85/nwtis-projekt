/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Zeus
 */
@Named(value = "kontrolnaPlocaZrno")
@RequestScoped
public class KontrolnaPlocaZrno {

    /**
     * Creates a new instance of KontrolnaPlocaZrno
     */
    public KontrolnaPlocaZrno() {
    }
    
    public void stopLoader(){
         RequestContext.getCurrentInstance().execute("PF('bui').hide();");
    }
    
    public void managerAction(String action){
        RequestContext.getCurrentInstance().execute("PF('bui').show();");
        (new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KontrolnaPlocaZrno.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    RequestContext.getCurrentInstance().execute("PF('bui').show();");
                }
            }
        }).run();
        RequestContext.getCurrentInstance().execute("PF('bui').hide();");
    }
    
    public void masterAction(String action){
         RequestContext.getCurrentInstance().execute("PF('bui').show();");
        (new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KontrolnaPlocaZrno.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    RequestContext.getCurrentInstance().execute("PF('bui').show();");
                }
            }
        }).run();
        RequestContext.getCurrentInstance().execute("PF('bui').hide();");
    }
}
