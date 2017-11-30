/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;
import com.leapmotion.leap.Vector;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 *
 * @author Cameron
 */
public class DecisionTreeNode {
//    private class 
    private String trueBool = "true";
    private String falseBool = "false";
    private HashMap<Object, DecisionTreeNode> attributeValueToOutcome;
    
    private DecisionTree.Attribute attribute;
    private ArrayList<DecisionTree.Attribute> usedAttributes;
    public Boolean visited = false;
    private DecisionTreeCell graphic;
    
    public DecisionTreeNode(){
        Event.registerHandler(Event.TYPE.FIND_GESTURE_INITIATED_IN_DECISION_TREE, (Event event) -> {
            updateVisited(false);
        });
    }
    
    public void setGraphic(DecisionTreeCell graphic){
        this.graphic = graphic;
    }
    
    public ArrayList<DecisionTreeNode> getChildren(){
        if(attributeValueToOutcome == null){
            return new ArrayList();
        }
        return new ArrayList(attributeValueToOutcome.values());
    }
    
    public DecisionTreeNode(DecisionTree.Attribute attribute){
        this.attribute = attribute;
    }
    
    public Boolean isGesture(){
        return false;
    }
    
    public DecisionTree.Attribute getAttribute(){
        return attribute;
    }
    
    public void setAttribute(DecisionTree.Attribute attribute){
        this.attribute = attribute;
    }
    
    public void setConditionalNode(Object value, DecisionTreeNode node) throws Exception{
        if(attribute == null){
            throw new Exception("Set this node's attribute first");
        }
        switch(attribute){
            case INDEX_EXTENDED:
            case MIDDLE_EXTENDED:
            case RING_EXTENDED:
            case PINKY_EXTENDED:
            case THUMB_EXTENDED:
                //vaue should be boolean
                if(!value.getClass().getName().equals(Boolean.class.getName())){
                   throw new Exception("Attribute " + attribute + " expects a boolean value"); 
                }
                Boolean boolVal = (Boolean)value;
                if(attributeValueToOutcome == null){
                    attributeValueToOutcome = new HashMap<>();
                }
                if(boolVal){
                    attributeValueToOutcome.put(this.trueBool, node);
                } else {
                    attributeValueToOutcome.put(this.falseBool, node);
                }
                break;
            case PALM_NORMAL:
            case INDEX_DIRECTION:
            case MIDDLE_DIRECTION:
            case RING_DIRECTION:
            case PINKY_DIRECTION:
            case THUMB_DIRECTION:
                /*
            case INDEX_METACARPAL_DIRECTION:
            case INDEX_INTERMEDIATE_DIRECTION:
            case INDEX_PROXIMAL_DIRECTION:
            case INDEX_DISTAL_DIRECTION:
            case MIDDLE_METACARPAL_DIRECTION:
            case MIDDLE_INTERMEDIATE_DIRECTION:
            case MIDDLE_PROXIMAL_DIRECTION:
            case MIDDLE_DISTAL_DIRECTION:
            case RING_METACARPAL_DIRECTION:
            case RING_INTERMEDIATE_DIRECTION:
            case RING_PROXIMAL_DIRECTION:
            case RING_DISTAL_DIRECTION:
            case PINKY_METACARPAL_DIRECTION:
            case PINKY_INTERMEDIATE_DIRECTION:
            case PINKY_PROXIMAL_DIRECTION:
            case PINKY_DISTAL_DIRECTION:
            case THUMB_INTERMEDIATE_DIRECTION:
            case THUMB_PROXIMAL_DIRECTION:
            case THUMB_DISTAL_DIRECTION:
                */
                //value should be Vector
                if(!value.getClass().getName().equals(GestureVector.class.getName())){
                    throw new Exception("Attribute " + attribute + " expects a GestureVector");
                }
                if(attributeValueToOutcome == null){
                    attributeValueToOutcome = new HashMap<>();
                }
                attributeValueToOutcome.put(value, node);
                break;
        }
    }
    
    public void setUsedAttributes(ArrayList<DecisionTree.Attribute> attributes){
        usedAttributes = new ArrayList<>(attributes);
    }
    
