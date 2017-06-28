/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb;

import javax.ejb.Stateful;
import javax.ejb.LocalBean;

/**
 *
 * @author Zeus
 */
@Stateful
@LocalBean
public class ZrnoAutorizacije {

    public boolean autenticirajKorisnika(final String korisnickoIme, final String lozinka) {
        return false;
    }
}
