/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.rest.klijenti;

import java.io.IOException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Korisnik;

/**
 * Jersey REST client generated for REST resource:KorisnikRESTResourceContainer
 * [/korisnik]<br>
 * USAGE:
 * <pre>
 *        KorisnikRESTKlijent client = new KorisnikRESTKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Zeus
 */
public class KorisnikRESTKlijent {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI 
            = "http://localhost:29000/mdomladov_aplikacija_1/webresources";
    
    private static final String KORISNIK_ENDPOINT = "korisnik";
    
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public KorisnikRESTKlijent() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path(KORISNIK_ENDPOINT);
    }

    public Response putJson(Korisnik korisnik) throws IOException {
        OkHttpClient klijent = new OkHttpClient();
        String json = JsonHelper.createJsonObjectString(korisnik);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(String.format("%s/%s", BASE_URI, KORISNIK_ENDPOINT))
                .put(body)
                .addHeader("content-type", "application/json")
                .build();

        return klijent.newCall(request).execute();
    }

    public String getKorisnikRESTResource(String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{korisnickoIme}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public Response postJson(Korisnik korisnik) throws IOException {
        OkHttpClient klijent = new OkHttpClient();
        String json = JsonHelper.createJsonObjectString(korisnik);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(String.format("%s/%s", BASE_URI, KORISNIK_ENDPOINT))
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        return klijent.newCall(request).execute();
    }

    public String getJson() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }

}
