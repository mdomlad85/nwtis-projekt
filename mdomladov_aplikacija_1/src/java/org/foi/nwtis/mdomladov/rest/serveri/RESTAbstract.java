/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.rest.serveri;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.foi.nwtis.mdomladov.dal.DnevnikDAL;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik;

/**
 *
 * @author Marko Domladovac
 */
public abstract class RESTAbstract {

    @Context
    protected UriInfo context;

    @Context
    private HttpServletRequest request;

    protected long startTime;

    protected DnevnikDAL dnevnikDb;

    public RESTAbstract() {
        dnevnikDb = new DnevnikDAL();
    }
    
    protected void log(){
        dnevnikDb.dodaj(getDnevnik());
    }

    private Dnevnik getDnevnik() {
        String userInfo = context.getAbsolutePath().getUserInfo();
        String user = userInfo == null ? "mdomladov" : userInfo;
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik(user);
        dnevnik.setVrijeme(new Date());
        dnevnik.setUrl(context.getAbsolutePath().toString());
        dnevnik.setVrsta(Dnevnik.getVrste().get(Dnevnik.Vrsta.WS));
        dnevnik.setIpadresa(request.getRemoteAddr());
        dnevnik.setTrajanje((int) (System.currentTimeMillis() - startTime));

        return dnevnik;
    }
}
