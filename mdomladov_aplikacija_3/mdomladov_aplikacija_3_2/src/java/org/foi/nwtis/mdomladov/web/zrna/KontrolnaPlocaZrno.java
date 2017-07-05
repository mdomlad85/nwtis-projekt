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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.foi.nwtis.mdomladov.ejb.sb.UpraviteljJmsZrno;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.helpers.SocketHelper;
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Zeus
 */
@Named(value = "kontrolnaPlocaZrno")
@ViewScoped
public class KontrolnaPlocaZrno extends ApstraktnoZrno {

    @EJB
    private UpraviteljJmsZrno jmsSingleton;

    private final SocketHelper socketHelper;

    /**
     * Creates a new instance of KontrolnaPlocaZrno
     */
    public KontrolnaPlocaZrno() {
        socketHelper = new SocketHelper();
    }

    public void stopLoader() {
        RequestContext.getCurrentInstance().execute("PF('bui').hide();");
    }

    public void masterAction(String akcija) {
        String msg = socketHelper.posaljiNaredbu(akcija, SocketHelper.IOT_MASTER);
        setMessage(msg);
    }

    public void setMessage(String msgText) {
        FacesMessage.Severity msgFlag = FacesMessage.SEVERITY_INFO;
        FacesMessage msg = new FacesMessage(jeziciBundle
                .getString("kontrolna_ploca_poruka_naslov"), msgText);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Jms2Poruka> getPoruke() {
        return jmsSingleton.getNwtis2Poruke();
    }

    public void obrisi(int porukaId) {
        jmsSingleton.obrisiNwtis2Poruku(porukaId);
    }

    public void dodaj() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String jsonPoruka = params.get("poruka") != null ? params.get("poruka") : "";
        Jms2Poruka poruka = JsonHelper.parseJms2Poruka(params.get("poruka"));
        if (poruka != null) {
            jmsSingleton.addNwtis2Poruka(poruka);
        }
    }
}
