/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework;

import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author karl_ctr_kirch
 */
public abstract class SECF_Component {

    private static long nextId = Long.MIN_VALUE;
    private final long ID = nextId++;
    private Map<String, Object> _objectParams = new HashMap<String, Object>();
    private Object2FloatOpenHashMap<String> _floatParams = new Object2FloatOpenHashMap();
    private Object2IntOpenHashMap<String> _intParams = new Object2IntOpenHashMap();
    private Object2LongOpenHashMap<String> _longParams = new Object2LongOpenHashMap();
    private Object2DoubleOpenHashMap<String> _doubleParams = new Object2DoubleOpenHashMap();

    public Object getObjectParameter(String param) {
        return _objectParams.get(param);
    }

    public void setObjectParameter(String param, Object value) {
        _objectParams.put(param, value);
    }

    public float getFloatParameter(String param) {
        return _floatParams.getFloat(param);
    }

    public void setFloatParameter(String param, float f) {
        _floatParams.put(param, f);
    }

    public int getIntParameter(String param) {
        return _intParams.getInt(param);
    }

    public void setIntParameter(String param, int i) {
        _intParams.put(param, i);
    }

    public long getLongParameter(String param) {
        return _longParams.getLong(param);
    }

    public void setLongParameter(String param, long l) {
        _longParams.put(param, l);
    }

    public double getDoubleParameter(String param) {
        return _doubleParams.getDouble(param);
    }

    public void setDoubleParameter(String param, double d) {
        _doubleParams.put(param, d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SECF_Component other = (SECF_Component) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.ID ^ (this.ID >>> 32));
        return hash;
    }
}
