/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import com.joekarl.simpleentitycomponentframework.components.Transform2D;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author karl_ctr_kirch
 */
public class EntityTest extends TestCase {

    public EntityTest(String testName) {
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

    public void testResetTag() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        entity.setTag("Tag 1");

        entity.setTag("Tag 2");

        Entity foundEntity = em.getFirstEntityByTag("Tag 2");
        Entity shouldBeNullEntity = em.getFirstEntityByTag("Tag 1");
        Assert.assertNull(shouldBeNullEntity);
        Assert.assertEquals(entity, foundEntity);

        List<Entity> entitiesWithTag = em.getEntitiesByTag("Tag 2");
        List<Entity> shouldBeEmptyList = em.getEntitiesByTag("Tag 1");

        Assert.assertTrue(entitiesWithTag.contains(entity));
        Assert.assertTrue(shouldBeEmptyList.isEmpty());
    }

    public void testRemoveTag() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        entity.setTag("Tag 1");

        entity.setTag(null);

        Entity shouldBeNullEntity = em.getFirstEntityByTag("Tag 1");
        Assert.assertNull(shouldBeNullEntity);

        List<Entity> shouldBeEmptyList = em.getEntitiesByTag("Tag 1");

        Assert.assertTrue(shouldBeEmptyList.isEmpty());
    }

    public void testAddComponent() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        final class TempComponent extends Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        Assert.assertEquals(tc, entity.getComponent(TempComponent.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));
    }
    
    public void testRemoveComponentNow() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        final class TempComponent extends Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        Assert.assertEquals(tc, entity.getComponent(TempComponent.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));

        entity.removeComponentNow(tc);

        Assert.assertNull(entity.getComponent(TempComponent.class));

        Assert.assertFalse(entity.getComponents().contains(tc));

        componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertFalse(componentGroup.contains(new EntityTriMap(entity, tc)));
    }

    public void testRemoveComponent() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        final class TempComponent extends Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        Assert.assertEquals(tc, entity.getComponent(TempComponent.class));

        Assert.assertTrue(entity.getComponents().contains(tc));

        List<EntityTriMap> componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertTrue(componentGroup.contains(new EntityTriMap(entity, tc)));

        entity.removeComponent(tc);
        
        em.cleanRemovedComponents();

        Assert.assertNull(entity.getComponent(TempComponent.class));

        Assert.assertFalse(entity.getComponents().contains(tc));

        componentGroup = em.getComponentGroup(TempComponent.class);

        Assert.assertFalse(componentGroup.contains(new EntityTriMap(entity, tc)));
    }

    public void testAddComponentAsOtherType() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();

        abstract class TestType extends Component {
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

    public void testKillEntity() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();
        long id = entity.getId();

        final class TempComponent extends Component {
        }

        TempComponent tc = new TempComponent();

        entity.addComponent(tc);

        entity.setTag("PLAYER");

        entity.setDead();

        em.cleanDeadEntities();

        Entity foundEntity = em.getEntityById(id);

        Assert.assertNull(foundEntity);

        Assert.assertTrue(entity.isDead());

        Assert.assertNull(entity.getComponent(TempComponent.class));

        Assert.assertTrue(em.getComponentGroup(TempComponent.class).isEmpty());

        Assert.assertNull(em.getFirstEntityByTag("PLAYER"));
    }
    
    public void testEntityHasTransform() {
        EntityManager em = new EntityManager();
        Entity entity = em.createEntity();
        
        Assert.assertNotNull(entity.getComponent(Transform2D.class));
    }

}
