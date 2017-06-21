
package org.foi.nwtis.mdomladov.ws.klijenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dajStatusUredjajaGrupeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dajStatusUredjajaGrupeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://serveri.ws.dkermek.nwtis.foi.org/}statusUredjaja" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dajStatusUredjajaGrupeResponse", propOrder = {
    "_return"
})
public class DajStatusUredjajaGrupeResponse {

    @XmlElement(name = "return")
    protected StatusUredjaja _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link StatusUredjaja }
     *     
     */
    public StatusUredjaja getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusUredjaja }
     *     
     */
    public void setReturn(StatusUredjaja value) {
        this._return = value;
    }

}
