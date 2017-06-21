/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 * Klasa za rad s bazom podataka
 *
 * @author Marko Domladovac
 */
public abstract class DBAbstract {

    protected Connection conn;
    protected Statement stmt;
    protected ResultSet rs;
    protected PreparedStatement pstmt;

    protected final APP_Konfiguracija konfiguracija;

    public DBAbstract() {
        konfiguracija = (APP_Konfiguracija) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);
    }

    public DBAbstract(APP_Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    protected void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(konfiguracija
                .getDriverDatabase(konfiguracija.getServerDatabase()));
        if (conn == null) {
            conn = DriverManager
                    .getConnection(konfiguracija.getUrl(),
                            konfiguracija.getAdminUsername(),
                            konfiguracija.getAdminPassword());
        }

        if (stmt == null) {
            stmt = (Statement) conn.createStatement();
        }
    }

    protected void closeDbResources() {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
                pstmt = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBAbstract.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
