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

    /*
     * shared entity manager
     */
    public static EntityManager sharedManager() {
        if (sharedManager == null) {
            sharedManager = new EntityManager();
        }
        return sharedManager;
    }

    public EntityManager() {
    }
    /*
     * entity map for fast access by id
     */
    private Long2ObjectOpenHashMap<Entity> entityMap = new Long2ObjectOpenHashMap<Entity>();
    /*
     * tag map for fast access by tag
     */
    private Map<String, List<Entity>> tagMap = new HashMap<String, List<Entity>>();
    /*
     * component maps for fast access by component type
     * stored as EntityTriMap for fast lookup
     */
    private Map<Class<? extends Component>, List<EntityTriMap>> componentMaps =
            new HashMap<Class<? extends Component>, List<EntityTriMap>>();

    /*
     * creates an entity and inserts it into the entity map
     */
    public final Entity createEntity() {
        Entity entity = new Entity(this);
        entityMap.put(entity.getId(), entity);
        return entity;
    }

    /*
     * query the component map to find entities by component
     * returns EntityTriMap list 
     */
    public final List<EntityTriMap> getComponentGroup(Class<? extends Component> componentType) {
        List<EntityTriMap> foundMap = componentMaps.get(componentType);
        if (foundMap == null) {
            return Collections.EMPTY_LIST;
        }
        return foundMap;
    }

    /*
     * removes component from component map for entity
     */
    protected final void removeComponentWithEntity(Component c, Entity e) {
        if (componentMaps.containsKey(c.getClass())) {
            List<EntityTriMap> foundMapList = componentMaps.get(c.getClass());
            tempTriMap.setId(e.getId());
            tempTriMap.setComponent(c);
            if (foundMapList.contains(tempTriMap)) {
                foundMapList.remove(tempTriMap);
            }
        }
    }
    /*
     * shared tempTriMap for fast add and remove lookups
     */
    private EntityTriMap tempTriMap = new EntityTriMap(-1, null, null);

    /*
     * adds component to component map for entity 
     */
    protected final void addComponentWithEntity(Component c, Entity e,
            Class<? extends Component> componentType) {
        c.entity = e;
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

    /*
     * overload for more generic add component 
     */
    public final void addComponentWithEntity(Component c, Entity e) {
        addComponentWithEntity(c, e, c.getClass());
    }

    /*
     * find by entity id
     */
    public final Entity getEntityById(long id) {
        return entityMap.get(id);
    }

    /*
     * get entities from tagmap with matching tag
     */
    public final List<Entity> getEntitiesByTag(String tag) {
        List<Entity> entityList = tagMap.get(tag);
        if (entityList == null) {
            return Collections.EMPTY_LIST;
        }
        return entityList;
    }

    /*
     * get first entity from tagmap with matching tag
     */
    public final Entity getFirstEntityByTag(String tag) {
        List<Entity> entityList = tagMap.get(tag);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.get(0);
    }

    /*
     * remove tag from tag map for entity
     */
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

    /*
     * add tag to tag map for entity
     */
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

    public final void cleanRemovedComponents() {
        for (Map.Entry<Class<? extends Component>, List<EntityTriMap>> entry : componentMaps.entrySet()) {
            for (EntityTriMap entityTriMap : new ArrayList<EntityTriMap>(entry.getValue())) {
                if (entityTriMap.getComponent().remove) {
                    entityTriMap.getEntity().removeComponentNow(entityTriMap.getComponent());
                }
            }
        }
    }

    /*
     * clean dead entities and call destroy on them
     */
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
