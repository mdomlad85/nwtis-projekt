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
import org.foi.nwtis.mdomladov.dal.KorisnikDAL;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.podaci.Korisnik;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 * REST Web Service
 *
 * @author Zeus
 */
@Path("/korisnik")
public class KorisnikRESTResourceContainer extends RESTAbstract {

    private final KorisnikDAL db;

    /**
     * Creates a new instance of KorisnikRESTResourceContainer
     */
    public KorisnikRESTResourceContainer() {
        APP_Konfiguracija konfiguracija = (APP_Konfiguracija) SlusacAplikacije.context.getAttribute(SlusacAplikacije.APP_KONFIG);
        db = new KorisnikDAL(konfiguracija);
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
        String response = JsonHelper.createJsonArrayString(db.getKorisnici());
        log();

        return response;
    }

    /**
     * Sub-resource locator method for {korisnickoIme}
     *
     * @param korisnickoIme
     * @return
     */
    @GET
    @Path("{korisnickoIme}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getKorisnikRESTResource(@PathParam("korisnickoIme") String korisnickoIme) {
        startTime = System.currentTimeMillis();
        String response = KorisnikRESTResource.getInstance(korisnickoIme, db).getJson();
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
        Korisnik korisnik = JsonHelper.parsirajKorisnika(content);

        if (korisnik != null) {
            responseOk = db.addKorisnik(korisnik);
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
        Korisnik korisnik = JsonHelper.parsirajKorisnika(content);

        if (korisnik != null) {
            responseOk = db.editKorisnik(korisnik);
            log();
        }

        return responseOk != null
                ? Response.ok(responseOk ? 1 : 0).build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }
}
