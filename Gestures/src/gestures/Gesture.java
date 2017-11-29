/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class Gesture extends DecisionTreeNode implements JSONWritableReadable{
    public static Boolean debug = Capstone2_Group5.debug;
    
    public String name;
//    public Image rawImage;
    
    public GestureFinger index;
    public GestureFinger middle;
    public GestureFinger ring;
    public GestureFinger pinky;
    public GestureFinger thumb;
    
    public GesturePalm palm;
    
    public Gesture(){
        index = new GestureFinger(Finger.Type.TYPE_INDEX);
        middle = new GestureFinger(Finger.Type.TYPE_MIDDLE);
        ring = new GestureFinger(Finger.Type.TYPE_RING);
        pinky = new GestureFinger(Finger.Type.TYPE_PINKY);
        thumb = new GestureFinger(Finger.Type.TYPE_THUMB);
        palm = new GesturePalm();
    }
    
//    public Boolean performedIn(Frame frame){
//        Debugger.print("Checking if " + name + " is in frame " + frame);
//        Hand hand = frame.hands().frontmost();
//        if(hand.isValid() == false){
//            return false;
//        }
////        System.out.println("hand " + hand);
//        Finger frameIndex = hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0);
////        System.out.println("index " + frameIndex);
//        Finger frameMiddle = hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0);
////        System.out.println("middle " + frameMiddle);
//        Finger frameRing = hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0);
////        System.out.println("ring " + frameRing);
//        Finger framePinky = hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0);
////        System.out.println("pinky " + framePinky);
//        Finger frameThumb = hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0);
////        System.out.println("thumb " + frameThumb);
//        if(!frameIndex.isValid() || !frameMiddle.isValid() || !frameRing.isValid() || !framePinky.isValid() || !frameThumb.isValid()){
//            return false;
//        }
//        Boolean indexCorrect = this.index.performedBy(frameIndex);
//        Debugger.print("  Index correct: " + indexCorrect);
//        Boolean middleCorrect = this.middle.performedBy(frameMiddle);
//        Debugger.print("  Middle correct: " + middleCorrect);
//        Boolean ringCorrect = this.ring.performedBy(frameRing);
//        Debugger.print("  Ring Correct: " + ringCorrect);
//        Boolean pinkyCorrect = this.pinky.performedBy(framePinky);
//        Debugger.print("  Pinky Correct: " + pinkyCorrect);
//        Boolean thumbCorrect = this.thumb.performedBy(frameThumb);
//        Debugger.print("  Thumb Correct: " + thumbCorrect);
//        Boolean palmCorrect = this.palm.allowedVector.contains(hand.palmNormal());
//        Debugger.print("  Palm correct:  " + palmCorrect);
//        
//        Boolean performed = (indexCorrect &&
//                middleCorrect &&
//                ringCorrect &&
//                pinkyCorrect &&
//                thumbCorrect &&
//                palmCorrect);
//        return performed;
//    }
    
    public void setFingerDirection(Finger.Type type, Vector newCenter) throws Exception{
        getGestureFinger(type).allowedDirection.setVector(newCenter);
    }
    
    public void setFingerBoneDirection(Finger.Type fingerType, Bone.Type boneType, Vector center) throws Exception{
        getGestureBone(fingerType, boneType).allowedDirection.setVector(center);
    }
    
//    public void setFingerRange(Finger.Type type, Float newRange) throws Exception{
//        getGestureFinger(type).allowedDirection.setAllRanges(newRange);
//    }
    
//    public void setFingerBoneRange(Finger.Type fingerType, Bone.Type boneType, Float range) throws Exception{
//        if(!(fingerType == Finger.Type.TYPE_THUMB && boneType == Bone.Type.TYPE_METACARPAL)){
//            getGestureBone(fingerType, boneType).allowedDirection.setAllRanges(range);
//        }
//    }
    
//    public void setAllFingerBoneRanges(Finger.Type fingerType, Float range) throws Exception{
//        for(Bone.Type type : Bone.Type.values()){
//            setFingerBoneRange(fingerType, type, range);
//        }
//    }
    
//    public void setAllHandBoneRanges(Float range) throws Exception{
//        for(Finger.Type fingerType : Finger.Type.values()){
//            setAllFingerBoneRanges(fingerType, range);
//        }
//    }
    
    public void setPalmCenter(Vector center) throws Exception{
        this.palm.allowedVector.setVector(center);
    }
    
//    public void setPalmRange(Float range) throws Exception{
//        this.palm.allowedVector.setAllRanges(range);
//    }
    
    private GestureFinger getGestureFinger(Finger.Type type) throws Exception{
        GestureFinger finger;
        switch(type){
            case TYPE_THUMB:
                finger = this.thumb;
                break;
            case TYPE_INDEX:
                finger = this.index;
                break;
            case TYPE_MIDDLE:
                finger = this.middle;
                break;
            case TYPE_RING:
                finger = this.ring;
                break;
            case TYPE_PINKY:
                finger = this.pinky;
                break;
            default:
                throw new Exception("Unable to find gesture finger <" + type + ">");
        }
        return finger;
    }
    
    private GestureBone getGestureBone(Finger.Type fingerType, Bone.Type boneType)throws Exception{
        GestureFinger finger = getGestureFinger(fingerType);
        if(finger != null){
            GestureBone bone;
            switch(boneType){
                case TYPE_DISTAL:
                    bone = finger.distal;
                    break;
                case TYPE_PROXIMAL:
                    bone = finger.proximal;
                    break;
                case TYPE_INTERMEDIATE:
                    bone = finger.intermediate;
                    break;
                case TYPE_METACARPAL:
                    bone = finger.metacarpal;
                    break;
                default:
                    throw new Exception("Unable to find gesture bone <" + fingerType + "> <" + boneType + ">");
            }
            return bone;
        } else {
            throw new Exception("Unable to find gesture bone <" + fingerType + "> <" + boneType + ">");
        }
    }
    
    public Object getAttributeValue(DecisionTree.Attribute attribute){
        Object value = null;
        switch(attribute){
            case INDEX_EXTENDED:
                value = this.index.isExtended;
                break;
            case MIDDLE_EXTENDED:
                value = this.middle.isExtended;
                break;
            case RING_EXTENDED:
                value = this.ring.isExtended;
                break;
            case PINKY_EXTENDED:
                value = this.pinky.isExtended;
                break;
            case THUMB_EXTENDED:
                value = this.thumb.isExtended;
                break;
            case PALM_NORMAL:
                value = this.palm.allowedVector;
                break;
            case INDEX_DIRECTION:
                value = this.index.allowedDirection;
                break;
            case MIDDLE_DIRECTION:
                value = this.middle.allowedDirection;
                break;
            case RING_DIRECTION:
                value = this.ring.allowedDirection;
                break;
            case PINKY_DIRECTION:
                value = this.pinky.allowedDirection;
                break;
            case THUMB_DIRECTION:
                value = this.thumb.allowedDirection;
                break;
                /*
            case INDEX_METACARPAL_DIRECTION:
                value = this.index.metacarpal.allowedDirection;
                break;
            case INDEX_INTERMEDIATE_DIRECTION:
                value = this.index.intermediate.allowedDirection;
                break;
            case INDEX_PROXIMAL_DIRECTION:
                value = this.index.proximal.allowedDirection;
                break;
            case INDEX_DISTAL_DIRECTION:
                value = this.index.distal.allowedDirection;
                break;
            case MIDDLE_METACARPAL_DIRECTION:
                value = this.middle.metacarpal.allowedDirection;
                break;
            case MIDDLE_INTERMEDIATE_DIRECTION:
                value = this.middle.intermediate.allowedDirection;
                break;
            case MIDDLE_PROXIMAL_DIRECTION:
                value = this.middle.proximal.allowedDirection;
                break;
            case MIDDLE_DISTAL_DIRECTION:
                value = this.middle.distal.allowedDirection;
                break;
            case RING_METACARPAL_DIRECTION:
                value = this.ring.metacarpal.allowedDirection;
                break;
            case RING_INTERMEDIATE_DIRECTION:
                value = this.ring.intermediate.allowedDirection;
                break;
            case RING_PROXIMAL_DIRECTION:
                value = this.ring.proximal.allowedDirection;
                break;
            case RING_DISTAL_DIRECTION:
                value = this.ring.distal.allowedDirection;
                break;
            case PINKY_METACARPAL_DIRECTION:
                value = this.pinky.metacarpal.allowedDirection;
                break;
            case PINKY_INTERMEDIATE_DIRECTION:
                value = this.pinky.intermediate.allowedDirection;
                break;
            case PINKY_PROXIMAL_DIRECTION:
                value = this.pinky.proximal.allowedDirection;
                break;
            case PINKY_DISTAL_DIRECTION:
                value = this.pinky.distal.allowedDirection;
                break;
            case THUMB_INTERMEDIATE_DIRECTION:
                value = this.thumb.intermediate.allowedDirection;
                break;
            case THUMB_PROXIMAL_DIRECTION:
                value = this.thumb.proximal.allowedDirection;
                break;
            case THUMB_DISTAL_DIRECTION:
                value = this.thumb.distal.allowedDirection;
                break;
*/
        }
        return value;
    }
    
    @Override
    public Boolean isGesture(){
        return true;
    }
    
    @Override
    public String toString(){
        return "Gesture named: " + name;
//        return "\nGesture: \n" +
//                " name: " + name + "\n" +
//                "  Index: {\n" +
//               "    isExtended: " + index.isExtended + ",\n" +
//               "    allowedPositions: " + index.allowedDirection + ",\n" +
//               "    bonePositions: {\n" +
//               "      metacarpal: " + index.metacarpal.allowedDirection + ",\n" +
//               "      proximal: " + index.proximal.allowedDirection + ",\n" +
//               "      intermediate: " + index.intermediate.allowedDirection + ",\n" +
//               "      distal: " + index.distal.allowedDirection + "},\n" +
//               "  },\n" +
//               "  Middle: {\n" +
//               "    isExtended: " + middle.isExtended + ",\n" +
//               "    allowedPositions: " + middle.allowedDirection + "},\n" +
//               "    bonePositions: {\n" +
//               "      metacarpal: " + middle.metacarpal.allowedDirection + ",\n" +
//               "      proximal: " + middle.proximal.allowedDirection + ",\n" +
//               "      intermediate: " + middle.intermediate.allowedDirection + ",\n" +
//               "      distal: " + middle.distal.allowedDirection + "},\n" +
//               "  },\n" +
//               "  Ring: {\n" +
//               "    isExtended: " + ring.isExtended + ",\n" +
//               "    allowedPositions: " + ring.allowedDirection + "},\n" +
//               "    bonePositions: {\n" +
//               "      metacarpal: " + ring.metacarpal.allowedDirection + ",\n" +
//               "      proximal: " + ring.proximal.allowedDirection + ",\n" +
//               "      intermediate: " + ring.intermediate.allowedDirection + ",\n" +
//               "      distal: " + ring.distal.allowedDirection + "},\n" +
//               "  },\n" +
//               "  Pinky: {\n" +
//               "    isExtended: " + pinky.isExtended + ",\n" +
//               "    allowedPositions: " + pinky.allowedDirection + "},\n" +
//               "    bonePositions: {\n" +
//               "      metacarpal: " + pinky.metacarpal.allowedDirection + ",\n" +
//               "      proximal: " + pinky.proximal.allowedDirection + ",\n" +
//               "      intermediate: " + pinky.intermediate.allowedDirection + ",\n" +
//               "      distal: " + pinky.distal.allowedDirection + "},\n" +
//               "  },\n" +
//               "  Thumb: {\n" +
//               "    isExtended: " + thumb.isExtended + ",\n" +
//               "    allowedPositions: " + thumb.allowedDirection + "},\n" +
//               "    bonePositions: {\n" +
//               "      metacarpal: " + thumb.metacarpal.allowedDirection + ",\n" +
//               "      proximal: " + thumb.proximal.allowedDirection + ",\n" +
//               "      intermediate: " + thumb.intermediate.allowedDirection + ",\n" +
//               "      distal: " + thumb.distal.allowedDirection + "},\n" +
//               "  },\n" +
//               "  Palm Normal: " + palm.allowedVector;
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
        //name, fingers, palm
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("thumb", thumb.toJSONObject());
        obj.put("index", index.toJSONObject());
        obj.put("middle", middle.toJSONObject());
        obj.put("ring", ring.toJSONObject());
        obj.put("pinky", pinky.toJSONObject());
        obj.put("palm", palm.toJSONObject());
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        if(jsonObject == null){
            return;
        }
        Object nameObj = jsonObject.get("name");
        if(nameObj != null){
            name = nameObj.toString();
        }
        thumb.makeSelfFromJSONObject((JSONObject)jsonObject.get("thumb"));
        index.makeSelfFromJSONObject((JSONObject)jsonObject.get("index"));
        middle.makeSelfFromJSONObject((JSONObject)jsonObject.get("middle"));
        ring.makeSelfFromJSONObject((JSONObject)jsonObject.get("ring"));
        pinky.makeSelfFromJSONObject((JSONObject)jsonObject.get("pinky"));
        palm.makeSelfFromJSONObject((JSONObject)jsonObject.get("palm"));
    }
}
