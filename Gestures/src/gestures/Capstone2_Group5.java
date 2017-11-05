/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.util.Scanner;

/**
 *
 * @author Cameron
 */
public class Capstone2_Group5{
    public static Boolean debug = false;
    private static GestureCapturer capturer;
    private static Boolean capturing = false;
    private static final Scanner INPUT = new Scanner(System.in);
    private static final OSControl osControl = new BasicCommands();

    /**
     * @param choice the command line argument
     */
    public static void main(String choice){
        // TODO code application logic here
        
//        DecisionTree.init();

        Thread mainThread = new Thread(new Runnable(){
            Boolean running = true;
            @Override
            public void run() {
                while(running){

                    String next = choice;
                    switch(next){
                        case "0":
                            LeapService.stop();
                            running = false;
                            break;
                        case "1":
                            /*
                            GestureRecognizer recognizer = new GestureCapturer();
                            capturer = (GestureCapturer)recognizer;
                            LeapService.start(recognizer);
                            Gesture newGesture;
                                try {
                                    newGesture = capturer.capture();
                                    if(newGesture == null){
                                        System.out.println("Invalid hand");
                                    }
                                    newGesture.name = "Placeholder";

                                    
                                } catch (Exception ex) {
                                    Logger.getLogger(Capstone2_Group5.class.getName()).log(Level.SEVERE, "null", ex);
                                }
                                */
                            break;
                        case "2":
//                            LeapService.start(basicRecognizer);
//                            LeapService.start(decisionTree);
                            break;
                        case "3":
                            TestSuite.run(); //MUST ADD -ea TO VM OPTIONS.  GO TO PROJECT PROPERTIES, RUN, VM OPTIONS
                            Debugger.viewLog();
                            break;
                        default:
                            System.out.println("Not a valid option");
                    }
                }
                Debugger.viewLog();
            }
        });
        mainThread.start();
    }
    
    public static OSControl getOSController(){
        return osControl;
    }
}
