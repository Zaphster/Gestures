/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.*;

/**
 *
 * @author Cameron
 */
public class GestureCapturer implements GestureRecognizer{
    public static Boolean debug = Capstone2_Group5.debug;
    private Frame capturedFrame;
    private Frame lastFrame;
//    public static Float defaultAllowedFingerRadianRange = new Float(0.5); //about 30 degrees
//    public static Float defaultAllowedBoneRadianRange = new Float(0.7); // about 45 degrees
//    public static Float defaultAllowedPalmRadianRange = new Float(6.28); // about 360 degrees
//    public static Float defaultAllowedGrabAngleRadianRange = new Float(0.35); // about 20 degrees
//    private double defaultAllowedDistance = 20; // in mm
    
    public GestureCapturer(){
    }
    
    public Gesture capture()throws Exception{
        
        capturedFrame = lastFrame;
        if(capturedFrame == null){
            throw new Exception("No frame found.  Is the leap motion controller connected?");
        }
        Hand hand = capturedFrame.hands().frontmost();
        if(hand == null || !hand.isValid()){
            return null;
        }

        Finger index = hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0);
        Finger middle = hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0);
        Finger ring = hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0);
        Finger pinky = hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0);
        Finger thumb = hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0);
        
//        Image frameImage = capturedFrame.images().get(0);
        
        Gesture captured = new Gesture();
//        captured.rawImage = frameImage;
        setFingerProperties(index, captured.index);
        setFingerProperties(middle, captured.middle);
        setFingerProperties(ring, captured.ring);
        setFingerProperties(pinky, captured.pinky);
        setFingerProperties(thumb, captured.thumb);
        
        captured.palm.allowedVector.setVector(hand.palmNormal());// = getVectorRange(hand.palmNormal());
        Event gestureCaptured = new Event(Event.TYPE.GESTURE_CAPTURED);
        gestureCaptured.addDetail("gesture", captured);
        gestureCaptured.trigger();
//        System.out.println("Captured gesture");
//        System.out.println(captured);
        return captured;
    }
    
//    private GestureVector getVectorRange(Vector vector) throws Exception{
//        return getVectorRange(vector);
//    }
    
//    private GestureVector getVectorRange(Vector vector) throws Exception{
//        return new GestureVector(vector);
//    }
    
    private void setFingerProperties(Finger finger, GestureFinger gestureFinger) throws Exception{
        gestureFinger.isExtended = finger.isExtended();
        gestureFinger.allowedDirection.setVector(finger.direction());// = getVectorRange(finger.direction(), defaultAllowedFingerRadianRange);
        if(!finger.type().equals(Finger.Type.TYPE_THUMB)){
            gestureFinger.metacarpal.allowedDirection.setVector(finger.bone(Bone.Type.TYPE_METACARPAL).direction());// = getVectorRange(finger.bone(Bone.Type.TYPE_METACARPAL).direction(), defaultAllowedBoneRadianRange);
        }
        gestureFinger.intermediate.allowedDirection.setVector(finger.bone(Bone.Type.TYPE_INTERMEDIATE).direction());// = getVectorRange(finger.bone(Bone.Type.TYPE_INTERMEDIATE).direction(), defaultAllowedBoneRadianRange);
        gestureFinger.proximal.allowedDirection.setVector(finger.bone(Bone.Type.TYPE_PROXIMAL).direction());// = getVectorRange(finger.bone(Bone.Type.TYPE_PROXIMAL).direction(), defaultAllowedBoneRadianRange);
        gestureFinger.distal.allowedDirection.setVector(finger.bone(Bone.Type.TYPE_DISTAL).direction());// = getVectorRange(finger.bone(Bone.Type.TYPE_DISTAL).direction(), defaultAllowedBoneRadianRange);
    }
    
    @Override
    public void scan(Frame frame) {
        lastFrame = frame;
    }
}
