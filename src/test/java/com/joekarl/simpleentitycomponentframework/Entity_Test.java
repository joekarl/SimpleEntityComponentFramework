/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author karl_ctr_kirch
 */
public class Entity_Test extends TestCase {

    public Entity_Test(String testName) {
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

    public void testAdd_Get_Component() {
        SECF_Entity entity = new SECF_Entity();

        SECF_Component originalComponent = entity.addComponent(TestComponent.class);

        Assert.assertTrue(entity.hasComponent(TestComponent.class));

        SECF_Component gottenComponent = entity.getComponent(TestComponent.class);

        Assert.assertEquals(originalComponent, gottenComponent);

        List<SECF_Component> gottenComponents = entity.getComponents(TestComponent.class);

        Assert.assertTrue(gottenComponents.size() == 1);
        Assert.assertEquals(gottenComponents.get(0), originalComponent);
    }

    public void testAdd_Remove_Component() {
        SECF_Entity entity = new SECF_Entity();

        SECF_Component originalComponent = entity.addComponent(TestComponent.class);

        Assert.assertTrue(entity.hasComponent(TestComponent.class));

        entity.removeComponent(originalComponent, TestComponent.class);

        Assert.assertFalse(entity.hasComponent(TestComponent.class));
    }

    public void testAdd_Remove_Components() {
        SECF_Entity entity = new SECF_Entity();
        
        entity.addComponent(TestComponent.class);

        Assert.assertTrue(entity.hasComponent(TestComponent.class));

        entity.removeComponents(TestComponent.class);

        Assert.assertFalse(entity.hasComponent(TestComponent.class));
    }
}

class TestComponent extends SECF_Component {
}