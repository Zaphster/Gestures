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
public class LeapServiceListener extends Listener{
    public Controller controller;
    public GestureRecognizer gestureRecognizer;
    private final Event controllerConnected;
    private final Event controllerDisconnected;
    
    public LeapServiceListener(GestureRecognizer gestureRecognizer){
        this.gestureRecognizer = gestureRecognizer;
        controllerConnected = new Event(Event.TYPE.LEAP_CONTROLLER_CONNECTED);
        controllerDisconnected = new Event(Event.TYPE.LEAP_CONTROLLER_DISCONNECTED);
    }
    
    @Override
    public void onConnect(Controller controller){
        this.controller = controller;
        controllerConnected.trigger();
    }

    @Override
    public void onDisconnect(Controller controller) {
        super.onDisconnect(controller);
        controllerDisconnected.trigger();
    }
    
    @Override
    public void onFrame(Controller controller){
        Frame frame = controller.frame();
        gestureRecognizer.scan(frame);
    }
}
