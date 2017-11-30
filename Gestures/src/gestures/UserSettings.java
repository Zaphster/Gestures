/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

    

/**
 *
 * @author alec
 */
public class UserSettings {
    
    private int keyPressDelay = 50;
    private int mouseClickDelay = 50;
    private int mouseMovementDelay = 0;
    private float padSensitivity_coefficient = 500;
    private boolean useZAxis = false;
    private int gestureFoundThreshold = 50;
    
    public UserSettings(){

    }
    
    public void setDefaultSettings(){
    
        keyPressDelay = 50;
        mouseClickDelay = 50;
        mouseMovementDelay = 0;
        
        padSensitivity_coefficient = 500;
        
        useZAxis = false;
    
        gestureFoundThreshold = 50;
    }
    
    public void setKeyPressDelay(int delay){
        this.keyPressDelay = delay;
    }
    
    public int getKeyPressDelay(){
        return this.keyPressDelay;
    }
    
    public void setMouseClickDelay(int delay){
        this.keyPressDelay = delay;
    }
    
    public int getMouseClickDelay(){
        return this.keyPressDelay;
    }
    
    public void setMouseMovementDelay(int delay){
        this.mouseMovementDelay = delay;
    }
    
    public int getMouseMovementDelay(){
        return this.mouseMovementDelay;
    }
    
    public void setPadSensitivity(float sensitivity){
        this.padSensitivity_coefficient = sensitivity;
    }
    
    public float getPadSenstitivity(){
        return this.padSensitivity_coefficient;
    }
    
    public void setGestureFoundThreshold(int threshold){
        this.gestureFoundThreshold = threshold;
    }
    
    public int getGestureFoundThreshold(){
        return this.gestureFoundThreshold;
    }
    
    public void setAxisChoice(boolean bool){
        this.useZAxis = bool;
    }
    
    public boolean getAxisChoice(){
        return this.useZAxis;
    }
}
