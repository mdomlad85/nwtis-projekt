/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.rest.klijenti;

import org.foi.nwtis.mdomladov.helpers.OWMRESTHelper;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.mdomladov.web.podaci.Lokacija;
import org.foi.nwtis.mdomladov.web.podaci.MeteoPodaci;

/**
 *
 * @author nwtis_1
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    /**
     * Ključ je lokacija geokordinate (lokacija toString) Vrijednost je adresa
     * Nije static jer jednom po ciklusu pazim na redundanciju (ipak je trenutno
     * vrijeme - ostalo se treba riješiti intervalom)
     */
    private HashMap<String, MeteoPodaci> lokacije;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));

            JsonObject coord = jo.getJsonObject("coord");
            mp.setLokacija(coord.getJsonNumber("lat").bigDecimalValue().toPlainString(),
                    coord.getJsonNumber("lon").bigDecimalValue().toPlainString());
            mp.setNaziv(jo.getJsonString("name").getString());

            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");

            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");

            JsonObject wind = jo.getJsonObject("wind");
            mp.setWindSpeedValue(new Double(wind.getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");

            if(wind.containsKey("deg")){
            mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            } else {
                mp.setWindDirectionValue(-1F);
            }
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
            return mp;

        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Optimizacija 
     * 
     * @param geoloc
     * @return 
     */
    public MeteoPodaci getRealTimeWeather(Lokacija geoloc) {
        if (lokacije == null) {
            lokacije = new HashMap<>();
        }
        MeteoPodaci meteoPodaci = lokacije.get(geoloc.toString());
        
        if(meteoPodaci == null){
            meteoPodaci = getRealTimeWeather(geoloc.getLatitude(), 
                    geoloc.getLongitude());
        }
        
        return meteoPodaci;
    }
}
