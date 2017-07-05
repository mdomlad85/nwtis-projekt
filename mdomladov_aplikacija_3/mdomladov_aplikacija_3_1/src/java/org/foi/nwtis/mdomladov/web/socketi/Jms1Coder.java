/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.socketi;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.foi.nwtis.mdomladov.helpers.JsonHelper;
import org.foi.nwtis.mdomladov.podaci.Statistika;

/**
 *
 * @author Zeus
 */
public class Jms1Coder implements Encoder.Text<Statistika>, Decoder.Text<Statistika> {

    @Override
    public String encode(Statistika poruka) throws EncodeException {
        return JsonHelper.createJsonObjectString(poruka);
    }

    @Override
    public Statistika decode(String s) throws DecodeException {
       return JsonHelper.parseJms1Poruka(s);
    }

    @Override
    public boolean willDecode(String s) {
        Statistika poruka = JsonHelper.parseJms1Poruka(s);
        return poruka != null;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
