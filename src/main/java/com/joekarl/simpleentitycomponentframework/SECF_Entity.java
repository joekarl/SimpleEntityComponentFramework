/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karl_ctr_kirch
 */
public class SECF_Entity {

    private Map<Class<? extends SECF_Component>, List<SECF_Component>> _components =
            new HashMap<Class<? extends SECF_Component>, List<SECF_Component>>();

    public SECF_Component getComponent(Class<? extends SECF_Component> componentType) {
        List<? extends SECF_Component> components = _components.get(componentType);
        if (components != null && components.size() > 0) {
            return components.get(0);
        }
        return null;
    }

    public List<SECF_Component> getComponents(Class<? extends SECF_Component> componentType) {
        List<SECF_Component> components = _components.get(componentType);
        if (components != null && components.size() > 0) {
            return components;
        }
        return null;
    }

    public boolean hasComponent(Class<? extends SECF_Component> componentType) {
        List<? extends SECF_Component> components = _components.get(componentType);
        if (components != null && components.size() > 0) {
            return true;
        }
        return false;
    }

    public SECF_Component addComponent(Class<? extends SECF_Component> componentType) {
        List<SECF_Component> components = _components.get(componentType);
        if (components == null) {
            components = new ArrayList<SECF_Component>();
            _components.put(componentType, components);
        }
        SECF_Component c = null;
        try {
            c = componentType.newInstance();
            components.add(c);
        } catch (InstantiationException ex) {
            Logger.getLogger(SECF_Entity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SECF_Entity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    public List<SECF_Component> removeComponents(Class<? extends SECF_Component> componentType) {
        return _components.remove(componentType);
    }

    public SECF_Component removeComponent(SECF_Component component, Class<? extends SECF_Component> componentType) {
        List<SECF_Component> components = _components.get(componentType);
        if (components != null) {
            boolean remove = components.remove(component);
            if (remove) {
                return component;
            }
        }
        return null;
    }
}
