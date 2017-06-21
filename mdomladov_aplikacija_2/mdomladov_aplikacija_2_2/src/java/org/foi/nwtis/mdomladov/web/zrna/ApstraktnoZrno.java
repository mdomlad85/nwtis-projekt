/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author Zeus
 */
public class ApstraktnoZrno {

    protected ResourceBundle jeziciBundle;

    public ApstraktnoZrno() {
        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        jeziciBundle = ResourceBundle
                .getBundle(String.format("org.foi.nwtis.mdomladov.i18n_%s", locale.getLanguage()));
    }

}
