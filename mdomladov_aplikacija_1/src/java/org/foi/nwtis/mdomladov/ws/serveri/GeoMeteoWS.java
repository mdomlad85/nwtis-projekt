/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ws.serveri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.mdomladov.dal.DnevnikDAL;
import org.foi.nwtis.mdomladov.dal.MeteoDAL;
import org.foi.nwtis.mdomladov.dal.UredjajDAL;
import org.foi.nwtis.mdomladov.rest.klijenti.GMKlijent;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik;
import org.foi.nwtis.mdomladov.web.podaci.MeteoPodaci;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;

/**
 *
 * @author Zeus
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    @Resource
    private WebServiceContext context;

    private long startTime;

    /**
     * Web service operation
     *
     * @return
     */
    @WebMethod(operationName = "dajSveUredjaje")
    public List<Uredjaj> dajSveUredjaje() {
        startTime = System.currentTimeMillis();
        List<Uredjaj> uredjaji = new ArrayList<>();
        UredjajDAL uredjajDb = new UredjajDAL();
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        try {
            uredjaji = uredjajDb.getUredjaji();
        } catch (Exception ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.INFO, null, ex);
        }
        dnevnikDb.dodaj(getDnevnik("dajSveUredjaje"));

        return uredjaji;
    }

    /**
     * Web service operation
     *
     * @param naziv
     * @param adresa
     */
    @WebMethod(operationName = "dodajUredjaj")
    @Oneway
    public void dodajUredjaj(@WebParam(name = "naziv") String naziv, @WebParam(name = "adresa") String adresa) {
        startTime = System.currentTimeMillis();
        UredjajDAL uredjajDb = new UredjajDAL();
        if (uredjajDb.addUredjaj(naziv, adresa)) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.INFO, null,
                    String.format("Uređaj %s je uspješno pohranjen.", naziv));
        } else {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.INFO, null,
                    String.format("Došlo je do pogreške. Uređaj %s nije spremljen!", naziv));
        }
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        dnevnikDb.dodaj(getDnevnik("dodajUredjaj"));
    }

    /**
     * Web service operation
     *
     * @param uredjajId
     * @param intervalOd
     * @param intervalDo
     * @return
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaUredjaj")
    public List<MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(@WebParam(name = "uredjajId") int uredjajId, @WebParam(name = "intervalOd") long intervalOd, @WebParam(name = "intervalDo") long intervalDo) {
        startTime = System.currentTimeMillis();
        MeteoDAL meteoDb = new MeteoDAL();
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        dnevnikDb.dodaj(getDnevnik("dajSveMeteoPodatkeZaUredjaj"));
        return meteoDb.dajSveMeteoPodatkeZaUredjaj(uredjajId, intervalOd, intervalDo);
    }

    /**
     * Web service operation
     *
     * @param uredjajId - identifikator uredjaja
     * @return
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatkeZaUredjaj")
    public MeteoPodaci dajZadnjeMeteoPodatkeZaUredjaj(@WebParam(name = "uredjajId") int uredjajId) {
        startTime = System.currentTimeMillis();
        MeteoDAL meteoDb = new MeteoDAL();
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        dnevnikDb.dodaj(getDnevnik("dajZadnjeMeteoPodatkeZaUredjaj"));
        return meteoDb.dajZadnjeMeteoPodatkeZaUredjaj(uredjajId);
    }

    /**
     * Web service operation
     *
     * @param uredjajId
     * @param brojMeteoPodataka
     * @return
     */
    @WebMethod(operationName = "dajZadnjihNMeteoPodataka")
    public List<MeteoPodaci> dajZadnjihNMeteoPodataka(@WebParam(name = "uredjajId") int uredjajId,
            @WebParam(name = "brojMeteoPodataka") int brojMeteoPodataka) {
        startTime = System.currentTimeMillis();
        MeteoDAL meteoDb = new MeteoDAL();
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        dnevnikDb.dodaj(getDnevnik("dajZadnjihNMeteoPodataka"));
        return meteoDb.dajZadnjihNMeteoPodatakaZaUredjaj(uredjajId, brojMeteoPodataka);
    }

    /**
     * Web service operation
     *
     * @param uredjajId
     * @return
     */
    @WebMethod(operationName = "dajAdresuZauredjaj")
    public String dajAdresuZaUredjaj(@WebParam(name = "uredjajId") int uredjajId) {
        startTime = System.currentTimeMillis();
        UredjajDAL uredjajDb = new UredjajDAL();
        Uredjaj uredjaj = uredjajDb.getUredjaj(String.valueOf(uredjajId));
        GMKlijent gm = new GMKlijent();
        DnevnikDAL dnevnikDb = new DnevnikDAL();
        dnevnikDb.dodaj(getDnevnik("dajAdresuZauredjaj"));

        return gm.getAdresaByLocation(uredjaj.getGeoloc());
    }

    private Dnevnik getDnevnik(String endpoint) {
        HttpServletRequest se = (HttpServletRequest) context.getMessageContext()
                .get(MessageContext.SERVLET_REQUEST);
        
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik("mdomladov");
        dnevnik.setVrijeme(new Date());
        dnevnik.setUrl(se.getRequestURL().toString() + "/" + endpoint);
        dnevnik.setVrsta(Dnevnik.getVrste().get(Dnevnik.Vrsta.WS));
        dnevnik.setIpadresa(se.getRemoteAddr());
        dnevnik.setTrajanje((int) (System.currentTimeMillis() - startTime));

        return dnevnik;
    }

}
