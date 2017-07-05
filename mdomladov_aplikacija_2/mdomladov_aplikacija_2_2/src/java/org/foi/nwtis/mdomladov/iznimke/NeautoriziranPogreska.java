/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.iznimke;

/**
 *
 * @author Marko Domladovac
 */
public class NeautoriziranPogreska extends Exception {

    public NeautoriziranPogreska() {
        super("Niste autorizirani za traženu akciju.");
    }
    
}
