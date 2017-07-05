/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.iznimke.NeautoriziranPogreska;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.podaci.Korisnik;
import static org.foi.nwtis.mdomladov.web.filteri.KontrolaPristupa.KORISNIK_ATTRIBUTE;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Marko Domladovac
 */
public class SocketHelper {

    public static final String IOT_MASTER = "IoT_Master";

    public static final String IOT = "IoT";

    public static final String DEFAULT = "";

    public static String IOT_MASTER_LIST = "list";

    public static String IOT_MASTER_LIST_PATTERN = "IoT (\\d+) \"([^\"]+)\"";

    public static final String IOT_REMOVE = "remove";

    public static String IOT_ADD = "ADD";

    protected ResourceBundle jeziciBundle;

    public SocketHelper() {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        jeziciBundle = ResourceBundle
                .getBundle(String.format("org.foi.nwtis.mdomladov.i18n_%s", locale.getLanguage()));
    }

    public Korisnik getKorisnik() {
        Korisnik korisnik = null;
        HttpSession session = (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

        if (session != null) {
            korisnik = (Korisnik) session.getAttribute(KORISNIK_ATTRIBUTE);
        }

        return korisnik;
    }

    public String getKorisnikNaredba() {
        Korisnik korisnik = getKorisnik();

        return getKorisnikNaredba(korisnik);
    }

    public String getKorisnikNaredba(Korisnik korisnik) {
        return String.format("USER %s; PASSWD %s;",
                korisnik.getKorisnickoIme(), korisnik.getKorisnickaLozinka());
    }

    private String napraviNaredbu(String akcija, String dodatak) throws NeautoriziranPogreska {
        Korisnik korisnik = getKorisnik();

        if (korisnik == null) {
            throw new NeautoriziranPogreska();
        }

        StringBuilder naredba = new StringBuilder(getKorisnikNaredba(korisnik));
        naredba.append(" ");
        naredba.append(dodatak);
        naredba.append(" ");
        naredba.append(akcija.toUpperCase());
        naredba.append(";");

        return naredba.toString();
    }

    public String posaljiNoviUredjajNaredbu(int id, String naziv, String adresa) throws NeautoriziranPogreska {
        Korisnik korisnik = getKorisnik();

        if (korisnik == null) {
            throw new NeautoriziranPogreska();
        }
        
        String naredba = String.format("%s %s %s %s \"%s\" \"%s\";", 
                getKorisnikNaredba(korisnik), 
                IOT, id, IOT_ADD, naziv, adresa);
        
        return translateResponse(posalji(naredba), IOT_ADD, "");
    }

    public String posaljiNaredbu(String akcija, String dodatak) {
        String naredba;
        try {
            naredba = napraviNaredbu(akcija, dodatak);
        } catch (NeautoriziranPogreska ex) {
            return ex.getMessage();
        }
        return translateResponse(posalji(naredba), akcija, dodatak);
    }

    private String posalji(String naredba) {
        APP_Konfiguracija konfiguracija = SlusacAplikacije.getKonfiguracija();
        InputStreamReader is = null;
        OutputStream os = null;
        Socket socket = null;

        String response = null;

        try {
            socket = new Socket(konfiguracija.getSocketServer(),
                    konfiguracija.getSocketPort());
            //socket.setSoTimeout(5000);

            os = socket.getOutputStream();
            is = new InputStreamReader(socket.getInputStream(), "Cp1250");
            os.write(naredba.getBytes());
            os.flush();
            socket.shutdownOutput();
            StringBuilder sb = new StringBuilder();
            int znak;
            do {
                znak = is.read();
                if (znak != -1) {
                    sb.append((char) znak);
                }
            } while (znak != -1);

            response = sb.toString();

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(SocketHelper.class.getName()).log(Level.SEVERE, null, ex);
            response = "ERR";

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }

                if (socket != null) {
                    socket.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(SocketHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    private String translateResponse(String response, String akcija, String dodatak) {
        String msg = null;
        if (response.startsWith("OK")) {
            msg = String.format(jeziciBundle.getString("server_komande_uspjeh"),
                    String.format("%s %s", dodatak, akcija));
            if (akcija.toLowerCase().equals(IOT_MASTER_LIST)) {
                msg = response.replace("OK 10;", "");
            } else {
                for (int i = 11; i <= 100; i++) {
                    if (response.contains(String.valueOf(i))) {
                        msg = jeziciBundle.getString(String.format("server_komande_ok_%d", i));
                        break;
                    }
                }
            }
        } else if (response.startsWith("ERR")) {
            msg = String.format(jeziciBundle.getString("server_komande_pogreska"),
                    String.format("%s %s", dodatak, akcija));
            for (int i = 10; i <= 100; i++) {
                if (response.contains(String.valueOf(i))) {
                    msg = jeziciBundle.getString(String.format("server_komande_err_%d", i));
                    break;
                }
            }
        }
        
        if(msg == null){
            jeziciBundle.getString("server_komande_nepoznata_pogreska");
        }

        return msg;
    }
}
