/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.dretve;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import org.foi.nwtis.mdomladov.ejb.sb.UpravljackoZrno;
import org.foi.nwtis.mdomladov.helpers.EmailHelper;
import org.foi.nwtis.mdomladov.helpers.QueueHelper;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mdomladov.podaci.Statistika;

/**
 * Klasa ObradaPoruka provjerava na poslužitelju (adresa i IMAP port određena
 * konfiguracijom) u pravilnom intervalu (određen konfiguracijom u sek) ima li
 * poruka u poštanskom sandučiću korisnika (adresa i lozinka određeni
 * konfiguracijom, npr. servis@nwtis.nastava.foi.hr, 123456). Koristi se IMAP
 * protokol. Poruke koje u predmetu (subject) imaju točno traženi sadržaj
 * (određen konfiguracijom, npr. NWTiS poruka) obrađuju se tako da se ispituje
 * sadržaj poruke. Poruka treba biti u "text/plain" formatu kako bi se provjerio
 * njen sadržaj
 *
 * Od pristiglih nepročitanih email poruka one koje imaju predmet poruke prema
 * postavkama iz konfiguracijske datoteke i MIME tip „text/plain“ nazivamo NWTiS
 * porukama. Obrađene NWTiS poruke treba označiti da su pročitane i prebaciti u
 * mapu/direktorij prema postavkama za NWTiS poruke. Ostale ne-NWTiS poruke
 * treba ostaviti da su nepročitane. Na kraju svake iteracije obrade email
 * poruka treba poslati JMS poruku (naziv reda čekanja NWTiS_{korisnicko_ime}_1)
 * s podacima o rednom broju JMS poruke koja se šalje, vremenu početka i
 * završetka rada iteracije dretve, broju pročitanih poruka, broju NWTiS poruka.
 * Poruka treba biti u obliku ObjectMessage, pri čemu je naziv vlastite klase
 * proizvoljan, a njena struktura treba sadržavati potrebne podatke koji su
 * prethodno spomenuti. Red čekanja treba ima vlastiti brojač JMS poruka.
 *
 * @author Marko Domladovac
 */
public class ObradaPoruka extends EmailHelper {

    private long startTime;

    /**
     * parametar trajanjeSpavanja definira koliko će trajati jedan ciklus u
     * milisekundama
     */
    private int interval;

    private Statistika statistika;
    
    private QueueHelper queueHelper;

    /**
     *
     */
    public ObradaPoruka() {
        inicijalizirajVarijable();
    }

    ObradaPoruka(String filepath) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        super(filepath);
        inicijalizirajVarijable();
    }

    private void inicijalizirajVarijable() {
        konfiguracija = UpravljackoZrno.getKonfiguracija();
        interval = konfiguracija.getTimeSecThread() * 1000;
        username = konfiguracija.getUsernameThread();
        password = konfiguracija.getPasswordThread();
        queueHelper = new QueueHelper(QueueHelper.QUEUE_NWTIS_1);
    }

    @Override
    public void run() {

        try {
            while (!this.isInterrupted()) {
                pripemiRad();
                messages = dohvatiPorukeZaMapu(INBOX_FOLDER, null, null, null);
                obradiNeprocitanePoruke();
                closeResources();
                zavrsiRad();
                sleep(getTrajanjeSpavanja());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ObradaPoruka.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void pripemiRad() {
        startTime = System.currentTimeMillis();
        statistika = new Statistika();
        statistika.setStartTime(startTime);
    }

    private void obradiNeprocitanePoruke() {
        for (Message message : messages) {
            statistika.ukupnoPoruka++;
            try {
                if (message.getContentType().toLowerCase()
                        .contains(ContentType.TEXT_PLAIN)
                        && message.getSubject()
                                .equals(konfiguracija.getSubject())) {
                    
                    kopirajPoruku(konfiguracija.geFolderNWTiS(),
                            store, message, folder);
                    statistika.ukupnoIspravnihPoruka++;
                }
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaPoruka.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void zavrsiRad() {
        statistika.setStopTime(System.currentTimeMillis());
        statistika.setBrojac(queueHelper.getBrojPorukaIzReda());
        queueHelper.send(statistika);
    }

    private void kopirajPoruku(String nazivDirektorija, Store store, Message message, Folder polazisni)
            throws MessagingException {

        Folder odredisni = store.getFolder(nazivDirektorija);
        if (!odredisni.exists()) {
            odredisni.create(Folder.HOLDS_MESSAGES);
        }
        odredisni.open(Folder.READ_WRITE);
        Message[] zaKopiranje = {message};
        polazisni.copyMessages(zaKopiranje, odredisni);
        odredisni.close(false);
        message.setFlag(Flags.Flag.SEEN, true);
    }

    @Override
    public synchronized void start() {
        Logger.getLogger(ObradaPoruka.class.getName()).log(Level.INFO,
                "ObradaPoruka je započela s radom", this);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interrupt() {
        Logger.getLogger(ObradaPoruka.class.getName()).log(Level.INFO,
                "ObradaPoruka je zavrsila s radom", this);
        super.interrupt();
    }

    /**
     *
     * @return
     */
    protected long getTrajanjeSpavanja() {
        int i = 1;
        long spavanje = 0;
        do {
            spavanje = i++ * interval - (System.currentTimeMillis() - startTime) / 1000;
        } while (spavanje < 0);

        return spavanje;
    }
    
    
}