    public ArrayList<DecisionTree.Attribute> getUsedAttributes(){
        ArrayList<DecisionTree.Attribute> attributes = new ArrayList<>(usedAttributes);
        return attributes;
    }
    
    public HashMap<DecisionTreeNode, Double> getNextNodesByValueWithConfidence(Object value) throws Exception{
        updateVisited(true);
        HashMap<DecisionTreeNode, Double> nodeList = new HashMap<>();
        if(attributeValueToOutcome == null){
            return nodeList;
        }
        switch(attribute){
            case INDEX_EXTENDED:
            case MIDDLE_EXTENDED:
            case RING_EXTENDED:
            case PINKY_EXTENDED:
            case THUMB_EXTENDED:
                //value should be boolean. compare against Boolean
                if(!value.getClass().getName().equals(Boolean.class.getName())){
                    throw new Exception("Attribute " + attribute + " must be given a boolean to be compared against");
                }
                Boolean boolVal = (Boolean)value;
                if(boolVal){
                    nodeList.put(attributeValueToOutcome.get(this.trueBool), 100.0);
                    nodeList.put(attributeValueToOutcome.get(this.falseBool), 0.0);
                } else {
                    nodeList.put(attributeValueToOutcome.get(this.falseBool), 100.0);
                    nodeList.put(attributeValueToOutcome.get(this.trueBool), 0.0);
                }
                break;
            case PALM_NORMAL:
            case INDEX_DIRECTION:
            case MIDDLE_DIRECTION:
            case RING_DIRECTION:
            case PINKY_DIRECTION:
            case THUMB_DIRECTION:
                /*
            case INDEX_METACARPAL_DIRECTION:
            case INDEX_INTERMEDIATE_DIRECTION:
            case INDEX_PROXIMAL_DIRECTION:
            case INDEX_DISTAL_DIRECTION:
            case MIDDLE_METACARPAL_DIRECTION:
            case MIDDLE_INTERMEDIATE_DIRECTION:
            case MIDDLE_PROXIMAL_DIRECTION:
            case MIDDLE_DISTAL_DIRECTION:
            case RING_METACARPAL_DIRECTION:
            case RING_INTERMEDIATE_DIRECTION:
            case RING_PROXIMAL_DIRECTION:
            case RING_DISTAL_DIRECTION:
            case PINKY_METACARPAL_DIRECTION:
            case PINKY_INTERMEDIATE_DIRECTION:
            case PINKY_PROXIMAL_DIRECTION:
            case PINKY_DISTAL_DIRECTION:
            case THUMB_INTERMEDIATE_DIRECTION:
            case THUMB_PROXIMAL_DIRECTION:
            case THUMB_DISTAL_DIRECTION:
*/
                //value should be Vector. compare against GestureVector
                if(!value.getClass().getName().equals(Vector.class.getName())){
                    throw new Exception("Attribute " + attribute + " must be given a Vector to be compared against");
                }
                
                attributeValueToOutcome.forEach((compare, node)-> {
                    GestureVector compareTo = (GestureVector)compare;
                    try{
                        float percentageSimilar = compareTo.getSimilarity((Vector)value);
//                        System.out.println("Percentage similar: " + percentageSimilar);
                        
                        nodeList.put(node, (double)percentageSimilar);
                    } catch (Exception e) {
                        Logger.getLogger(DecisionTreeNode.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
                break;
        }
        show("nodeList" + nodeList.toString());
        return nodeList;
    }
    
    private void show(String message){
        if(AdvancedRecognizer.interval == 0){
            System.out.println(message);
        }
    }
    
    private void updateVisited(Boolean visited){
        this.visited = visited;
        if(graphic != null){
            graphic.updateColor();
        }
    }
    
    @Override
    public String toString(){
        String string = "Decision tree node with attribute " + this.attribute + " and possible values ";
        if(attributeValueToOutcome == null){
            string += "- None";
        } else {
            for(Object key : attributeValueToOutcome.keySet()){
                if(key != null){
                    string += "\n * " + key.toString();
                } else {
                    string += "\n * null";
                }
            }
        }
        return string;
    }
}
