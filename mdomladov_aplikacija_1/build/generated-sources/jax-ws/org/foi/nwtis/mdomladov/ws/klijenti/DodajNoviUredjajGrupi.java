
package org.foi.nwtis.mdomladov.ws.klijenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dodajNoviUredjajGrupi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dodajNoviUredjajGrupi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="korisnickoIme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="korisnickaLozinka" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUredjaj" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nazivUredjaj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="adresaUredjaj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dodajNoviUredjajGrupi", propOrder = {
    "korisnickoIme",
    "korisnickaLozinka",
    "idUredjaj",
    "nazivUredjaj",
    "adresaUredjaj"
})
public class DodajNoviUredjajGrupi {

    protected String korisnickoIme;
    protected String korisnickaLozinka;
    protected int idUredjaj;
    protected String nazivUredjaj;
    protected String adresaUredjaj;

    /**
     * Gets the value of the korisnickoIme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    /**
     * Sets the value of the korisnickoIme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKorisnickoIme(String value) {
        this.korisnickoIme = value;
    }

    /**
     * Gets the value of the korisnickaLozinka property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKorisnickaLozinka() {
        return korisnickaLozinka;
    }

    /**
     * Sets the value of the korisnickaLozinka property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKorisnickaLozinka(String value) {
        this.korisnickaLozinka = value;
    }

    /**
     * Gets the value of the idUredjaj property.
     * 
     */
    public int getIdUredjaj() {
        return idUredjaj;
    }

    /**
     * Sets the value of the idUredjaj property.
     * 
     */
    public void setIdUredjaj(int value) {
        this.idUredjaj = value;
    }

    /**
     * Gets the value of the nazivUredjaj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivUredjaj() {
        return nazivUredjaj;
    }

    /**
     * Sets the value of the nazivUredjaj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivUredjaj(String value) {
        this.nazivUredjaj = value;
    }

    /**
     * Gets the value of the adresaUredjaj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresaUredjaj() {
        return adresaUredjaj;
    }

    /**
     * Sets the value of the adresaUredjaj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresaUredjaj(String value) {
        this.adresaUredjaj = value;
    }

}
