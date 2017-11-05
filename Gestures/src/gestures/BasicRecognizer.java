/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.*;
import java.util.ArrayList;

/**
 *
 * @author Cameron
 */
public class BasicRecognizer implements GestureRecognizer{
    private Event thumbsUp = new Event(Event.TYPE.GESTURE_PERFORMED);
    private Event pointingWithIndex = new Event(Event.TYPE.GESTURE_PERFORMED);
    private BasicCommands command = new BasicCommands();
    private ArrayList<Gesture> gestures = new ArrayList<>();
    
    private long lastCheck = 0;

    
    public BasicRecognizer(){
        thumbsUp.addDetail("gesture", "thumbsUp");
        pointingWithIndex.addDetail("gesture", "pointingWithIndexFinger");
        Event.registerHandler(Event.TYPE.GESTURE_CAPTURED, (Event event)->{
            Gesture gesture = (Gesture)event.get("gesture");
            gestures.add(gesture);
            System.out.println("Gesture <" + gesture.name + "> added to recognized gesture list");
        });
    }
    
    public void checkForPointingWithIndex(Frame frame){
        Finger index = frame.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0);
        Finger middle = frame.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0);
        Finger ring = frame.fingers().fingerType(Finger.Type.TYPE_RING).get(0);
        Finger pinky = frame.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0);
        Finger thumb = frame.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0);
        
        if(index.isExtended() && !middle.isExtended() && !ring.isExtended() && !pinky.isExtended() && !thumb.isExtended()){
            pointingWithIndex.trigger();
//            Command.perform(Command.MOUSE_PRIMARY_DOWN);
        }
    }
    
    public void checkForThumbsUp(Frame frame){
        Finger index = frame.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0);
        Finger middle = frame.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0);
        Finger ring = frame.fingers().fingerType(Finger.Type.TYPE_RING).get(0);
        Finger pinky = frame.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0);
        Finger thumb = frame.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0);
        
        if(!index.isExtended() && !middle.isExtended() && !ring.isExtended() && !pinky.isExtended() && thumb.isExtended()){
            thumbsUp.trigger();
        }
    }
    
    @Override
    public void scan(Frame frame) {
//        checkForThumbsUp(frame);
//        checkForPointingWithIndex(frame);
//        if(frame.timestamp() > (lastCheck + (3000000))){
//            lastCheck = frame.timestamp();
//            System.out.println("3 second intervals");
            for(Gesture gesture : gestures){
                if(gesture.performedIn(frame)){
                    Event gesturePerformed = new Event(Event.TYPE.GESTURE_PERFORMED);
                    gesturePerformed.addDetail("gesture", gesture.name);
                    gesturePerformed.trigger();
                }
            }
//        }
    }
    
}