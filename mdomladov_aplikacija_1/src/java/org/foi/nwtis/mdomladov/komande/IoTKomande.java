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
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.mdomladov.ws.klijenti.IotKlijent;
import org.foi.nwtis.mdomladov.ws.klijenti.StatusUredjaja;

/**
 * IoT d{1-6} ADD ″naziv″ ″adresa″; – dodaje IoT uređaj u grupu. Vraća OK 10;
 * ako ne postoji, odnosno ERR 30; ako postoji. • IoT d{1-6} WORK; – aktivira
 * IoT uređaj. Vraća OK 10; ako nije bio aktivan, odnosno ERR 31; ako je bio
 * aktivan. • IoT d{1-6} WAIT; – blokira IoT uređaj. Vraća OK 10; ako je bio
 * aktivan, odnosno ERR 32; ako nije bio aktivan. • IoT d{1-6} REMOVE; – briše
 * IoT uređaja iz grupe. Vraća OK 10; ako postoji, odnosno ERR 33; ako ne
 * postoji. • IoT d{1-6} STATUS; – vraća status IoT uređaja. Vraća OK dd; gdje
 * dd znači: 34 – blokiranan, 35 – aktivnan.
 *
 * @author Marko Domladovac
 */
public class IoTKomande {

    private enum Naredba {
        ADD,
        WORK,
        WAIT,
        REMOVE,
        STATUS
    }

    private final HashMap<String, Naredba> naredbe;

    private final Naredba naredba;

    private final IotKlijent iotKlijent;

    private String naziv;

    private String adresa;

    public IoTKomande(String naredba) {
        naredbe = new HashMap();
        naredbe.put("add", Naredba.ADD);
        naredbe.put("work", Naredba.WORK);
        naredbe.put("wait", Naredba.WAIT);
        naredbe.put("remove", Naredba.REMOVE);
        naredbe.put("status", Naredba.STATUS);

        this.naredba = naredbe.get(naredba.toLowerCase());

        APP_Konfiguracija konf = (APP_Konfiguracija) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);

        iotKlijent = new IotKlijent(konf.getServiceUsername(),
                konf.getServiceUserPassword());
    }

    public String aktivirajKomandu(int idUredjaj) {

        String response = null;
        int errNum = -1;
        StatusUredjaja statusUredjaja = iotKlijent.dajStatusUredjajaGrupe(idUredjaj);

        switch (naredba) {
            case ADD:
                if (!naziv.isEmpty() && !adresa.isEmpty()
                        && statusUredjaja == StatusUredjaja.NEPOSTOJI
                        && iotKlijent.dodajNoviUredjajGrupi(idUredjaj, naziv, adresa)) {

                    response = "OK 10;";
                } else {
                    errNum = 30;
                }
                break;
            case WORK:
                if (statusUredjaja != StatusUredjaja.AKTIVAN
                        && iotKlijent.aktivirajUredjajGrupe(idUredjaj)) {

                    response = "OK 10;";
                } else {
                    errNum = 31;
                }
                break;
            case WAIT:
                if (statusUredjaja != StatusUredjaja.BLOKIRAN
                        && iotKlijent.blokirajUredjajGrupe(idUredjaj)) {

                    response = "OK 10;";
                } else {
                    errNum = 32;
                }
                break;
            case REMOVE:
                if (statusUredjaja != StatusUredjaja.NEPOSTOJI
                        && iotKlijent.obrisiUredjajGrupe(idUredjaj)) {

                    response = "OK 10;";
                } else {
                    errNum = 33;
                }
                break;
            case STATUS:
                switch (statusUredjaja) {
                    case BLOKIRAN:
                        response = "OK 34;";
                        break;
                    case AKTIVAN:
                        response = "OK 35;";
                        break;
                }
                break;
        }
        
        if(errNum != -1){
            response = String.format("ERR %d;", errNum);
        }

        return response;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

}
