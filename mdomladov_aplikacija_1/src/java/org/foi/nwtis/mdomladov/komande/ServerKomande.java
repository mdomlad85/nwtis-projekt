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
import org.foi.nwtis.mdomladov.web.dretve.PreuzmiMeteoPodatke;
import org.foi.nwtis.mdomladov.web.dretve.UpravljackaDretva;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Marko Domladovac
 *
 * USER korisnik; PASSWD lozinka; – autentikacija korisnika. Vraća ERR 10; ako
 * ne postoji korisnik ili ne odgovara lozinka. Odnosno, prelazi na obradu
 * ostalog dijela komande. • PAUSE; – privremeno prekida preuzimanje
 * meteoroloških podataka od sljedećeg ciklusa (i dalje može preuzimati
 * komande). Vraća OK 10; ako nije bio u pauzi, odnosno ERR 10; ako je bio u
 * pauzi. • START; – nastavlja s preuzimanjem meteoroloških podataka od
 * sljedećeg ciklusa. Vraća OK 10; ako je bio u pauzi, odnosno ERR 11; ako nije
 * bio u pauzi. • STOP; – potpuno prekida preuzimanje meteoroloških podataka i
 * preuzimanje komandi. I završava rad. Vraća OK 10; ako nije bio u postupku
 * prekida, odnosno ERR 12; ako je bio u postupku prekida. • STATUS; – vraća
 * trenutno stanje poslužitelja. Vraća OK dd; gdje dd znači: 13 – privremeno ne
 * preuzima podatke, 14 – preuzima podatke, 15 – ne preuzima podatke i
 * korisničke komande.
 */
public class ServerKomande {

    private enum Naredba {
        START,
        STOP,
        PAUSE,
        STAT
    }

    private final HashMap<String, Naredba> naredbe;

    private final Naredba naredba;

    public ServerKomande(String naredba) {
        naredbe = new HashMap();
        naredbe.put("start", Naredba.START);
        naredbe.put("stop", Naredba.STOP);
        naredbe.put("pause", Naredba.PAUSE);
        naredbe.put("status", Naredba.STAT);

        this.naredba = naredbe.get(naredba.toLowerCase());
    }

    public String aktivirajKomandu() {

        String response = null;
        UpravljackaDretva uDretva = (UpravljackaDretva) SlusacAplikacije.getDretve()
                .get(SlusacAplikacije.UPRAVLJAC_IME);
        PreuzmiMeteoPodatke pDretva = (PreuzmiMeteoPodatke) SlusacAplikacije.getDretve()
                .get(SlusacAplikacije.METEO_IME);

        switch (naredba) {
            case PAUSE:
                if (uDretva.getStanjeServera() == UpravljackaDretva.StanjeServera.PAUSED) {
                    response = "ERR 10;";
                } else {
                    uDretva.setStanjeServera(UpravljackaDretva.StanjeServera.PAUSED);
                    
                    synchronized (uDretva) {
                        uDretva.notify();
                    }

                    if (pDretva != null) {
                        synchronized (pDretva) {
                            pDretva.notify();
                        }
                    }
                }
                break;
            case START:
                if (uDretva.getStanjeServera() == UpravljackaDretva.StanjeServera.STARTED) {
                    response = "ERR 11;";
                } else {
                    uDretva.setStanjeServera(UpravljackaDretva.StanjeServera.STARTED);

                    synchronized (uDretva) {
                        uDretva.notify();
                    }

                    if (pDretva != null) {
                        synchronized (pDretva) {
                            pDretva.notify();
                        }
                    }
                }
                break;
            case STOP:
                if (uDretva.getStanjeServera() == UpravljackaDretva.StanjeServera.STOPPED) {
                    response = "ERR 12;";
                } else {
                    uDretva.setStanjeServera(UpravljackaDretva.StanjeServera.STOPPED);
                }
                break;
            case STAT:
                int respNum = -1;
                switch (uDretva.getStanjeServera()) {
                    case PAUSED:
                        respNum = 13;
                        break;
                    case STARTED:
                        respNum = 14;
                        break;
                    case STOPPED:
                        respNum = 15;
                        break;
                    default:
                        System.out.println("Server je u nepoznatom stanju");
                }
                response = String.format("OK %d;", respNum);
        }

        if (response == null) {
            response = "OK 10;";
        }

        return response;
    }
}
