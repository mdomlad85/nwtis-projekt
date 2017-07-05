/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Zeus
 */
@Entity
@Table(name = "MQTT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mqtt.findAll", query = "SELECT m FROM Mqtt m")
    , @NamedQuery(name = "Mqtt.findById", query = "SELECT m FROM Mqtt m WHERE m.id = :id")
    , @NamedQuery(name = "Mqtt.findByIotId", query = "SELECT m FROM Mqtt m WHERE m.iotId = :iotId")
    , @NamedQuery(name = "Mqtt.findByVrijemeReceived", query = "SELECT m FROM Mqtt m WHERE m.vrijemeReceived = :vrijemeReceived")
    , @NamedQuery(name = "Mqtt.findByTekst", query = "SELECT m FROM Mqtt m WHERE m.tekst = :tekst")
    , @NamedQuery(name = "Mqtt.findByTrajanje", query = "SELECT m FROM Mqtt m WHERE m.trajanje = :trajanje")
    , @NamedQuery(name = "Mqtt.findByStatus", query = "SELECT m FROM Mqtt m WHERE m.status = :status")})
public class Mqtt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IOT_ID")
    private int iotId;
    @Column(name = "VRIJEME_RECEIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeReceived;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TEKST")
    private String tekst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRAJANJE")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "STATUS")
    private String status;

    public Mqtt() {
    }

    public Mqtt(Integer id) {
        this.id = id;
    }

    public Mqtt(Integer id, int iotId, String tekst, int trajanje, String status) {
        this.id = id;
        this.iotId = iotId;
        this.tekst = tekst;
        this.trajanje = trajanje;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIotId() {
        return iotId;
    }

    public void setIotId(int iotId) {
        this.iotId = iotId;
    }

    public Date getVrijemeReceived() {
        return vrijemeReceived;
    }

    public void setVrijemeReceived(Date vrijemeReceived) {
        this.vrijemeReceived = vrijemeReceived;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mqtt)) {
            return false;
        }
        Mqtt other = (Mqtt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.mdomladov.ejb.eb.Mqtt[ id=" + id + " ]";
    }
    
}
