/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

/**
 *
 * @author Cameron
 */
public class AdvancedRecognizer implements GestureRecognizer{
    Event gestureFound = new Event(Event.TYPE.GESTURE_PERFORMED);
    
    @Override
    public void scan(Frame frame){
        if(frame != null){
            Hand hand = frame.hands().frontmost();
            if(hand != null){
                Vector position = hand.palmPosition();
                if(position != null){
                    Capstone2_Group5.getOSController().updateHandPosition((int)position.getX(), (int)position.getY(), (int)position.getZ());
                }
            }
        }
        try{
            Gesture gesture = DecisionTree.findGesture(frame);
            if(gesture != null){
                gestureFound.addDetail("gesture", gesture);
                gestureFound.trigger();
            }
        } catch(Exception e){
            Debugger.print(e.getMessage());
        }
    }
}
