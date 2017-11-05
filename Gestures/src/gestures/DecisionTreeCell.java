/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.Point;
import java.util.ArrayList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Cameron
 */
public class DecisionTreeCell extends Circle {
    private DecisionTreeNode node;
    private ArrayList<DecisionTreeCell> children = new ArrayList();
    private static Double defaultRadius = 8.0;
    private Point topAnchor;
    private Point bottomAnchor;
    
    private DecisionTreeCell(){
        
    }
    
    public DecisionTreeCell(DecisionTreeNode node){
        this.node = node;
        this.setRadius(defaultRadius);
        this.setFill(Paint.valueOf("red"));
        if(node != null){
            node.setGraphic(this);
        }
//        this.setOnMouseClicked((event) -> {
//            System.out.println(node);
//        });
    }
    
    public DecisionTreeNode getData(){
        return node;
    }
    
    public ArrayList<DecisionTreeCell> getChildren(){
        return children;
    }
    
    public Point getTopAnchor(){
        return topAnchor;
    }
    
    public Point getBottomAnchor(){
        return bottomAnchor;
    }
    
    public void setCenterAndAnchor(double x, double y){
        super.setCenterX(x);
        super.setCenterY(y);
        this.topAnchor = new Point((int)this.getCenterX(), (int)(this.getCenterY() - this.radiusProperty().doubleValue()));
        this.bottomAnchor = new Point((int)this.getCenterX(), (int)(this.getCenterY() + this.radiusProperty().doubleValue()));
    }
    
    public void updateColor(){
        if(node.visited){
            this.setFill(Paint.valueOf("green"));
        } else {
            this.setFill(Paint.valueOf("red"));
        }
    }
    
}
