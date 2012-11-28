package com.joekarl.simpleentitycomponentframework;

/**
 *
 * @author karl_ctr_kirch
 * 
 * Interface for a component. All components must implement this interface
 * 
 */
public abstract class Component {
    protected Entity entity;
    protected boolean remove = false;

    public Entity getEntity() {
        return entity;
    }
    
}
