/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

/**
 *
 * @author Cameron
 * 
 */
public enum Command {
    MOUSE_PRIMARY_DOWN,
    MOUSE_PRIMARY_UP,
    MOUSE_PRIMARY_CLICK,
    MOUSE_SECONDARY_DOWN,
    MOUSE_SECONDARY_UP,
    MOUSE_SECONDARY_CLICK,
    MOUSE_PRIMARY_DOUBLE_CLICK,
    MOUSE_SCROLL,
    BACKSPACE_HELD_DOWN,
    BACKSPACE_RELEASE,
    BACKSPACE_PRESS,
    ENTER_HELD_DOWN,
    ENTER_RELEASE,
    ENTER_PRESS,
    SHIFT_HELD_DOWN,
    SHIFT_RELEASE,
    TAB_HELD_DOWN,
    TAB_RELEASE,
    TAB_PRESS,
    CAPSLOCK_PRESS,
    F12_PRESS,
    F11_PRESS,
    F10_PRESS;
        

    public String toTableString(){
        return this.name().replace("_", " ").trim();

    }
}