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
    MOUSE_PRIMARY_HELD_DOWN,
    MOUSE_PRIMARY_UP,
    MOUSE_PRIMARY_CLICK,
    MOUSE_SECONDARY_DOWN,
    MOUSE_SECONDARY_HELD_DOWN,
    MOUSE_SECONDARY_UP,
    MOUSE_SECONDARY_CLICK,
//    MOUSE_MOVE,
    MOUSE_SCROLL,
    BACKSPACE_HELD_DOWN,
    BACKSPACE_RELEASE,
    BACKSPACE_PRESS,
    ENTER_HELD_DOWN,
    ENTER_RELEASE,
    ENTER_PRESS,
    SHIFT_HELD_DOWN,
    SHIFT_RELEASE,
    SHIFT_PRESS,
    TAB_HELD_DOWN,
    TAB_RELEASE,
    TAB_PRESS,
    CAPSLOCK_PRESS,
    F12_PRESS,
    F11_PRESS,
    F10_PRESS,
    F9_PRESS,
    F8_PRESS,
    F7_PRESS,
    F6_PRESS,
    F5_PRESS,
    F4_PRESS,
    F3_PRESS,
    F2_PRESS,
    F1_PRESS;
        

    public String toTableString(){
        return this.name().replace("_", " ").trim();

    }
}
