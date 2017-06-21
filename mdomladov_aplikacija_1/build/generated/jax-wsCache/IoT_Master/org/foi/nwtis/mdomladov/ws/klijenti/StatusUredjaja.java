
package org.foi.nwtis.mdomladov.ws.klijenti;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for statusUredjaja.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="statusUredjaja">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PASIVAN"/>
 *     &lt;enumeration value="AKTIVAN"/>
 *     &lt;enumeration value="BLOKIRAN"/>
 *     &lt;enumeration value="NEPOSTOJI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "statusUredjaja")
@XmlEnum
public enum StatusUredjaja {

    PASIVAN,
    AKTIVAN,
    BLOKIRAN,
    NEPOSTOJI;

    public String value() {
        return name();
    }

    public static StatusUredjaja fromValue(String v) {
        return valueOf(v);
    }

}
