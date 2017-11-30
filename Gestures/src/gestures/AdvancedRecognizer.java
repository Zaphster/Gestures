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
    OSControl osControl = Capstone2_Group5.getOSController();
    static int interval = 0;
    
    @Override
    public void scan(Frame frame){
        interval = (interval + 1) % 300;
        if(interval == 0){
//            System.out.println();
//            System.out.println("----------------");
        }
        if(frame != null){
            Hand hand = frame.hands().frontmost();
            if(hand != null && hand.isValid()){
                Vector position = hand.palmPosition();
                if(position != null){
                    osControl.updateHandPosition((int)position.getX(), (int)position.getY(), (int)position.getZ());
                    osControl.moveMouse();
                }
            } else {
                osControl.resetHandPosition();
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
