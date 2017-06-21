/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dal;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik.Vrsta;

/**
 *
 * @author Zeus
 */
public class DnevnikDAL extends DBAbstract {

    public DnevnikDAL() {
    }

    public DnevnikDAL(APP_Konfiguracija konfiguracija) {
        super(konfiguracija);
    }

    public boolean dodaj(Dnevnik dnevnik) {
        boolean isValid = false;
        try {
            openConnection();
            pstmt = conn.prepareStatement(getInsertDnevnikQuery());
            napuniParamtere(dnevnik);
            isValid = pstmt.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    public List<Dnevnik> dajDnevnik() {
        List<Dnevnik> dnevnici = new ArrayList<>();
        try {
            openConnection();
            dnevnici = fillDnevnici();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return dnevnici;
    }

    public List<Dnevnik> dajDnevnikZaVrstu(Vrsta vrsta) {
        List<Dnevnik> dnevnici = new ArrayList<>();
        try {
            openConnection();
            dnevnici = fillDnevniciPoVrsti(vrsta);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return dnevnici;
    }

    private List<Dnevnik> fillDnevniciPoVrsti(Vrsta vrsta) throws SQLException {
        List<Dnevnik> dnevnici = new ArrayList<>();
        pstmt = conn.prepareStatement(getDnevniciPoVrstiQuery().toString());
        pstmt.setString(1, Dnevnik.getVrste().get(vrsta));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            dnevnici.add(getDnevnik());
        }

        return dnevnici;
    }

    private List<Dnevnik> fillDnevnici() throws SQLException {
        List<Dnevnik> dnevnici = new ArrayList<>();
        rs = stmt.executeQuery(getAllDnevniciQuery().toString());
        while (rs.next()) {
            dnevnici.add(getDnevnik());
        }

        return dnevnici;
    }

    private Dnevnik getDnevnik() throws SQLException {
        Dnevnik dnevnik = new Dnevnik();

        dnevnik.setId(rs.getInt("id"));
        dnevnik.setKorisnik(rs.getNString("korisnik"));
        dnevnik.setUrl(rs.getString("url"));
        dnevnik.setIpadresa(rs.getString("ip_adresa"));
        dnevnik.setVrijeme(rs.getTimestamp("vrijeme"));
        dnevnik.setTrajanje(rs.getInt("trajanje"));
        dnevnik.setStatus(rs.getInt("status"));
        dnevnik.setVrsta(rs.getString("vrsta"));

        return dnevnik;
    }

    private StringBuilder getAllDnevniciQuery() {
        StringBuilder sb = new StringBuilder("SELECT id, korisnik, url, ");
        sb.append("ip_adresa, vrijeme, trajanje, status, vrsta FROM dnevnik ");
        return sb;
    }

    private StringBuilder getDnevniciPoVrstiQuery() {
        StringBuilder sb = getAllDnevniciQuery();
        sb.append(" WHERE vrsta = ?");

        return sb;
    }

    private String getInsertDnevnikQuery() {
        StringBuilder sb = new StringBuilder("INSERT INTO dnevnik");
        sb.append("(korisnik, url, ip_adresa, vrijeme, trajanje, status, vrsta) ");
        sb.append("VALUES (?, ?, ?, ?, ?, ?, ?);");
        
        return sb.toString();
    }

    private void napuniParamtere(Dnevnik dnevnik) throws SQLException {
        pstmt.setString(1, dnevnik.getKorisnik());
        pstmt.setString(2, dnevnik.getUrl());
        pstmt.setString(3, dnevnik.getIpadresa());
        pstmt.setTimestamp(4, new Timestamp(dnevnik.getVrijeme().getTime()));
        pstmt.setInt(5, dnevnik.getTrajanje());
        pstmt.setInt(6, dnevnik.getStatus());
        pstmt.setString(7, dnevnik.getVrsta());
    }
}
