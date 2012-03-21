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
public class Entity_Tests extends TestCase {

    public Entity_Tests(String testName) {
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

    public void testCreateEntity() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();
        Assert.assertTrue(entity != null);

        Entity foundEntity = em.getEntityById(entity.getId());
        Assert.assertEquals(entity, foundEntity);
    }

    public void testFindByTag() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();
        entity.setTag("PLAYER");
        Entity foundEntity = em.getFirstEntityByTag("PLAYER");
        Assert.assertEquals(entity, foundEntity);
    }

    public void testFindAllByTag() {
        EntityManager em = new EntityManager();
        Entity entity1 = em.createEntity();
        Entity entity2 = em.createEntity();
        Entity entity3 = em.createEntity();

        entity1.setTag("group");
        entity2.setTag("group");
        entity3.setTag("group");

        List<Entity> entitiesWithTag = em.getEntitiesByTag("group");
        Assert.assertTrue(entitiesWithTag.contains(entity1));
        Assert.assertTrue(entitiesWithTag.contains(entity2));
        Assert.assertTrue(entitiesWithTag.contains(entity3));
    }

    public void testAddComponent() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        final class TempComponent implements Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        Assert.assertEquals(tc, entity.getComponent(TempComponent.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));
    }

    public void testRemoveComponent() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        final class TempComponent implements Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        Assert.assertEquals(tc, entity.getComponent(TempComponent.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));

        entity.removeComponent(tc);

        Assert.assertNull(entity.getComponent(TempComponent.class));

        Assert.assertFalse(entity.getComponents().contains(tc));

        componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertFalse(componentGroup.contains(new EntityTriMap(entity, tc)));
    }

    public void testAddComponentAsOtherType() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        abstract class TestType implements Component {
        }

        final class TempComponent extends TestType {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc, TestType.class);

        Assert.assertEquals(tc, entity.getComponent(TestType.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TestType.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));
    }
}
