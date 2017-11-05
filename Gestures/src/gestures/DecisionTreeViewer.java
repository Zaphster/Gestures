/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Cameron
 */
public class DecisionTreeViewer {
    private static Stage stage = new Stage();
    private static AnchorPane root = new AnchorPane();
    private static Point sceneSize = new Point(900, 1000);
    private static Scene scene;
    private static Point primaryNodePosition = new Point(450, 10);
    private static DecisionTreeCell treeRoot;
    private static HashMap<Integer, Integer> layerCounts;
    private static HashMap<Integer, Integer> rightMostPositionUsedForLayer;
    private static Integer heightBetweenNodes;
    static{
        root.setOnMouseMoved((MouseEvent event) -> {
//            System.out.println("mouse moved in anchorpane");
//            System.out.println(event.getTarget());
            if(DecisionTreeCell.class.isAssignableFrom(event.getTarget().getClass())){
//                System.out.println("found a decision tree cell")d;
                DecisionTreeCell cell = (DecisionTreeCell)event.getTarget();
                DecisionTreeNode data = cell.getData();
                if(data != null){
                    ToolTip.setText(data.toString());
                    ToolTip.show();
                    ToolTip.moveTo(event.getScreenX() + 5, event.getScreenY() + 5);
                }
            } else {
                ToolTip.hide();
            }
        });
        root.setOnMouseExited((event) -> {
            ToolTip.hide();
        });
    }
    
    
    public static void start(){
        if(true){
            return;
        }
        if (scene == null){
            scene = new Scene(root, sceneSize.x, sceneSize.y);
        }
        root.getChildren().clear();
        treeRoot = new DecisionTreeCell(DecisionTree.getRoot());
        treeRoot.setCenterX(primaryNodePosition.getX());
        treeRoot.setCenterY(primaryNodePosition.getY());
        layerCounts = new HashMap();
        rightMostPositionUsedForLayer = new HashMap();
        populateTree(DecisionTree.getRoot(), treeRoot);
        determineLayerCounts(treeRoot, 1);
        heightBetweenNodes = sceneSize.y / layerCounts.keySet().size();
        //System.out.println("height between nodes is " + heightBetweenNodes);
        viewTree(treeRoot, 1);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public static void populateTree(DecisionTreeNode decisionNode, DecisionTreeCell treeNode){
        //System.out.println("creating treeNode from decisionNode: " + decisionNode);
        if(decisionNode != null){
            ArrayList<DecisionTreeNode> decisionChildren = decisionNode.getChildren();
            decisionChildren.forEach((decisionChildNode) -> {
                DecisionTreeCell childTreeNode = new DecisionTreeCell(decisionChildNode);
                treeNode.getChildren().add(childTreeNode);
                populateTree(decisionChildNode, childTreeNode);
            });
        }
    }
    
    public static void determineLayerCounts(DecisionTreeCell node, Integer layer){
        Integer layerCount = layerCounts.get(layer);
        Integer newCount;
        if(layerCount == null){
            newCount = 1;
        } else {
            newCount = layerCount + 1;
        }
        layerCounts.put(layer, newCount);
        Integer nextLayer = layer + 1;
        for(DecisionTreeCell child : node.getChildren()){
            determineLayerCounts(child, nextLayer);
        }
    }
    
    public static void viewTree(DecisionTreeCell node, Integer layer){
        Integer layerCount = layerCounts.get(layer);
        if(layerCount == null){
            System.out.println("Layer count was null for layer: " + layer);
            return;
        }
        Integer spaceBetween = sceneSize.x / layerCount;
        Integer rightmostPosition = rightMostPositionUsedForLayer.get(layer);
        if(rightmostPosition == null){
            rightmostPosition = spaceBetween / 2;
        } else {
            rightmostPosition += spaceBetween;
        }
        rightMostPositionUsedForLayer.put(layer, rightmostPosition);
        double topPosition = (double)(layer * heightBetweenNodes);
        node.setCenterAndAnchor(rightmostPosition, topPosition);
        
        root.getChildren().add(node);
        Integer nextLayer = layer + 1;
        for(DecisionTreeCell cell : node.getChildren()){
            viewTree(cell, nextLayer);
            Line line = new Line();
            line.setStroke(Paint.valueOf("black"));
            line.setStartX(node.getBottomAnchor().x);
            line.setStartY(node.getBottomAnchor().y);
            line.setEndX(cell.getTopAnchor().x);
            line.setEndY(cell.getTopAnchor().y);
            root.getChildren().add(line);
        }
    }
   
    
    
}
