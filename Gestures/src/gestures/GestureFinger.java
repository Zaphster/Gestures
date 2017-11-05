/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class GestureFinger implements JSONWritableReadable{
    public static Boolean debug = Capstone2_Group5.debug;
    private Finger.Type type;
    public GestureBone metacarpal= new GestureBone(Bone.Type.TYPE_METACARPAL);
    public GestureBone proximal = new GestureBone(Bone.Type.TYPE_PROXIMAL);
    public GestureBone intermediate = new GestureBone(Bone.Type.TYPE_INTERMEDIATE);
    public GestureBone distal = new GestureBone(Bone.Type.TYPE_DISTAL);
    
    public Boolean isExtended;
    public VectorRange allowedDirection = new VectorRange();
    
    public GestureFinger(){
        
    }
    
    public GestureFinger(Finger.Type type){
//        metacarpal = new GestureBone(Bone.Type.TYPE_METACARPAL);
//        proximal = new GestureBone(Bone.Type.TYPE_PROXIMAL);
//        intermediate = new GestureBone(Bone.Type.TYPE_INTERMEDIATE);
//        distal = new GestureBone(Bone.Type.TYPE_DISTAL);
//        allowedDirection = new VectorRange();
        this.type = type;
    }
    
    public Finger.Type getType(){
        return type;
    }
    
    public Boolean performedBy(Finger finger){
            Boolean fingerDirectionCorrect, metacarpalCorrect, proximalCorrect, intermediateCorrect, distalCorrect, extendedCorrect;
//            Debugger.print(type + " Correctness:");
            fingerDirectionCorrect = allowedDirection.contains(finger.direction());
            Debugger.print("  finger direction: " + fingerDirectionCorrect);
            if(type == Finger.Type.TYPE_THUMB){
                metacarpalCorrect = true;
            } else {
                metacarpalCorrect = metacarpal.allowedDirection.contains(finger.bone(Bone.Type.TYPE_METACARPAL).direction());
            }
            Debugger.print("  metacarpal: " + metacarpalCorrect);
            proximalCorrect = proximal.allowedDirection.contains(finger.bone(Bone.Type.TYPE_PROXIMAL).direction());
            Debugger.print("  proximal: " + proximalCorrect);
            intermediateCorrect = intermediate.allowedDirection.contains(finger.bone(Bone.Type.TYPE_INTERMEDIATE).direction());
            Debugger.print("  intermediate: " + intermediateCorrect);
            distalCorrect = distal.allowedDirection.contains(finger.bone(Bone.Type.TYPE_DISTAL).direction());
            Debugger.print("  distal: " + distalCorrect);
            extendedCorrect = finger.isExtended() == this.isExtended;
            Debugger.print("  extended? " + extendedCorrect);
            return fingerDirectionCorrect && 
                    metacarpalCorrect &&
                    proximalCorrect &&
                    intermediateCorrect &&
                    distalCorrect &&
                    extendedCorrect;
    }
    
    @Override
    public String makeJSONString(){
        return toJSONObject().toJSONString();
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
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("type", type.toString());
        obj.put("metacarpal", metacarpal.toJSONObject());
        obj.put("proximal", proximal.toJSONObject());
        obj.put("intermediate", intermediate.toJSONObject());
        obj.put("distal", distal.toJSONObject());
        obj.put("isExtended", isExtended);
        obj.put("allowedDirection", allowedDirection.toJSONObject());
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        type = Finger.Type.valueOf(jsonObject.get("type").toString());
        metacarpal.makeSelfFromJSONObject((JSONObject)jsonObject.get("metacarpal"));
        proximal.makeSelfFromJSONObject((JSONObject)jsonObject.get("proximal"));
        intermediate.makeSelfFromJSONObject((JSONObject)jsonObject.get("intermediate"));
        distal.makeSelfFromJSONObject((JSONObject)jsonObject.get("distal"));
        isExtended = Boolean.valueOf(jsonObject.get("isExtended").toString());
        allowedDirection.makeSelfFromJSONObject((JSONObject)jsonObject.get("allowedDirection"));
    }

}
