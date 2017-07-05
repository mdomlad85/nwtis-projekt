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
import org.foi.nwtis.mdomladov.podaci.Jms2Poruka;

/**
 *
 * @author Zeus
 */
public class Jms2Coder implements Encoder.Text<Jms2Poruka>, Decoder.Text<Jms2Poruka> {

    @Override
    public String encode(Jms2Poruka poruka) throws EncodeException {
        return JsonHelper.createJsonObjectString(poruka);
    }

    @Override
    public Jms2Poruka decode(String s) throws DecodeException {
       return JsonHelper.parseJms2Poruka(s);
    }

    @Override
    public boolean willDecode(String s) {
        Jms2Poruka poruka = JsonHelper.parseJms2Poruka(s);
        return poruka != null;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
