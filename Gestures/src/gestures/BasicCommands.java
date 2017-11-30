/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javafx.animation.Timeline;

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
 * 
 * 
 */

public class BasicCommands implements OSControl{
    
    private Robot robot;
    
    private int autoDelay = 0;
    private int keyPressDelay = 50;
    private int mouseClickDelay = 50;
    private int mouseMovementDelay = 0;
    
    private int screenHeight;
    private int screenWidth;
    
    private int mousePositionX;
    private int mousePositionY;
    
    private float padSensitivity_coefficient = 500;
    
    private boolean useZAxis = false;
    
    private int handCurrentX;
    private int handCurrentY;
    private int handCurrentZ;
    
    private int handDeltaX;
    private int handDeltaY;
    private int handDeltaZ;
    private boolean handReset = true;
    
    private MouseMoveSmoother smoother;

    public BasicCommands(){
            
        GraphicsDevice screenDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point pointer = getMousePosition();
        
        try{
            robot = new Robot(screenDev);
            smoother = new MouseMoveSmoother(robot);
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
    
    private void smoothMove(int x, int y){
        smoother.smooth(x, y);
    }
    
    private double getPadSensitivity(){
        double hypotenuse;
        if(useZAxis) {
            hypotenuse = hypotenuse(this.handDeltaX, this.handDeltaZ);
        } else {
            hypotenuse = hypotenuse(this.handDeltaX, this.handDeltaY);
        }
//        System.out.println("hypotenuse: " + hypotenuse);
        return hypotenuse * padSensitivity_coefficient;
    }
    
    private double distance(int x1, int y1, int x2, int y2){
        int xDist = x2 - x1;
        int yDist = y2 - y1;
        return hypotenuse(xDist, yDist);
    }
    
    private double hypotenuse(int x, int y){
        int squared = (x * x) + (y * y);
        double sqrt = Math.sqrt(squared);
//        System.out.println("x: " + x + ", y: " + y + ", x^2 + y^2: " + squared + ", sqrt: " + sqrt);
        return sqrt;
    }
    
    private void moveStandard(){
        double padSensitivity = getPadSensitivity();
        int moveAmountX = (int)Math.round(this.handDeltaX * padSensitivity / 100);
        int moveAmountY = (int)Math.round(this.handDeltaY * padSensitivity / 100);
        int moveAmountZ = (int)Math.round(this.handDeltaZ * padSensitivity / 100);
        
        Point nextMousePosition = getMousePosition();
        if(useZAxis == true){
            nextMousePosition.x += moveAmountX;
            nextMousePosition.y -= moveAmountZ;
        }else{
            nextMousePosition.x += moveAmountX;
            nextMousePosition.y -= moveAmountY;
        }
        smoothMove(nextMousePosition.x, nextMousePosition.y);
    }
    
    public void mouseScroll(){
        
        int z_pos = this.handCurrentZ;
        int y_pos = this.handCurrentY;
        
        if(useZAxis == true){
            robot.mouseWheel(z_pos);
        }else{
            robot.mouseWheel((y_pos*(-1)));
        }
        
    }

    @Override
    public void performCommand(Command command) {

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
            case BACKSPACE_HELD_DOWN:
                this.holdKey(KeyEvent.VK_BACK_SPACE);
                break;
            case BACKSPACE_RELEASE:
                this.releaseKey(KeyEvent.VK_BACK_SPACE);
                break;
            case BACKSPACE_PRESS:
                this.pressKey(KeyEvent.VK_BACK_SPACE);
                break;
            case ENTER_HELD_DOWN:
                this.holdKey(KeyEvent.VK_ENTER);
                break;
            case ENTER_RELEASE:
                this.releaseKey(KeyEvent.VK_ENTER);
                break;
            case ENTER_PRESS:
                this.pressKey(KeyEvent.VK_ENTER);
                break;
            case SHIFT_HELD_DOWN:
                this.holdKey(KeyEvent.VK_SHIFT);
                break;
            case SHIFT_RELEASE:
                this.releaseKey(KeyEvent.VK_SHIFT);
                break;
            case SHIFT_PRESS:
                this.pressKey(KeyEvent.VK_SHIFT);
                break;
            case TAB_HELD_DOWN:
                this.holdKey(KeyEvent.VK_TAB);
                break;
            case TAB_RELEASE:
                this.releaseKey(KeyEvent.VK_TAB);
                break;
            case TAB_PRESS:
                this.pressKey(KeyEvent.VK_TAB);
                break;
            case CAPSLOCK_PRESS:
                this.holdKey(KeyEvent.VK_CAPS_LOCK);
                break;
            case F12_PRESS:
                this.holdKey(KeyEvent.VK_F12);
                break;
            case F11_PRESS:
                this.holdKey(KeyEvent.VK_F11);
                break;
            case F10_PRESS:
                this.holdKey(KeyEvent.VK_F10);
                break;
            case F9_PRESS:
                this.holdKey(KeyEvent.VK_F9);
                break;
            case F8_PRESS:
                this.holdKey(KeyEvent.VK_F8);
                break;
            case F7_PRESS:
                this.holdKey(KeyEvent.VK_F7);
                break;
            case F6_PRESS:
                this.holdKey(KeyEvent.VK_F6);
                break;
            case F5_PRESS:
                this.holdKey(KeyEvent.VK_F5);
                break;
            case F4_PRESS:
                this.holdKey(KeyEvent.VK_F4);
                break;
            case F3_PRESS:
                this.holdKey(KeyEvent.VK_F3);
                break;
            case F2_PRESS:
                this.holdKey(KeyEvent.VK_F2);
                break;
            case F1_PRESS:
                this.holdKey(KeyEvent.VK_F1);
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
        if(handReset){
            handDeltaX = 0;
            handDeltaY = 0;
            handDeltaZ = 0;
        } else {
            handDeltaX = x - handCurrentX;
            handDeltaY = y - handCurrentY;
            handDeltaZ = z - handCurrentZ;
        }
        
        handCurrentX = x;
        handCurrentY = y;  
        handCurrentZ = z;
        handReset = false;
    }
    
    @Override
    public void moveMouse(){
        moveStandard();
    }
    
    @Override
    public void resetHandPosition(){
        handReset = true;
    }

    private Point getMousePosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
