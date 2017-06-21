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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.mdomladov.dal.UredjajDAL;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author Zeus
 */
public class UredjajRESTResource {

    private String id;
    
    private final UredjajDAL uredjajDb;

    /**
     * Creates a new instance of KorisnikRESTResource
     */
    private UredjajRESTResource(String id, UredjajDAL uredjajDb) {
        this.id = id;
        this.uredjajDb = uredjajDb;
    }

    /**
     * Creates a new instance of UredjajResource
     */
    private UredjajRESTResource(String id) {
        this.id = id;
        uredjajDb = new UredjajDAL();
    }

    /**
     * Get instance of the UredjajRESTResource
     */
    public static UredjajRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of UredjajRESTResource class.
        return new UredjajRESTResource(id);
    }

    /**
     * Get instance of the UredjajRESTResource
     */
    public static UredjajRESTResource getInstance(String id, UredjajDAL uredjajDb) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of UredjajRESTResource class.
        return new UredjajRESTResource(id, uredjajDb);
    }

    /**
     * Retrieves representation of an instance of org.foi.nwtis.mdomladov.rest.serveri.UredjajRESTResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
         return JsonHelper.createJsonObjectString(
                uredjajDb.getUredjaj(id));
    }
}
