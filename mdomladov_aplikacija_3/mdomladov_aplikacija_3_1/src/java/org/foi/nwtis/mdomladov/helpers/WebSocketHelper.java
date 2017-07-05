/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import org.foi.nwtis.mdomladov.web.socketi.klijenti.WebsocketClientEndpoint;

/**
 *
 * @author Marko Domladovac
 */
public class WebSocketHelper {
    
    private final URI url;

    public WebSocketHelper(String endpoint) throws URISyntaxException {
        this.url = new URI("ws://localhost:16962/mdomladov_aplikacija_3_2/" + endpoint);
    }

    public WebSocketHelper() throws URISyntaxException {
        url = new URI("ws://localhost:16962/mdomladov_aplikacija_3_2/poruke");
    }  
    
    
    public void send(String jsonMsg){
        final WebsocketClientEndpoint  clientEndPoint = new WebsocketClientEndpoint(url);
        clientEndPoint.sendMessage(jsonMsg);
    }
}
