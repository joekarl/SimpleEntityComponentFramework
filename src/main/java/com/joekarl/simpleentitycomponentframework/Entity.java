/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 */
public class Entity {

    private static final LongList takenIds = new LongArrayList();
    private static long _nextId = Long.MIN_VALUE;

    private static long getNextId() {
        while (takenIds.contains(_nextId)) {
            _nextId++;
        }
        return _nextId;
    }
    private long id = getNextId();

    protected Entity(EntityManager em) {
        cachedEntityManager = em;
    }
    private boolean dead = false;
    private String tag = null;
    private Map<Class<? extends Component>, Component> components =
            new HashMap<Class<? extends Component>, Component>();
    private EntityManager cachedEntityManager;

    public final void addComponent(Component component) {
        addComponent(component, component.getClass());
    }

    public final void addComponent(Component component,
            Class<? extends Component> componentType) {
        components.put(componentType, component);
        cachedEntityManager.addComponentWithEntity(component, this, componentType);
    }

    public final <T extends Component> T getComponent(Class<T> type) {
        Component foundComponent = components.get(type);
        if (foundComponent != null) {
            return type.cast(foundComponent);
        }
        return null;
    }

    public final Collection<Component> getComponents() {
        return components.values();
    }

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

    public final long getId() {
        return id;
    }

    public final boolean isDead() {
        return dead;
    }

    public final void setDead() {
        dead = true;
    }

    public final void setTag(String tag) {
        if (this.tag != null || tag == null || tag.equals("")) {
            cachedEntityManager.removeTagFromEntity(this);
        }
        this.tag = tag;
        //update tag manager
        cachedEntityManager.addTagToEntity(this, tag);
    }

    public final String getTag() {
        return tag;
    }

    public final void removeAllComponents() {
        Collection<Component> componentCollection = components.values();
        Iterator<Component> iterator = componentCollection.iterator();
        while (iterator.hasNext()) {
            Component c = iterator.next();
            cachedEntityManager.removeComponentWithEntity(c, this);
            iterator.remove();
        }
    }

    protected final void destroy() {
        removeAllComponents();
        setTag(null);
    }
}
