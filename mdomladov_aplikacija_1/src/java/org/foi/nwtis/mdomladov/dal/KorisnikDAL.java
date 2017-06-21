/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.podaci.Korisnik;

/**
 *
 * @author Zeus
 */
public class KorisnikDAL extends DBAbstract {

    private boolean ukljuciLozinku;

    public KorisnikDAL() {
    }

    public KorisnikDAL(APP_Konfiguracija konfiguracija) {
        super(konfiguracija);
    }

    public List<Korisnik> getKorisnici() {
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            openConnection();
            korisnici = fillKorisnici();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return korisnici;
    }

    public Korisnik getKorisnik(String korisnickoIme) {
        Korisnik korisnik = new Korisnik();
        try {
            openConnection();
            rs = stmt.executeQuery(
                    getKorisnikByImeQuery(korisnickoIme));

            if (rs.next()) {
                korisnik = getKorisnik();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return korisnik;
    }

    public Korisnik getKorisnik(String korisnickoIme, String lozinka) {
        Korisnik korisnik = null;
        try {
            openConnection();
            rs = stmt.executeQuery(
                    getKorisnikByImeLozinkaQuery(korisnickoIme, lozinka));

            if (rs.next()) {
                korisnik = getKorisnik();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return korisnik;
    }

    public boolean addKorisnik(Korisnik korisnik) {
        boolean isValid = false;
        try {
            String insertQuery = getInsertKorisnikQuery(korisnik);
            openConnection();
            isValid = stmt.executeUpdate(insertQuery) == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    public boolean editKorisnik(Korisnik korisnik) {
        boolean isValid = false;
        try {
            String editQuery = getEditKorisnikQuery(korisnik);
            openConnection();
            isValid = stmt.executeUpdate(editQuery) == 1;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDbResources();
        }
        return isValid;
    }

    private List<Korisnik> fillKorisnici() throws SQLException {
        List<Korisnik> korisnici = new ArrayList<>();
        rs = stmt.executeQuery(getAllKorisniciQuery(false).toString());
        while (rs.next()) {
            korisnici.add(getKorisnik());
        }

        return korisnici;
    }

    private Korisnik getKorisnik() throws SQLException {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(rs.getInt(1));
        korisnik.setIme(rs.getNString(2));
        korisnik.setPrezime(rs.getNString(3));
        korisnik.setKorisnickoIme(rs.getString(4));
        korisnik.setEmail(rs.getString(5));
        if (this.ukljuciLozinku) {
            korisnik.setKorisnickaLozinka(rs.getString(6));
        }
        return korisnik;
    }

    private String getKorisnikByImeQuery(String korisnickoIme) {
        StringBuilder sb = getAllKorisniciQuery(true);
        sb.append(" where korisnicko_ime = '");
        sb.append(korisnickoIme);
        sb.append("'");

        return sb.toString();
    }

    private String getKorisnikByImeLozinkaQuery(String korisnickoIme, String lozinka) {
        StringBuilder sb = getAllKorisniciQuery(true);
        sb.append(" where korisnicko_ime = '");
        sb.append(korisnickoIme);
        sb.append("' AND lozinka = '");
        sb.append(lozinka);
        sb.append("'");

        return sb.toString();
    }

    private StringBuilder getAllKorisniciQuery(boolean ukljuciLozinku) {
        this.ukljuciLozinku = ukljuciLozinku;
        StringBuilder sb = new StringBuilder("SELECT id, ime, prezime, korisnicko_ime, email ");
        if (ukljuciLozinku) {
            sb.append(" ,lozinka ");
        }
        sb.append("FROM korisnik");
        return sb;
    }

    private String getInsertKorisnikQuery(Korisnik korisnik) {
        StringBuilder query = new StringBuilder("INSERT INTO korisnik ");
        query.append("(ime, prezime, korisnicko_ime, lozinka, email) VALUES ('");
        query.append(korisnik.getIme());
        query.append("', '");
        query.append(korisnik.getPrezime());
        query.append("', '");
        query.append(korisnik.getKorisnickoIme());
        query.append("', '");
        query.append(korisnik.getKorisnickaLozinka());
        query.append("', '");
        query.append(korisnik.getEmail());
        query.append("')");

        return query.toString();
    }

    private String getEditKorisnikQuery(Korisnik korisnik) {
        StringBuilder query = new StringBuilder("UPDATE korisnik SET ");
        query.append(fillEditQuery("ime", korisnik.getIme(), false));
        query.append(fillEditQuery("prezime", korisnik.getPrezime(), false));
        query.append(fillEditQuery("korisnicko_ime", korisnik.getKorisnickoIme(), false));
        query.append(fillEditQuery("lozinka", korisnik.getKorisnickaLozinka(), false));
        query.append(fillEditQuery("email", korisnik.getEmail(), true));
        query.append(" WHERE id=");
        query.append(korisnik.getId());

        return query.toString();
    }

    public StringBuilder fillEditQuery(String labela, String value, boolean isLast) {
        StringBuilder query = new StringBuilder();
        if (value != null && !value.isEmpty()) {
            query.append(labela);
            query.append(" = '");
            query.append(value);
                query.append("'");

            if (!isLast) {
                query.append(",");
            }
        } else if (isLast) {
            query.deleteCharAt(query.length() - 2);
        }
        return query;
    }
}
