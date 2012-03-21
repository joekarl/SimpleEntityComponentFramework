/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author karl_ctr_kirch
 */
public class EntityManager {

    private static EntityManager sharedManager = null;

    public static EntityManager sharedManager() {
        if (sharedManager == null) {
            sharedManager = new EntityManager();
        }
        return sharedManager;
    }

    public EntityManager() {
    }
    private Long2ObjectOpenHashMap<Entity> entityMap = new Long2ObjectOpenHashMap<Entity>();
    private Map<String, List<Entity>> tagMap = new HashMap<String, List<Entity>>();
    private Map<Class<? extends Component>, List<EntityTriMap>> componentMaps =
            new HashMap<Class<? extends Component>, List<EntityTriMap>>();

    public final Entity createEntity() {
        Entity entity = new Entity(this);
        entityMap.put(entity.getId(), entity);
        return entity;
    }

    public final List<EntityTriMap> getComponentGroup(Class<? extends Component> componentType) {
        List<EntityTriMap> foundMap = componentMaps.get(componentType);
        if (foundMap == null) {
            return Collections.EMPTY_LIST;
        }
        return foundMap;
    }

    public final void removeComponentWithEntity(Component c, Entity e) {
        if (componentMaps.containsKey(c.getClass())) {
            List<EntityTriMap> foundMapList = componentMaps.get(c.getClass());
            tempTriMap.setId(e.getId());
            tempTriMap.setComponent(c);
            if (foundMapList.contains(tempTriMap)) {
                foundMapList.remove(tempTriMap);
            }
        }
    }
    private EntityTriMap tempTriMap = new EntityTriMap(-1, null, null);

    public final void addComponentWithEntity(Component c, Entity e,
            Class<? extends Component> componentType) {
        if (componentMaps.containsKey(componentType)) {
            //check to see if it exists
            List<EntityTriMap> foundMapList = componentMaps.get(componentType);
            tempTriMap.setId(e.getId());
            tempTriMap.setComponent(c);
            if (!foundMapList.contains(tempTriMap)) {
                foundMapList.add(new EntityTriMap(e, c));
            }
        } else {
            //list doesn't exist
            List<EntityTriMap> newMapList = new ArrayList<EntityTriMap>();
            componentMaps.put(componentType, newMapList);
            newMapList.add(new EntityTriMap(e, c));
        }
    }

    public final void addComponentWithEntity(Component c, Entity e) {
        addComponentWithEntity(c, e, c.getClass());
    }

    public final Entity getEntityById(long id) {
        return entityMap.get(id);
    }

    public final List<Entity> getEntitiesByTag(String tag) {
        List<Entity> entityList = tagMap.get(tag);
        if (entityList == null) {
            return Collections.EMPTY_LIST;
        }
        return entityList;
    }

    public final Entity getFirstEntityByTag(String tag) {
        List<Entity> entityList = tagMap.get(tag);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.get(0);
    }

    protected final void removeTagFromEntity(Entity e) {
        List<Entity> entityList = getEntitiesByTag(e.getTag());
        Iterator<Entity> iterator = entityList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(e)) {
                iterator.remove();
                break;
            }
        }
    }

    protected final void addTagToEntity(Entity e, String tag) {
        if (tag != null && e.getTag() != null && !tag.equals(e.getTag())) {
            removeTagFromEntity(e);
        }

        List<Entity> entityList = tagMap.get(tag);
        if (entityList == null) {
            entityList = new ArrayList<Entity>();
            tagMap.put(tag, entityList);
        }
        entityList.add(e);
    }

    public final void cleanDeadEntities() {
        ObjectCollection<Entity> entities = entityMap.values();
        ObjectIterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.isDead()) {
                entity.destroy();
                iterator.remove();

            }
        }
    }
}
