/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.ejb.sb.facades;

import java.util.Date;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.foi.nwtis.mdomladov.ejb.eb.Mqtt;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zeus
 */
public class MqttFacadeTest {
    
    public MqttFacadeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class MqttFacade.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Mqtt entity = new Mqtt();
        entity.setIotId(1);
        entity.setStatus("53");
        entity.setTekst("test");
        entity.setTrajanje(10000);
        entity.setVrijemeReceived(new Date());
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MqttFacade instance = (MqttFacade)container.getContext().lookup("java:global/classes/MqttFacade");
        instance.create(entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
