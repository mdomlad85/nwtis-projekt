/*
 * Copyright (C) 2017 Marko Domladovac
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.foi.nwtis.mdomladov.komande;

import java.util.HashMap;
import java.util.List;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.mdomladov.ws.klijenti.IotKlijent;
import org.foi.nwtis.mdomladov.ws.klijenti.IotMasterKlijent;
import org.foi.nwtis.mdomladov.ws.klijenti.StatusKorisnika;
import org.foi.nwtis.mdomladov.ws.klijenti.StatusUredjaja;
import org.foi.nwtis.mdomladov.ws.klijenti.Uredjaj;

/**
 * IoT_Master START; – registrira grupu. Vraća OK 10; ako nije bila
 * registrirana, odnosnoERR 20; ako je bila registrirana. • IoT_Master STOP; –
 * odjavljuje (deregistrira) grupu. Vraća OK 10; ako je bila registrirana,
 * odnosno ERR 21; ako nije bila registrirana. • IoT_Master WORK; – aktivira
 * grupu. Vraća OK 10; ako nije bila aktivna, odnosno ERR22; ako je bila
 * aktivna. • IoT_Master WAIT; – blokira grupu. Vraća OK 10; ako je bila
 * aktivna, odnosno ERR 23; ako nije bila aktivna. • IoT_Master LOAD; – učitava
 * predefinirane IoT uređaje u grupu. Vraća OK 10;. • IoT_Master CLEAR; – briše
 * sve IoT uređaje iz grupu. Vraća OK 10;. • IoT_Master STATUS; – vraća status
 * grupe. Vraća OK dd; gdje dd znači: 24 – blokirana, 25 – aktivna. • IoT_Master
 * LIST; – vraća id svih IoT uređaje iz grupi. Vraća OK 10; {IoT d{1-6} ″naziv″,
 * {IoT d{1-6} ″naziv″,...,{IoT d{1-6} ″naziv″}}};
 *
 * @author Marko Domladovac
 */
public class IoTMasterKomande {

    private enum Naredba {
        START,
        STOP,
        WORK,
        WAIT,
        LOAD,
        CLEAR,
        STATUS,
        LIST
    }

    private final HashMap<String, Naredba> naredbe;

    private final Naredba naredba;

    private final IotMasterKlijent iotMasterKlijent;

    public IoTMasterKomande(String naredba) {
        naredbe = new HashMap();
        naredbe.put("start", Naredba.START);
        naredbe.put("stop", Naredba.STOP);
        naredbe.put("work", Naredba.WORK);
        naredbe.put("wait", Naredba.WAIT);
        naredbe.put("load", Naredba.LOAD);
        naredbe.put("clear", Naredba.CLEAR);
        naredbe.put("status", Naredba.STATUS);
        naredbe.put("list", Naredba.LIST);

        this.naredba = naredbe.get(naredba.toLowerCase());

        APP_Konfiguracija konf = (APP_Konfiguracija) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);

        iotMasterKlijent = new IotMasterKlijent(konf.getServiceUsername(),
                konf.getServiceUserPassword());
    }

    public String aktivirajKomandu() {

        String response = null;
        int errNum = -1;
        StatusKorisnika statusKorisnika = iotMasterKlijent.dajStatusGrupeIoT();

        switch (naredba) {
            case START:
                if (statusKorisnika == StatusKorisnika.NEPOSTOJI
                        && iotMasterKlijent.registrirajGrupuIoT()) {

                    response = "OK 10;";
                } else {
                    errNum = 20;
                }
                break;
            case STOP:
                if (statusKorisnika != StatusKorisnika.NEPOSTOJI
                        && iotMasterKlijent.deregistrirajGrupuIoT()) {

                    response = "OK 10;";
                } else {
                    errNum = 21;
                }
                break;
            case WORK:
                if (statusKorisnika != StatusKorisnika.AKTIVAN
                        && iotMasterKlijent.aktivirajGrupuIoT()) {

                    response = "OK 10;";
                } else {
                    errNum = 22;
                }
                break;
            case WAIT:
                if (statusKorisnika == StatusKorisnika.AKTIVAN
                        && iotMasterKlijent.blokirajGrupuIoT()) {

                    response = "OK 10;";
                } else {
                    errNum = 23;
                }
                break;
            case LOAD:
                if (statusKorisnika != StatusKorisnika.NEPOSTOJI
                        && iotMasterKlijent.ucitajSveUredjajeGrupe()) {

                    response = "OK 10;";
                }
                break;
            case CLEAR:
                if (statusKorisnika != StatusKorisnika.NEPOSTOJI
                        && iotMasterKlijent.obrisiSveUredjajeGrupe()) {

                    response = "OK 10;";
                }
                break;
            case STATUS:
                switch (statusKorisnika) {
                    case PASIVAN:
                    case BLOKIRAN:
                    case NEAKTIVAN:
                        response = "OK 24;";
                        break;
                    case AKTIVAN:
                        response = "OK 25;";
                        break;
                }
                break;
            case LIST:
                if (statusKorisnika != StatusKorisnika.NEPOSTOJI) {

                    StringBuilder sb = new StringBuilder("OK 10; ");
                    List<Uredjaj> uredjaji = iotMasterKlijent.dajSveUredjajeGrupe();
                    
                    for (Uredjaj uredjaj : uredjaji) {
                        sb.append(String.format("IoT %d \"%s\"", 
                                uredjaj.getId(), uredjaj.getNaziv()));
                    }

                    response = sb.toString();
                }
        }

        if (errNum != -1) {
            response = String.format("ERR %d;", errNum);
        }

        return response;
    }

}
