/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import org.foi.nwtis.mdomladov.podaci.Lokacija;
import org.foi.nwtis.mdomladov.podaci.Statistika;
import org.foi.nwtis.mdomladov.podaci.Uredjaj;

/**
 *
 * @author Zeus
 */
public class JsonHelper {

    private static JsonObjectBuilder jsonObj;

    public static <T> String createJsonArrayString(List<T> objekti) {
        JsonArrayBuilder objektiJson = Json.createArrayBuilder();

        objekti.forEach((t) -> {
            jsonObj = Json.createObjectBuilder();
            FillJsonObjekt(t);
            objektiJson.add(jsonObj);
        });

        return objektiJson.build().toString();
    }

    public static <T> String createJsonObjectString(T t) {
        jsonObj = Json.createObjectBuilder();
        FillJsonObjekt(t);

        return jsonObj.build().toString();
    }

    private static <T> void FillJsonObjekt(T t) {

        if (t != null) {
            Class<?> c = t.getClass();
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                if (m.getName().startsWith("get")) {
                    try {
                        Object val = m.invoke(t, null);
                        if (val != null) {
                            String name = String.format("%s%s",
                                    m.getName().substring(3, 4).toLowerCase(),
                                    m.getName().substring(4));
                            if (val instanceof Integer) {
                                jsonObj.add(name, (int) val);
                            } else if (val instanceof Float) {
                                jsonObj.add(name, (float) val);
                            } else if (val instanceof Double) {
                                jsonObj.add(name, (double) val);
                            } else if (val instanceof Long) {
                                jsonObj.add(name, (long) val);
                            } else if (val instanceof String) {
                                jsonObj.add(name, val.toString());
                            } else if (val instanceof String) {
                                jsonObj.add(name, val.toString());
                            } else if (val instanceof Date) {
                                Date datum = (Date) val;
                                jsonObj.add(name, ((Date) val).getTime());
                            } else if (val instanceof Lokacija) {
                                Lokacija lokacija = (Lokacija) val;
                                JsonObjectBuilder jsonLokacijaObj = Json.createObjectBuilder();
                                jsonLokacijaObj.add("latitude", lokacija.getLatitude());
                                jsonLokacijaObj.add("longitude", lokacija.getLongitude());

                                if (lokacija.getAdresa() != null) {
                                    jsonLokacijaObj.add("adresa", lokacija.getAdresa());
                                }
                                jsonObj.add(m.getName().substring(3).toLowerCase(), jsonLokacijaObj);
                            }
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(JsonHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static Uredjaj parsirajUredjaj(String uredjajStr) {
        Uredjaj uredjaj = null;

        try {
            JsonReader reader = Json.createReader(new StringReader(uredjajStr));
            JsonObject jo = reader.readObject();
            uredjaj = getUredjajFromJobj(jo);

        } catch (Exception ex) {
            uredjaj = null;
        }

        return uredjaj;
    }

    public static List<Korisnik> parseKorisnici(String korisniciJsonStr) {
        List<Korisnik> korisnici = new ArrayList<>();

        try {
            JsonReader reader = Json.createReader(new StringReader(korisniciJsonStr));
            JsonArray ja = reader.readArray();

            for (int i = 0; i < ja.size(); i++) {
                Korisnik korisnik = getKorisnikFromJobj(ja.getJsonObject(i));

                if (korisnik != null) {
                    korisnici.add(korisnik);
                }
            }
        } catch (Exception ex) {
            korisnici = null;
        }

        return korisnici;
    }

    public static List<Uredjaj> parseUredjaje(String uredjajiJson) {
        List<Uredjaj> uredjaji = new ArrayList<>();

        try {
            JsonReader reader = Json.createReader(new StringReader(uredjajiJson));
            JsonArray ja = reader.readArray();

            for (int i = 0; i < ja.size(); i++) {
                Uredjaj uredjaj = getUredjajFromJobj(ja.getJsonObject(i));

                if (uredjaj != null) {
                    uredjaji.add(uredjaj);
                }
            }
        } catch (Exception ex) {
            uredjaji = null;
        }

        return uredjaji;
    }

    public static Korisnik parseKorisnik(String korisnikJsonStr) {
        Korisnik korisnik = null;

        try {
            JsonReader reader = Json.createReader(new StringReader(korisnikJsonStr));
            JsonObject jo = reader.readObject();
            korisnik = getKorisnikFromJobj(jo);
        } catch (Exception ex) {
            korisnik = null;
        }

        return korisnik;
    }

    public static Jms2Poruka parseJms2Poruka(String jms2Json) {
        Jms2Poruka jms2 = null;

        try {
            JsonReader reader = Json.createReader(new StringReader(jms2Json));
            JsonObject jo = reader.readObject();
            jms2 = getJms2FromJobj(jo);
        } catch (Exception ex) {
            jms2 = null;
        }

        return jms2;
    }

    public static Statistika parseJms1Poruka(String jms1Json) {
        Statistika jms1 = null;

        try {
            JsonReader reader = Json.createReader(new StringReader(jms1Json));
            JsonObject jo = reader.readObject();
            jms1 = getJms1FromJobj(jo);
        } catch (Exception ex) {
            jms1 = null;
        }

        return jms1;
    }

    private static Korisnik getKorisnikFromJobj(JsonObject jo) {
        Korisnik korisnik = new Korisnik();
        if (jo.containsKey("id")) {
            korisnik.setId(jo.getInt("id"));
        }
        korisnik.setIme(getAndCheck(jo, "ime"));
        korisnik.setPrezime(getAndCheck(jo, "prezime"));
        korisnik.setKorisnickoIme(getAndCheck(jo, "korisnickoIme"));
        korisnik.setKorisnickaLozinka(getAndCheck(jo, "korisnickaLozinka"));
        korisnik.setEmail(getAndCheck(jo, "email"));
        return korisnik;
    }

    private static Uredjaj getUredjajFromJobj(JsonObject jo) {
        Uredjaj uredjaj = new Uredjaj();
        if (jo.containsKey("id")) {
            uredjaj.setId(jo.getInt("id"));
        }
        uredjaj.setNaziv(getAndCheck(jo, "naziv"));
        JsonObject joGeo = jo.getJsonObject("geoloc");
        String latitude = getAndCheck(joGeo, "latitude");
        String longitude = getAndCheck(joGeo, "longitude");

        Lokacija lokacija = new Lokacija(latitude, longitude);
        lokacija.setAdresa(getAndCheck(joGeo, "adresa"));
        uredjaj.setGeoloc(lokacija);
        uredjaj.setStatus(jo.getJsonNumber("status").intValue());

        return uredjaj;
    }

    private static Jms2Poruka getJms2FromJobj(JsonObject jo) {
         Jms2Poruka jms2 = new Jms2Poruka();
         jms2.setBrojJmsPoruke(jo.getJsonNumber("brojJmsPoruke").intValue());
         jms2.setBrojPorukaSlot(jo.getJsonNumber("brojPorukaSlot").intValue());
         jms2.setKrajSlota(jo.getJsonNumber("krajSlota").longValue());
         jms2.setPocetakSlota(jo.getJsonNumber("pocetakSlota").longValue());

        return jms2;
    }

    private static Statistika getJms1FromJobj(JsonObject jo) {
        Statistika jms1 = new Statistika();
         jms1.setBrojac(jo.getJsonNumber("brojac").intValue());
         jms1.setUkupnoIspravnihPoruka(jo.getJsonNumber("ukupnoIspravnihPoruka").intValue());
         jms1.setUkupnoPoruka(jo.getJsonNumber("ukupnoPoruka").intValue());
         jms1.setStartTime(jo.getJsonNumber("startTime").longValue());
         jms1.setStopTime(jo.getJsonNumber("stopTime").longValue());

        return jms1;
    }

    private static String getAndCheck(JsonObject jo, String key) {
        String val = "";
        if (jo.containsKey(key)) {
            val = jo.getString(key);
        }

        return val;
    }

}
