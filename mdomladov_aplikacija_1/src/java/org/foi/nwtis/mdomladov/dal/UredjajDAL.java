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
import org.foi.nwtis.mdomladov.rest.klijenti.GMKlijent;
import org.foi.nwtis.mdomladov.web.podaci.Lokacija;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;

/**
 *
 * @author Marko Domaldovac
 */
public class UredjajDAL extends DBAbstract {

    private static Integer uredjajId;

    public UredjajDAL() {
    }

    public UredjajDAL(APP_Konfiguracija konfiguracija) {
        super(konfiguracija);
    }

    public List<Uredjaj> getUredjaji() {
        List<Uredjaj> uredjaji = null;
        try {
            openConnection();
            uredjaji = fillUredjaji();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return uredjaji;
    }

    public boolean addUredjaj(String naziv, String adresa) {
        boolean retVal = false;
        GMKlijent k = new GMKlijent();
        Lokacija lok = k.getGeoLocation(adresa);

        if (lok != null) {
            retVal = addUredjaj(new Uredjaj(0, naziv, lok));
        }
        return retVal;
    }

    public boolean addUredjaj(Uredjaj uredjaj) {
        boolean isValid = false;
        try {
            openConnection();
            pstmt = conn.prepareStatement(getInsertIotQuery());
            setParamtereInsert(uredjaj);
            isValid = pstmt.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    public boolean editUredjaj(Uredjaj uredjaj) {
        boolean isValid = false;
        try {
            openConnection();
            pstmt = conn.prepareStatement(getEditUredjajQuery());
            setParamtereEdit(uredjaj);
            isValid = pstmt.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    private List<Uredjaj> fillUredjaji() throws SQLException {
        List<Uredjaj> uredjaji = new ArrayList<>();
        rs = stmt.executeQuery("SELECT id, naziv, longitude, latitude, status FROM uredjaji");
        while (rs.next()) {
            uredjaji.add(getUredjaj());
        }

        return uredjaji;
    }

    public Uredjaj getUredjaj(String uredjajId) {
        Uredjaj uredjaj = null;
        try {
            openConnection();
            pstmt = conn.prepareStatement(getKorisnikByIdQuery());
            pstmt.setInt(1, Integer.parseInt(uredjajId));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                uredjaj = getUredjaj();
            }
        } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return uredjaj;
    }

    public int getZadnjiUredjajId() {

        try {
            openConnection();
            rs = stmt.executeQuery("SELECT MAX(id) FROM uredjaji");
            uredjajId = rs.next() ? rs.getInt(1) : null;
            closeDbResources();
        } catch (ClassNotFoundException | SQLException ex) {
            uredjajId = null;
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }

        return uredjajId;
    }

    private Uredjaj getUredjaj() throws SQLException {
        Uredjaj uredjaj = null;
        String latitude = String.valueOf(rs.getFloat("latitude"));
        String longitude = String.valueOf(rs.getFloat("longitude"));
        Lokacija lokacija = new Lokacija(latitude, longitude);
        uredjaj = new Uredjaj(rs.getInt("id"),
                rs.getString("naziv"), lokacija);
        uredjaj.setStatus(rs.getInt("status"));
        return uredjaj;
    }

    private String getInsertIotQuery() {
        StringBuilder query = new StringBuilder("INSERT INTO uredjaji ");
        query.append("(naziv, latitude, longitude, status, vrijeme_promjene, vrijeme_kreiranja)");
        query.append(" VALUES (?, ?, ?, ?, ?, ?)");
        return query.toString();
    }

    private String getKorisnikByIdQuery() {
        return "SELECT id, naziv, longitude, latitude FROM uredjaji where id=?";
    }

    private String getEditUredjajQuery() {
        StringBuilder sb = new StringBuilder("UPDATE uredjaji\n");
        sb.append("SET\n");
        sb.append("naziv = ?,\n");
        sb.append("latitude = ?,\n");
        sb.append("longitude = ?,\n");
        sb.append("status = ?\n,");
        sb.append("vrijeme_promjene = ?\n");
        sb.append("WHERE id = ?;");

        return sb.toString();
    }

    private void setParamtereEdit(Uredjaj u) throws SQLException {
        setParametre(u);
        pstmt.setInt(6, u.getId());
    }

    private void setParamtereInsert(Uredjaj u) throws SQLException {
        setParametre(u);
        pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
    }

    private void setParametre(Uredjaj u) throws SQLException {
        pstmt.setString(1, u.getNaziv());
        pstmt.setDouble(2, Double.parseDouble(u.getGeoloc().getLatitude()));
        pstmt.setDouble(3, Double.parseDouble(u.getGeoloc().getLongitude()));
        pstmt.setInt(4, u.getStatus());
        pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
    }
}
