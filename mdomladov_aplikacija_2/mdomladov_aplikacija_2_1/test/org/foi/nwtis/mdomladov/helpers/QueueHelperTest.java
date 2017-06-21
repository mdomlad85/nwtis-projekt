/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.helpers;

import org.foi.nwtis.mdomladov.podaci.Statistika;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zeus
 */
public class QueueHelperTest {
    
    private QueueHelper nwtis1Queue;
    
    private QueueHelper nwtis2Queue;
    
    public QueueHelperTest() {
    }
    
    @Before
    public void setUp() {
        nwtis1Queue = new QueueHelper(QueueHelper.QUEUE_NWTIS_1);
        nwtis2Queue = new QueueHelper(QueueHelper.QUEUE_NWTIS_2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of send method, of class QueueHelper.
     */
    @Test
    public void testSend() {
        System.out.println("send");
        Statistika message = new Statistika(); 
        
        int brojPoruka = nwtis1Queue.getBrojPorukaIzReda();
        
        assertTrue(brojPoruka == 1);
    }
    
}
