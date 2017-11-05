/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.Point;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Cameron
 */
public class ToolTip {
    private static final Stage STAGE;
    private static final AnchorPane ROOT;
    private static final Text TEXTFIELD;
    private static final Scene SCENE;
    private static final Point SIZE = new Point(300, 200);
    static{
        STAGE = new Stage(StageStyle.UNIFIED);
        ROOT = new AnchorPane();
        ROOT.setMaxSize(SIZE.x, SIZE.y);
        TEXTFIELD = new Text(SIZE.x, SIZE.y, "");
        TEXTFIELD.setWrappingWidth(SIZE.x);
        AnchorPane.setTopAnchor(TEXTFIELD, 5.0);
        AnchorPane.setLeftAnchor(TEXTFIELD, 5.0);
        AnchorPane.setBottomAnchor(TEXTFIELD, 5.0);
        AnchorPane.setRightAnchor(TEXTFIELD, 5.0);
        ROOT.getChildren().add(TEXTFIELD);
        SCENE = new Scene(ROOT, SIZE.x, SIZE.y);
        STAGE.setScene(SCENE);
    }
    
    public static void show(){
//        System.out.println("showing tooltip");
        STAGE.show();
    }
    
    public static void moveTo(double x, double y){
        STAGE.setX(x);
        STAGE.setY(y);
    }
    
    public static void hide(){
        STAGE.hide();
    }
    
    public static void setText(String text){
//        System.out.println("setting tooltip text to " + text);
        TEXTFIELD.setText(text);
    }
    
}
