/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import javax.mail.search.StringTerm;
import static org.foi.nwtis.mdomladov.helpers.CoreHelper.konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;

/**
 * Klasa za rad s email porukama 
 * 
 * @author Marko Domladovac
 */
public abstract class EmailHelper extends CoreHelper {

    /**
     *
     * Inbox folder
     */
    protected static final String INBOX_FOLDER = "inbox";

    /**
     * korisničko ime
     */
    protected String username;

    /**
     *
     * lozinka
     */
    protected String password;

    /**
     * 
     * Tip sadržaja
     */
    public static class ContentType {

        /**
         *
         */
        public static final String TEXT_PLAIN = "text/plain";
    }

    /**
     *
     * Store
     */
    protected Store store = null;

    /**
     *
     * Folder
     */
    protected Folder folder = null;

    /**
     * 
     * Poruke
     */
    protected Message[] messages = null;

    /**
     * Creates a new instance of GlavnoZrno
     */
    public EmailHelper() {
    }

    /**
     * Creates a new instance of GlavnoZrno
     * @param filepath
     * @throws org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije
     * @throws org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija
     */
    public EmailHelper(String filepath) throws NemaKonfiguracije, NeispravnaKonfiguracija {
        super(filepath);
    }

    /**
     *
     * @return
     */
    protected Properties dajSmtpPostavke() {

        java.util.Properties properties = System.getProperties();

        properties.put(
                "mail.smtp.host",
                konfiguracija.getServer()
        );

        properties.put(
                "mail.smtp.port",
                konfiguracija.getPort()
        );

        return properties;
    }

    /**
     *
     * @param mapa
     * @param porukaOd
     * @param porukaDo
     * @param izraz
     * @return
     */
    protected Message[] dohvatiPorukeZaMapu(String mapa, Integer porukaOd, Integer porukaDo, String izraz) {
        
        try {
            spoji();
            inicijalizirajMapu(mapa);
            readEmails(porukaOd, porukaDo, izraz);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return messages;
    }
    
    protected void obrisi(String mapa, int messageNumber){
        try {
            spoji();
            inicijalizirajMapu(mapa);
            deleteEmail(messageNumber);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param nazivMape
     * @return
     */
    public int dohvatiUkupanBrojPorukaZaMapu(String nazivMape) {

        int ukupnoPoruka = -1;
        try {
            spoji();
            inicijalizirajMapu(nazivMape);
            ukupnoPoruka = folder.getMessageCount();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return ukupnoPoruka;
    }

    /**
     *
     * @return
     */
    protected Folder[] dohvatiMape() {
        try {
            spoji();
            return folder.list();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void deleteEmail(int messageNumber) {
        try {
            //čitanje mailova
            folder.open(Folder.READ_WRITE);
            Message message = folder.getMessage(messageNumber);
            message.setFlag(Flags.Flag.DELETED, true);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readEmails(Integer porukaOd, Integer porukaDo, String izraz) throws MessagingException {
        //čitanje mailova
        folder.open(Folder.READ_ONLY);
        if (porukaOd != null && porukaDo != null) {
            int porukaUMapi = folder.getMessageCount();
            int porukaDoTemp = porukaUMapi - porukaOd + 1;
            porukaOd = porukaUMapi - porukaDo + 1;
            porukaDo = porukaDoTemp;
            if (porukaOd < 1) {
                porukaOd = 1;
            }
            messages = folder.getMessages(porukaOd, porukaDo);
            if (izraz != null && !izraz.isEmpty()) {
                SearchTerm izrazPretrage = new StringTerm(izraz) {
                    @Override
                    public boolean match(Message message) {
                        try {
                            String contentType = message.getContentType().toLowerCase();
                            if (contentType.contains("text/plain")
                                    || contentType.contains("text/html")) {
                                String messageContent = message.getContent().toString();
                                if (messageContent.toLowerCase()
                                        .contains(pattern.toLowerCase())) {
                                    return true;
                                }
                            }
                            if (message.getSubject().toLowerCase()
                                    .contains(pattern.toLowerCase())) {
                                return true;
                            }
                        } catch (MessagingException | IOException ex) {
                            Logger.getLogger(EmailHelper.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                        return false;
                    }
                };
                messages = folder.search(izrazPretrage, messages);
            }
        } else {
            messages = folder.getMessages();
        }
    }

    private void spoji() throws MessagingException, NoSuchProviderException {
        Session session = Session.getDefaultInstance(System.getProperties(), null);
        store = session.getStore("imap");
        store.connect(
                konfiguracija.getServer(),
                username,
                password);
        //default folder handle
        folder = store.getDefaultFolder();
    }

    /**
     *
     * @param nazivMape
     * @throws MessagingException
     */
    protected void inicijalizirajMapu(String nazivMape) throws MessagingException {

        //dohvati inbox
        folder = folder.getFolder(nazivMape);

        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        } 
    }

    /**
     *
     */
    protected void closeResources() {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close(true);
            } catch (MessagingException ex) {
                Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (MessagingException ex) {
                Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param posiljatelj
     * @param primatelj
     * @param predmet
     * @param tekst
     * @throws MessagingException
     */
    public void saljiPoruku(String posiljatelj, String primatelj,
            String predmet, String tekst) throws MessagingException {

        Session session = Session.getDefaultInstance(dajSmtpPostavke());
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(posiljatelj));

        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(primatelj));

        message.setSubject(predmet);
        message.setText(tekst);
        message.setSentDate(new Date());

        // Send the messge
        Transport.send(message);
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }
}
