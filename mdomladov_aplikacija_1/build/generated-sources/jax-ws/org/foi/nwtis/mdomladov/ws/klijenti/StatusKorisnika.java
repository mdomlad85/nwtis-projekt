
package org.foi.nwtis.mdomladov.ws.klijenti;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statusKorisnika.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statusKorisnika">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PASIVAN"/>
 *     &lt;enumeration value="AKTIVAN"/>
 *     &lt;enumeration value="NEAKTIVAN"/>
 *     &lt;enumeration value="BLOKIRAN"/>
 *     &lt;enumeration value="NEPOSTOJI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "statusKorisnika")
@XmlEnum
public enum StatusKorisnika {

    PASIVAN,
    AKTIVAN,
    NEAKTIVAN,
    BLOKIRAN,
    NEPOSTOJI;

    public String value() {
        return name();
    }

    public static StatusKorisnika fromValue(String v) {
        return valueOf(v);
    }

}
