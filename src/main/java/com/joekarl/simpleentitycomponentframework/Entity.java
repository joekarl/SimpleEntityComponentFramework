package com.joekarl.simpleentitycomponentframework;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author karl_ctr_kirch
 * 
 * Base class for an entity, All entities must implement this interface
 * 
 */
public class Entity {

    /*
     * store all used id's just in case (and a really big case I might add)
     * that we've overflowed our long bounds and ended up at the bottom again
     */
    private static final LongList takenIds = new LongArrayList();
    /* 
     * shared next id value
     */
    private static long _nextId = Long.MIN_VALUE;

    /*
     * static method to grab the next available id
     */
    private static long getNextId() {
        while (takenIds.contains(_nextId)) {
            _nextId++;
        }
        return _nextId;
    }
    /*
     * stores the id of the entity
     */
    private long id = getNextId();

    /*
     * protected so that only the entity manager can create an entity
     */
    protected Entity(EntityManager em) {
        cachedEntityManager = em;
    }
    /*
     * whether the object is dead
     */
    private boolean dead = false;
    /*
     * tag associated with the object
     */
    private String tag = null;
    /*
     * components held owned by the enity
     */
    private Map<Class<? extends Component>, Component> components =
            new HashMap<Class<? extends Component>, Component>();
    /*
     * reference to the entity manager that created the entity
     */
    private EntityManager cachedEntityManager;

    /*
     * Add component to entity
     */
    public final void addComponent(Component component) {
        addComponent(component, component.getClass());
    }

    /*
     * Add component to entity, but associate the component with a different
     * component type, useful if you need inherited component types but probably 
     * should be avoided
     */
    public final void addComponent(Component component,
            Class<? extends Component> componentType) {
        if (components.containsKey(componentType)) {
            //remove existing component if it already exists
            cachedEntityManager.removeComponentWithEntity(component, this);
        }

        components.put(componentType, component);
        cachedEntityManager.addComponentWithEntity(component, this, componentType);
    }

    /*
     * returns the component of a given type for this entity
     */
    public final <T extends Component> T getComponent(Class<T> type) {
        Component foundComponent = components.get(type);
        if (foundComponent != null) {
            return type.cast(foundComponent);
        }
        return null;
    }

    /*
     * return all components
     */
    public final Collection<Component> getComponents() {
        return components.values();
    }

    /*
     * remove a component from this entity
     */
    public final void removeComponent(Component component) {
        Collection<Component> componentList = components.values();
        Iterator<Component> iterator = componentList.iterator();
        while (iterator.hasNext()) {
            Component c = iterator.next();
            if (c.equals(component)) {
                iterator.remove();
                cachedEntityManager.removeComponentWithEntity(c, this);
                break;
            }
        }
    }

    /*
     * the entities unique id
     */
    public final long getId() {
        return id;
    }

    /*
     * whether the entity is dead or not
     */
    public final boolean isDead() {
        return dead;
    }

    /*
     * set the entity to dead so it can be cleaned up by the entity manager
     */
    public final void setDead() {
        dead = true;
    }

    /*
     * set the tag on this entity
     * if tag is null or empty string it will remove the existing tag
     */
    public final void setTag(String tag) {
        if (this.tag != null || tag == null || tag.equals("")) {
            cachedEntityManager.removeTagFromEntity(this);
        }
        this.tag = tag;
        //update tag manager
        cachedEntityManager.addTagToEntity(this, tag);
    }

    /*
     * returns the entity's tag
     */
    public final String getTag() {
        return tag;
    }

    /*
     * remove all components
     */
    public final void removeAllComponents() {
        Collection<Component> componentCollection = components.values();
        Iterator<Component> iterator = componentCollection.iterator();
        while (iterator.hasNext()) {
            Component c = iterator.next();
            cachedEntityManager.removeComponentWithEntity(c, this);
            iterator.remove();
        }
    }

    /*
     * protected destroy entity called by entity manager
     * removes all components and current tag
     */
    protected final void destroy() {
        removeAllComponents();
        setTag(null);
    }
}
