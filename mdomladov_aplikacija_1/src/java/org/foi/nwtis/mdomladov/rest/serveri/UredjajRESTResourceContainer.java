/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.rest.serveri;

import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.mdomladov.dal.UredjajDAL;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.rest.klijenti.GMKlijent;
import org.foi.nwtis.mdomladov.web.podaci.Lokacija;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 * REST Web Service
 *
 * @author Zeus
 */
@Path("/uredjaj")
public class UredjajRESTResourceContainer extends RESTAbstract {

    private final UredjajDAL db;

    /**
     * Creates a new instance of KorisnikRESTResourceContainer
     */
    public UredjajRESTResourceContainer() {
        APP_Konfiguracija konfiguracija = (APP_Konfiguracija) SlusacAplikacije.context.getAttribute(SlusacAplikacije.APP_KONFIG);
        db = new UredjajDAL(konfiguracija);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.mdomladov.rest.serveri.KorisnikRESTResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        startTime = System.currentTimeMillis();
        String response = JsonHelper.createJsonArrayString(db.getUredjaji());
        log();

        return response;
    }

    /**
     * Sub-resource locator method for {korisnickoIme}
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUredjajResource(@PathParam("id") String id) {
        startTime = System.currentTimeMillis();
        String response = UredjajRESTResource.getInstance(id, db).getJson();
        log();

        return response;
    }

    /**
     * POST method for creating an instance of KorisnikRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postJson(String content) {
        startTime = System.currentTimeMillis();
        boolean responseOk = false;
        Uredjaj uredjaj = JsonHelper.parsirajUredjaj(content);

        if (uredjaj != null) {

            Lokacija lokacija = getLokacijaZaAdresu(uredjaj.getGeoloc().getAdresa());
            if (lokacija != null) {
                uredjaj.setGeoloc(lokacija);
            }
            responseOk = db.addUredjaj(uredjaj);
            log();
        }

        return Response.ok(responseOk ? 1 : 0).build();
    }

    /**
     * POST method for creating an instance of KorisnikRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response putJson(String content) {
        startTime = System.currentTimeMillis();
        Boolean responseOk = null;
        Uredjaj uredjaj = JsonHelper.parsirajUredjaj(content);

        if (uredjaj != null) {

            Lokacija lokacija = getLokacijaZaAdresu(uredjaj.getGeoloc().getAdresa());
            if (lokacija != null) {
                uredjaj.setGeoloc(lokacija);
            }

            responseOk = db.editUredjaj(uredjaj);
            log();
        }

        return responseOk != null
                ? Response.ok(responseOk ? 1 : 0).build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * PUT method for updating or creating an instance of UredjajRESTResource
     *
     * @param id
     * @param content representation for the resource
     * @return
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response putJson(@PathParam("id") int id, String content) {
        startTime = System.currentTimeMillis();
        Boolean responseOk = null;
        Uredjaj uredjaj = JsonHelper.parsirajUredjaj(content);

        if (uredjaj != null) {
            uredjaj.setId(id);
            responseOk = db.editUredjaj(uredjaj);
            log();
        }

        return responseOk != null
                ? Response.ok(responseOk ? 1 : 0).build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Lokacija getLokacijaZaAdresu(String adresa) {
        if (adresa != null) {
            GMKlijent klijent = new GMKlijent();
            Lokacija lokacija = klijent.getGeoLocation(adresa);
            return lokacija;
        }
        return null;
    }
}
