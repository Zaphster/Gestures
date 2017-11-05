/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

/**
 *
 * @author Cameron
 */
public class EventListeners {
    
    public EventListeners(){

        int gesturePerformedId = Event.registerHandler(Event.TYPE.GESTURE_PERFORMED, (Event event) -> {
            Gesture gesture = (Gesture)event.get("gesture");
           System.out.println("Gesture performed: " + gesture.name); 
        });
        
        int commandPerformedId = Event.registerHandler(Event.TYPE.COMMAND_PERFORMED, (Event event) -> {
           System.out.println("Command performed: " + event.get("command")); 
        });
    }
}
