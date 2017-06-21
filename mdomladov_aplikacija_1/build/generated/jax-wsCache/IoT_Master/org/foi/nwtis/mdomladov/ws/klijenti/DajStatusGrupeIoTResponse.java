
package org.foi.nwtis.mdomladov.ws.klijenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dajStatusGrupeIoTResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dajStatusGrupeIoTResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://serveri.ws.dkermek.nwtis.foi.org/}statusKorisnika" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dajStatusGrupeIoTResponse", propOrder = {
    "_return"
})
public class DajStatusGrupeIoTResponse {

    @XmlElement(name = "return")
    protected StatusKorisnika _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link StatusKorisnika }
     *     
     */
    public StatusKorisnika getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusKorisnika }
     *     
     */
    public void setReturn(StatusKorisnika value) {
        this._return = value;
    }

}
