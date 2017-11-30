/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

    

/**
 *
 * @author alec
 */
public class UserSettings implements JSONWritableReadable{
    
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
    
    public float getPadSensitivity(){
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
    
    public boolean getUseZAxis(){
        return this.useZAxis;
    }

    @Override
    public String makeJSONString() {
        return toJSONObject().toJSONString();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("keyPressDelay", this.keyPressDelay);
        obj.put("gestureFoundThreshold", this.gestureFoundThreshold);
        obj.put("mouseClickDelay", this.mouseClickDelay);
        obj.put("mouseMovementDelay", this.mouseMovementDelay);
        obj.put("padSensitivity_coefficient", this.padSensitivity_coefficient);
        obj.put("useZAxis", this.useZAxis);
        return obj;
    }

    @Override
    public void makeSelfFromJSON(String json) {
        Object obj = JSONValue.parse(json);
        if(obj != null){
            JSONObject jsonObj = (JSONObject)obj;
            makeSelfFromJSONObject(jsonObj);
        }
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        this.setAxisChoice((Boolean)jsonObject.get("useZAxis"));
        this.setGestureFoundThreshold(Integer.parseInt(jsonObject.get("gestureFoundThreshold").toString()));
        this.setKeyPressDelay(Integer.parseInt(jsonObject.get("keyPressDelay").toString()));
        this.setMouseClickDelay(Integer.parseInt(jsonObject.get("mouseClickDelay").toString()));
        this.setMouseMovementDelay(Integer.parseInt(jsonObject.get("mouseMovementDelay").toString()));
        this.setPadSensitivity(Float.parseFloat(jsonObject.get("padSensitivity_coefficient").toString()));
    }
}
