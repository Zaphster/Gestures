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
public class Debugger {
    private static Boolean debug = Capstone2_Group5.debug;
    private static final StringBuilder debugText = new StringBuilder();
    
    public static void print(String message){
        debugText.append(message).append("\n");
        if(debug){
            System.out.println(message);
        }
    }
    
    public static String getLog(){
        return debugText.toString();
    }
    
    public static void viewLog(){
        System.out.println(debugText.toString());
    }
}
