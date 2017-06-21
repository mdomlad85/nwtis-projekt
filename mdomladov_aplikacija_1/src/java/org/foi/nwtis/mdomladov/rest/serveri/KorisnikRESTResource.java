/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.rest.serveri;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.mdomladov.dal.KorisnikDAL;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;

/**
 * REST Web Service
 *
 * @author Zeus
 */
public class KorisnikRESTResource {

    private String korisnickoIme;
    
    private final KorisnikDAL korisnikDb;

    /**
     * Creates a new instance of KorisnikRESTResource
     */
    private KorisnikRESTResource(String korisnickoIme, KorisnikDAL korisnikDb) {
        this.korisnickoIme = korisnickoIme;
        this.korisnikDb = korisnikDb;
    }

    /**
     * Creates a new instance of KorisnikRESTResource
     */
    private KorisnikRESTResource(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
        this.korisnikDb = new KorisnikDAL();
    }

    /**
     * Get instance of the KorisnikRESTResource
     */
    public static KorisnikRESTResource getInstance(String korisnickoIme) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of KorisnikRESTResource class.
        return new KorisnikRESTResource(korisnickoIme);
    }

    /**
     * Get instance of the KorisnikRESTResource
     */
    public static KorisnikRESTResource getInstance(String korisnickoIme, KorisnikDAL korisnikDb) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of KorisnikRESTResource class.
        return new KorisnikRESTResource(korisnickoIme, korisnikDb);
    }

    /**
     * Retrieves representation of an instance of org.foi.nwtis.mdomladov.rest.serveri.KorisnikRESTResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
       
        return JsonHelper.createJsonObjectString(
                korisnikDb.getKorisnik(korisnickoIme));
    }

    /**
     * PUT method for updating or creating an instance of KorisnikRESTResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource KorisnikRESTResource
     */
    @DELETE
    public void delete() {
    }
}
