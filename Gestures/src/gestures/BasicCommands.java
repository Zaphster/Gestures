/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author alec
 * 
 *  ~To-Do~
 * 
 *  - add functions for keycodes: backspace 8, enter 13, shift 16, tap 9, left 37, up 38, right 39, down 40, caps 20
 * 
 *  - add functions for copy/cut/paste, 
 *      - Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
 * 
 *  - Spruce up mouseScroll
 * 
 *  - sets/gets
 * 
 */

public class BasicCommands implements OSControl{
    
    private Robot robot;
    
    private int autoDelay = 50;
    private int keyPressDelay = 250;
    private int mouseClickDelay = 250;
    private int mouseMovementDelay = 0;
    
    private int screenHeight;
    private int screenWidth;
    
    private int mousePositionX;
    private int mousePositionY;

    private int trackPadWindowHeight = 500;    
    private int trackPadWindowWidth = 500;
    
    private float joyStickSensitivity = 100; //is percent
    private float padSensitivity = 500;      //is percent
    
    private boolean useZAxis = true;
    private int yPosCalibration = 500;
    
    private int handCurrentX;
    private int handCurrentY;
    private int handCurrentZ;

    public BasicCommands(){
            
        GraphicsDevice screenDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point pointer = MouseInfo.getPointerInfo().getLocation();
        
        try{
            
            robot = new Robot(screenDev);
            
            robot.setAutoDelay(this.autoDelay);    
            robot.setAutoWaitForIdle(false);
            
        }catch(AWTException e){
            
            e.printStackTrace(System.out); //probably need to handle better eventually
            
        }    
        
        screenHeight = screenSize.height;
        screenWidth = screenSize.width;
        
        mousePositionX = (int)pointer.getX();
        mousePositionY = (int)pointer.getY();

    }
    
    private void click(int button){
        
        robot.mousePress(button);
        robot.delay(mouseClickDelay);
        robot.mouseRelease(button);
        robot.delay(mouseClickDelay);
        
    }
    
    private void holdClick(int button){
    
        robot.mousePress(button);
        robot.delay(mouseClickDelay);

        
    }
    
    private void releaseClick(int button){
    
        robot.mouseRelease(button);
        robot.delay(mouseClickDelay);
    
    }
    
    private void primaryClick(){
        this.click(InputEvent.BUTTON1_MASK);
    }
    
    private void secondaryClick(){
        this.click(InputEvent.BUTTON2_MASK);  
    }
    
    private void primaryClickHold(){   
        this.holdClick(InputEvent.BUTTON1_MASK);      
    }
    
    private void secondaryClickHold(){
        this.holdClick(InputEvent.BUTTON2_MASK);
    }
    
    private void primaryClickRelease(){
        this.releaseClick(InputEvent.BUTTON1_MASK);
    }
    
    private void secondaryClickRelease(){
        this.releaseClick(InputEvent.BUTTON2_MASK);
    }
    
    private void pressKey(int keycode){
        
        this.holdKey(keycode);
        this.releaseKey(keycode);
        
    }
    
    private void holdKey(int keycode){
        
        robot.keyPress(keycode);
        robot.delay(keyPressDelay);
        
    
    }
    
    private void releaseKey(int keycode){
    
        robot.keyRelease(keycode);
        robot.delay(keyPressDelay);
        
    }
    
    private void moveMouse(int x, int y){
    
        robot.mouseMove(x, y);
        robot.delay(mouseMovementDelay);
        
    }
    
    private void moveJoyStick(){
        
        int x_pos = Math.round(this.handCurrentX*joyStickSensitivity/100);
        int y_pos = Math.round(this.handCurrentY*joyStickSensitivity/100);
        int z_pos = Math.round(this.handCurrentZ*joyStickSensitivity/100);
        
        if(useZAxis == true){
            
            moveMouse(mousePositionX + x_pos, mousePositionY + z_pos);
            
            mousePositionX = mousePositionX + x_pos;
            mousePositionY = mousePositionY + z_pos;
            
        }else{
            
            moveMouse(mousePositionX + x_pos, mousePositionY - (y_pos-yPosCalibration));
            
            mousePositionX = mousePositionX + x_pos;
            mousePositionY = mousePositionY - (y_pos-yPosCalibration);
            
        }
        
    }
    
