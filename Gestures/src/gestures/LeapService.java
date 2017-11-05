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
public class LeapService implements Runnable{
    private static Controller controller = new Controller();
    private static Boolean running = false;
    private static Listener listener;
    private static int numRunning = 0;

    private LeapService(){
        
    }
    
    public static void start(GestureRecognizer recognizer){
        stop();
        listener = new LeapServiceListener(recognizer);
        controller.addListener(listener);
        Thread thread = new Thread(new LeapService());
        System.out.println("number running: " + numRunning);
        if(numRunning > 0){
            return;
        }
        thread.start();
    }
    
    public static void stop(){
        if(listener != null){
            controller.removeListener(listener);
        }
        running = false;
    }

    @Override
    public void run() {
        numRunning ++;
        System.out.println("Leap service started");
        running = true;
        while(running){
            //System.out.println("Leap motion controller connected: " + controller.isServiceConnected());
            while(controller.isServiceConnected() && running){
            }
            while(!controller.isServiceConnected() && running){
            }
        }
        System.out.println("Leap service stopped");
        numRunning --;
    }
}