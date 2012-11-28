/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import com.joekarl.simpleentitycomponentframework.components.Renderable;
import com.joekarl.simpleentitycomponentframework.components.Transform2D;
import java.awt.Graphics2D;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author karl
 */
public class ComponentTest extends TestCase {

    public ComponentTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:

    public void testComponentAccess() {
        EntityManager em = new EntityManager();
        Entity e = em.createEntity();
        e.addComponent(new Transform2D());
        final int expectedVal = 50;
        e.addComponent(new Renderable() {
            @Override
            public void draw(Graphics2D g2d) {
                Transform2D transform = this.getEntity().getComponent(Transform2D.class);
                transform.x = expectedVal;
                transform.y = expectedVal;
            }
        });
        
        for (EntityTriMap triMap : em.getComponentGroup(Renderable.class)) {
            Renderable r = (Renderable) triMap.getComponent();
            r.draw(null);
        }
        Transform2D transform = e.getComponent(Transform2D.class);
        Assert.assertTrue(transform.x == expectedVal);
        Assert.assertTrue(transform.y == expectedVal);
        
    }
}
