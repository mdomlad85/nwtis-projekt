/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.dretve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.mdomladov.dal.DnevnikDAL;
import org.foi.nwtis.mdomladov.dal.KorisnikDAL;
import org.foi.nwtis.mdomladov.komande.IoTKomande;
import org.foi.nwtis.mdomladov.komande.IoTMasterKomande;
import org.foi.nwtis.mdomladov.komande.ServerKomande;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.podaci.Dnevnik;
import org.foi.nwtis.mdomladov.web.podaci.Korisnik;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 *
 * @author Marko Domladovac
 */
class RadnaDretva extends Thread {

    private final Socket socket;

    private static int redniBroj = 1;

    private Matcher privateMatcher;

    private long startTime;

    private final KorisnikDAL korisnikDb;
    
    private final DnevnikDAL dnevnikDb;
    
    private Korisnik korisnik;

    public RadnaDretva(Socket socket) {
        korisnikDb = new KorisnikDAL();
        dnevnikDb = new DnevnikDAL();
        this.socket = socket;
        setName(String.format("zahtjev_%d", redniBroj++));
    }

    public RadnaDretva(Socket socket, APP_Konfiguracija konfig) {
        korisnikDb = new KorisnikDAL();
        dnevnikDb = new DnevnikDAL();
        this.socket = socket;
        setName(String.format("zahtjev_%d", redniBroj++));
    }

    public long getExecutionTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        super.run();
        System.out.println(this.getClass());

        String sintaksaKredencijali = "USER +(\\w+); *PASSWD +(\\w+);";

        String sintaksaServer = String.format("^%s *(PAUSE|STOP|START|STATUS);$", sintaksaKredencijali);
        String sintaksaIotMaster = String.format("^%s *IoT_Master (START|STOP|WORK|WAIT|LOAD|CLEAR|STATUS|LIST);$", sintaksaKredencijali);
        String sintaksaIot = String.format("^%s *IoT \\d{1,6} ((ADD \"(.+)\" \"(.+)\")|(WORK|WAIT|REMOVE|STATUS));$", sintaksaKredencijali);

        InputStream is = null;
        OutputStream os = null;
        try {
            String response = null;
            is = socket.getInputStream();
            os = socket.getOutputStream();

            StringBuffer sb = new StringBuffer();

            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }

            System.out.println("Primljena naredba:\t" + sb);
            setMatcher(sintaksaKredencijali, sb);
            boolean isValidUser = isValidUser();

            if (isValid(sintaksaServer, sb) && isValidUser) {
                ServerKomande komanda = new ServerKomande(privateMatcher.group(3));
                response = komanda.aktivirajKomandu();
            } else if (isValid(sintaksaIot, sb) && isValidUser) {

                IoTKomande komanda = privateMatcher.group(6) != null
                        ? new IoTKomande(privateMatcher.group(6))
                        : new IoTKomande(privateMatcher.group(4));

                response = komanda.aktivirajKomandu(Integer.parseInt(privateMatcher.group(3)));
            } else if (isValid(sintaksaIotMaster, sb) && isValidUser) {
                IoTMasterKomande komanda = new IoTMasterKomande(privateMatcher.group(3));
                response = komanda.aktivirajKomandu();
            }
            UpravljackaDretva uDretva = (UpravljackaDretva) SlusacAplikacije.getDretve()
                    .get(SlusacAplikacije.UPRAVLJAC_IME);            
            
            if (response == null) {
                if (!isValidUser) {
                    response = "ERR 10;";
                } else if (uDretva.getStanjeServera() == UpravljackaDretva.StanjeServera.STOPPED) {
                    response = "ERR 12;";
                } else {
                    //Neispravna komanda
                    response = "ERR 16;";
                }
            }
            dnevnikDb.dodaj(getDnevnik(sb));

            os.write(response.getBytes());
            os.flush();
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    private boolean isValid(String sintaksa, StringBuffer message) {
        setMatcher(sintaksa, message);
         UpravljackaDretva uDretva = (UpravljackaDretva) SlusacAplikacije.getDretve()
                    .get(SlusacAplikacije.UPRAVLJAC_IME);
        return privateMatcher.matches()
                && uDretva.getStanjeServera() != UpravljackaDretva.StanjeServera.STOPPED;
    }

    private void setMatcher(String sintaksa, StringBuffer message) {
        Pattern pattern = Pattern.compile(sintaksa);
        privateMatcher = pattern.matcher(message);
    }

    private boolean isValidUser() {
        if (privateMatcher.find()) {
            korisnik = korisnikDb
                    .getKorisnik(privateMatcher.group(1), 
                            privateMatcher.group(2));
        }

        return korisnik != null;
    }

    private Dnevnik getDnevnik(StringBuffer sb) {
        Dnevnik dnevnik = new Dnevnik();
            dnevnik.setKorisnik(korisnik.getKorisnickoIme());
            dnevnik.setVrijeme(new Date());
            dnevnik.setUrl(sb.toString());
            dnevnik.setVrsta(Dnevnik.getVrste().get(Dnevnik.Vrsta.KOMANDA));
            dnevnik.setIpadresa("127.0.0.1");
            dnevnik.setTrajanje((int) (System.currentTimeMillis() - startTime));
            
            return dnevnik;
    }

}
