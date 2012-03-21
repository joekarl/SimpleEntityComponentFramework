/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

/**
 *
 * @author karl_ctr_kirch
 */
public class EntityTriMap {

    private Component component;
    private Entity entity;
    private long id;

    public EntityTriMap(Entity e, Component component) {
        this(e.getId(), e, component);
    }

    public EntityTriMap(long id, Entity e, Component component) {
        this.id = id;
        this.entity = e;
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getId() {
        return id;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityTriMap other = (EntityTriMap) obj;
        if (this.component != other.component && (this.component == null || !this.component.equals(other.component))) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.component != null ? this.component.hashCode() : 0);
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
