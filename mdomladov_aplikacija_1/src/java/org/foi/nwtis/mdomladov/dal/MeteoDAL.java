/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dal;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.rest.klijenti.GMKlijent;
import org.foi.nwtis.mdomladov.web.podaci.Lokacija;
import org.foi.nwtis.mdomladov.web.podaci.MeteoPodaci;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;

/**
 *
 * @author Marko Domladovac
 */
public class MeteoDAL extends DBAbstract {

    public MeteoDAL() {
    }

    public MeteoDAL(APP_Konfiguracija konfiguracija) {
        super(konfiguracija);
    }
    

    public boolean addMeteo(MeteoPodaci meteo, Uredjaj uredjaj) {
        boolean isValid = false;
        try {
            openConnection();
            isValid = stmt.executeUpdate(getInsertMeteoQuery(meteo, uredjaj)) == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    public List<MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(int id, long intervalOd, long intervalDo) {
        List<MeteoPodaci> meteoPodaci = null;
        try {
            openConnection();
            meteoPodaci = fillMeteoPodaci(id, intervalOd, intervalDo);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return meteoPodaci;
    }

    public List<MeteoPodaci> dajZadnjihNMeteoPodatakaZaUredjaj(int uredjajId, int nPodataka) {
        List<MeteoPodaci> meteoPodaci = null;
        try {
            openConnection();
            meteoPodaci = fillMeteoPodaci(uredjajId, nPodataka);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return meteoPodaci;
    }

    public MeteoPodaci dajZadnjeMeteoPodatkeZaUredjaj(int id) {
        MeteoPodaci meteo = null;
        try {
            openConnection();
            meteo = pronadi(id);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return meteo;
    }

    private MeteoPodaci pronadi(int id) throws SQLException {
        MeteoPodaci meteo = null;
        rs = stmt.executeQuery(getPronadiZadnjiMeteoQuery(id));

        if (rs.next()) {
            meteo = fillMeteoObjekt();
        }

        return meteo;
    }

    private List<MeteoPodaci> fillMeteoPodaci(int id, long intervalOd, long intervalDo) throws SQLException {
        List<MeteoPodaci> meteoPodaci = new ArrayList<>();
        rs = stmt.executeQuery(getSelectMeteoQuery(id, intervalOd, intervalDo));
        while (rs.next()) {
            MeteoPodaci meteo = fillMeteoObjekt();
            meteoPodaci.add(meteo);
        }

        return meteoPodaci.size() > 0 ? meteoPodaci : null;
    }

    private List<MeteoPodaci> fillMeteoPodaci(int uredjajId, int nPodataka) throws SQLException {
        List<MeteoPodaci> meteoPodaci = new ArrayList<>();
        rs = stmt.executeQuery(getSelectMeteoLastNQuery(uredjajId, nPodataka));
        while (rs.next()) {
            MeteoPodaci meteo = fillMeteoObjekt();
            meteoPodaci.add(meteo);
        }

        return meteoPodaci.size() > 0 ? meteoPodaci : null;
    }

    private MeteoPodaci fillMeteoObjekt() throws SQLException {
        MeteoPodaci meteo = new MeteoPodaci();
        meteo.setNaziv(rs.getString("naziv"));
        meteo.setLokacija(rs.getString("latitude"), rs.getString("longitude"));
        meteo.setWeatherIcon(rs.getString("vrijeme"));
        meteo.setWeatherValue(rs.getString("vrijemeOpis"));
        meteo.setTemperatureValue(rs.getFloat("temp"));
        meteo.setTemperatureMin(rs.getFloat("tempMin"));
        meteo.setTemperatureMax(rs.getFloat("tempMax"));
        meteo.setHumidityValue(rs.getFloat("vlaga"));
        meteo.setPressureValue(rs.getFloat("tlak"));
        meteo.setWindSpeedValue(rs.getFloat("vjetar"));
        meteo.setWindDirectionValue(rs.getFloat("vjetarSmjer"));
        meteo.setLastUpdate(new Date(rs.getTimestamp("preuzeto").getTime()));
        return meteo;
    }

    private String getInsertMeteoQuery(MeteoPodaci meteo, Uredjaj uredjaj) {
        Lokacija lokacija = uredjaj.getGeoloc();
        GMKlijent gmKlijent = new GMKlijent();
        String adresa = gmKlijent.getAdresaByLocation(lokacija);
        StringBuilder query = new StringBuilder("INSERT INTO meteo ");
        query.append("(uredjaj_id, adresa_stanice, latitude, longitude, vrijeme, vrijeme_opis, temp, temp_min, temp_max, vlaga, tlak, vjetar, vjetar_smjer) VALUES (");

        query.append(uredjaj.getId());
        query.append(", '");
        query.append(adresa);
        query.append("', ");
        query.append(lokacija.getLatitude());
        query.append(", ");
        query.append(lokacija.getLongitude());
        query.append(", '");
        query.append(meteo.getWeatherIcon());
        query.append("', '");
        query.append(meteo.getWeatherValue());
        query.append("', ");
        query.append(meteo.getTemperatureValue());
        query.append(", ");
        query.append(meteo.getTemperatureMin());
        query.append(", ");
        query.append(meteo.getTemperatureMax());
        query.append(", ");
        query.append(meteo.getHumidityValue());
        query.append(", ");
        query.append(meteo.getPressureValue());
        query.append(", ");
        query.append(meteo.getWindSpeedValue());
        query.append(", ");
        query.append(meteo.getWindDirectionValue());

        query.append("");
        query.append(")");

        return query.toString();
    }

    private String getSelectMeteoQuery(int id, long intervalOd, long intervalDo) {
        StringBuilder sb = getPronadiMeteoZaUredjajQuery(id);
        sb.append(dohvatiInterval(intervalOd, intervalDo));
        sb.append(" ORDER BY PREUZETO DESC");

        return sb.toString();
    }

    private String getSelectMeteoLastNQuery(int uredjajId, int nPodataka) {
        StringBuilder sb = new StringBuilder(getPronadiZadnjiMeteoQuery(uredjajId));
        sb.append(" LIMIT ");
        sb.append(nPodataka);

        return sb.toString();
    }

    private String getPronadiZadnjiMeteoQuery(int id) {
        StringBuilder sb = getPronadiMeteoZaUredjajQuery(id);
        sb.append(" ORDER BY PREUZETO DESC");

        return sb.toString();
    }

    private StringBuilder getPronadiMeteoZaUredjajQuery(int id) {
        StringBuilder sb = new StringBuilder("SELECT u.NAZIV, m.* FROM METEO m join UREDJAJI u on m.id=u.id where u.id =");
        sb.append(id);

        return sb;
    }

    private StringBuilder dohvatiInterval(long intervalOd, long intervalDo) {
        StringBuilder sb = new StringBuilder(" and preuzeto between '");
        sb.append(new Timestamp(intervalOd));
        sb.append("' and '");
        sb.append(new Timestamp(intervalDo));
        sb.append("'");

        return sb;
    }
}
