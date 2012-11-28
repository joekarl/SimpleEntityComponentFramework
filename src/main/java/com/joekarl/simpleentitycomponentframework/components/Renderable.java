/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joekarl.simpleentitycomponentframework.components;

import com.joekarl.simpleentitycomponentframework.Component;
import java.awt.Graphics2D;

/**
 *
 * @author karl
 */
public abstract class Renderable extends Component {
    public abstract void draw(Graphics2D g2d);
}