    private void moveStandard(){
        
        int x_pos = Math.round(this.handCurrentX*padSensitivity/100);
        int y_pos = Math.round(this.handCurrentY*padSensitivity/100);
        int z_pos = Math.round(this.handCurrentZ*padSensitivity/100);
        
        if(useZAxis == true){
            moveMouse(screenWidth + x_pos, screenHeight + z_pos);
        }else{
            moveMouse(screenWidth + x_pos, screenHeight - (y_pos - yPosCalibration));
        }
           
    }
    
    private void moveTrackPad(){
        
        int x_pos = Math.round(this.handCurrentX*padSensitivity/100);
        int y_pos = Math.round(this.handCurrentY*padSensitivity/100);
        int z_pos = Math.round(this.handCurrentZ*padSensitivity/100);
        
        if(useZAxis == true){
            
            if(x_pos > trackPadWindowWidth || x_pos < (-1)*trackPadWindowWidth || z_pos > trackPadWindowHeight || z_pos < (-1)*trackPadWindowHeight){
                
                Point pointer = MouseInfo.getPointerInfo().getLocation();
                mousePositionX = (int)pointer.getX();
                mousePositionY = (int)pointer.getY();
                
            }else{
                moveMouse(mousePositionX + x_pos, mousePositionY + z_pos);
            }
        }else{
            moveMouse(mousePositionX + x_pos, mousePositionY - (y_pos - yPosCalibration));
        }
           
    }
    
    public void mouseScroll(){
        
        int z_pos = this.handCurrentZ;
        int y_pos = this.handCurrentY;
        
        if(useZAxis == true){
            robot.mouseWheel(z_pos);
        }else{
            robot.mouseWheel((y_pos - yPosCalibration)*(-1));
        }
        
    }

    @Override
    public void performCommand(Command command) {
        
//        MOUSE_PRIMARY_DOWN,
//        MOUSE_PRIMARY_HELD_DOWN,
//        MOUSE_PRIMARY_UP,
//        MOUSE_SECONDARY_DOWN,
//        MOUSE_SECONDARY_HELD_DOWN,
//        MOUSE_SECONDARY_UP,
//        MOUSE_MOVE,
//        MOUSE_SCROLL_UP,
//        MOUSE_SCROLL_DOWN,
//        KEY_DOWN,
//        KEY_HELD_DOWN,
//        KEY_UP,

        switch(command){
            case MOUSE_PRIMARY_DOWN:
                this.primaryClickHold();
                break;
            case MOUSE_PRIMARY_UP:
                this.primaryClickRelease();
                break;
            case MOUSE_PRIMARY_CLICK:
                this.primaryClick();
                break;
            case MOUSE_SECONDARY_DOWN:
                this.secondaryClickHold();
                break;
            case MOUSE_SECONDARY_UP:
                this.secondaryClickRelease();
                break;
            case MOUSE_SECONDARY_CLICK:
                this.secondaryClick();
                break;
            case MOUSE_SCROLL:
                this.mouseScroll();
                break;
            case MOUSE_MOVE:
//                this.moveJoyStick();
                this.moveTrackPad();
//                this.moveStandard();
                break;
            case KEY_DOWN:
                break;
            case KEY_HELD_DOWN:
                break;
            case KEY_UP:
                break;    
        }
    }
    
    public void useAutoDelay(){
        
        this.robot.setAutoDelay(this.autoDelay);    
        this.robot.setAutoWaitForIdle(true);
        
    }
    
    public void stopAutoDelay(){
        
        this.robot.setAutoDelay(this.autoDelay);    
        this.robot.setAutoWaitForIdle(false); 
        
    }
    
    @Override
    public void updateHandPosition(Integer x, Integer y, Integer z){
        
        this.handCurrentX = x;
        this.handCurrentY = y;  
        this.handCurrentZ = z;
        
    }
}
