/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Cameron
 */
public class DecisionTree {
    private static DecisionTreeNode root;
    private static ArrayList<Gesture> gestureList = new ArrayList<>();
    private static DecisionTreeNode nextNode;
    private static Object valueToCheck;
    private static Hand hand;
    private static ArrayList<DecisionTreeNode> nodeList = new ArrayList<>();
    private static ArrayList<Gesture> gesturesFound = new ArrayList<>();
    private static Event gesturesTooSimilar = new Event(Event.TYPE.GESTURES_TOO_SIMILAR);
    private static Event gestureSearchBegins = new Event(Event.TYPE.FIND_GESTURE_INITIATED_IN_DECISION_TREE);
    private static int tooSimilarListener;
    static{
        tooSimilarListener = Event.registerHandler(Event.TYPE.GESTURES_TOO_SIMILAR, (Event event) -> {
            System.out.print("Gestures are too similar: ");
            ArrayList<Gesture> gestures = (ArrayList<Gesture>)event.get("gestures");
            if(gestures != null){
                for(Gesture gesture : (ArrayList<Gesture>)event.get("gestures")){
                    System.out.print(gesture.name + " - ");
                }
            }
        });
    }
    
    private DecisionTree(){
    }
    
    public enum Attribute{
        INDEX_EXTENDED,
        MIDDLE_EXTENDED,
        RING_EXTENDED,
        PINKY_EXTENDED,
        THUMB_EXTENDED,
        PALM_NORMAL,
        //GRAB_ANGLE,  //not available on Linux
        //PINCH_DISTANCE,
        INDEX_DIRECTION,
        MIDDLE_DIRECTION,
        RING_DIRECTION,
        PINKY_DIRECTION,
        THUMB_DIRECTION,
        INDEX_METACARPAL_DIRECTION,
        INDEX_INTERMEDIATE_DIRECTION,
        INDEX_PROXIMAL_DIRECTION,
        INDEX_DISTAL_DIRECTION,
        MIDDLE_METACARPAL_DIRECTION,
        MIDDLE_INTERMEDIATE_DIRECTION,
        MIDDLE_PROXIMAL_DIRECTION,
        MIDDLE_DISTAL_DIRECTION,
        RING_METACARPAL_DIRECTION,
        RING_INTERMEDIATE_DIRECTION,
        RING_PROXIMAL_DIRECTION,
        RING_DISTAL_DIRECTION,
        PINKY_METACARPAL_DIRECTION,
        PINKY_INTERMEDIATE_DIRECTION,
        PINKY_PROXIMAL_DIRECTION,
        PINKY_DISTAL_DIRECTION,
        THUMB_INTERMEDIATE_DIRECTION,
        THUMB_PROXIMAL_DIRECTION,
        THUMB_DISTAL_DIRECTION
    };
    
    public static DecisionTreeNode getRoot(){
        return root;
    }
    
    public static void create(ArrayList<Gesture> gestureList) throws Exception{
        root = null;
        System.out.println("creating decision tree with gestures");
        gestureList.forEach((gesture) -> {
            System.out.println(gesture.name);
        });
        build(root, gestureList);
    }
    
    private static DecisionTree.Attribute determineBestAttribute(ArrayList<Gesture> gestureList, ArrayList<DecisionTree.Attribute> usedAttributes) throws Exception{
        HashMap<Attribute, Integer> distribution = new HashMap<>();
        int gestureCount = gestureList.size();
        if(gestureCount == 0){
            throw new Exception("Cannot determine best attribute with a list of 0 gestures");
        }
        int idealSplit = gestureCount/2;
        
        Attribute bestAttribute = null;
        for(Attribute att : Attribute.values()){
            if(!usedAttributes.contains(att)){
                HashMap<Object, Integer> numGesturesWithValues = new HashMap<>();
                //logic to determine best attribute
                //get number of similar values for the attribute
                for(Gesture gesture : gestureList){
                    Object value = gesture.getAttributeValue(att);
                    if(numGesturesWithValues.containsKey(value)){
                        Integer num = numGesturesWithValues.get(value);
                        num += 1;
                        numGesturesWithValues.put(value, num);
                    } else {
                        numGesturesWithValues.put(value, 1);
                    }
                }
                //find the difference between the values and the idealSplit, averaged over the number of different values
                int numDifferentValues = numGesturesWithValues.entrySet().size();
                int differenceTotal = 0;
                for(Entry<Object, Integer> entry : numGesturesWithValues.entrySet()){
                    differenceTotal += Math.abs(idealSplit - entry.getValue());
                }
                if(numDifferentValues == 0){
                    throw new Exception("No values found in gestures? Shouldn't be able to happen");
                }
                int averageDifference = differenceTotal/numDifferentValues;
                distribution.put(att, averageDifference);
            }
        }
        int lowestDifference = Integer.MAX_VALUE;
        for(Entry<Attribute, Integer> entry : distribution.entrySet()){
            if(entry.getValue() < lowestDifference){
                lowestDifference = entry.getValue();
                bestAttribute = entry.getKey();
            }
        }
        return bestAttribute;
    }
    
    private static void build(DecisionTreeNode currentNode, ArrayList<Gesture> gestureList) throws Exception{
        System.out.println("Start of build() - Current node:" + currentNode);
        if(gestureList.isEmpty()){
            return; //node will have no children.  a node with no children will return null when attempting to get its children by a valueToCheck
        }
        ArrayList<DecisionTree.Attribute> usedAttributes;
        if(currentNode == null){
            root = new DecisionTreeNode();
            currentNode = root;
            usedAttributes = new ArrayList<>();
        } else {
            usedAttributes = currentNode.getUsedAttributes();
        }
        DecisionTree.Attribute bestAttribute = determineBestAttribute(gestureList, usedAttributes);
        if(bestAttribute == null){
            throw new Exception("No best attribute? Shouldn't be able to arrive here");
        }
        currentNode.setAttribute(bestAttribute);
        usedAttributes.add(bestAttribute);
        HashMap<Object, ArrayList<Gesture>> valuesAndMatchingGestures = getPossibleValuesAndMatchingGestures(gestureList, bestAttribute);
        if(usedAttributes.size() == DecisionTree.Attribute.values().length){
            //gestures are the next nodes
            for(Entry<Object, ArrayList<Gesture>> entry: valuesAndMatchingGestures.entrySet()){
                Object value = entry.getKey();
                ArrayList<Gesture> gestures = entry.getValue();
                if(gestures.isEmpty()){
                    currentNode.setConditionalNode(value, null);
                } else if(gestures.size() == 1){
                    currentNode.setConditionalNode(value, gestures.get(0));
                } else {
                    currentNode.setConditionalNode(value, null);
                    Debugger.print("Multiple gestures ended up on this leaf.  None will be applied");
                }
            }
            System.out.println("   Node is now a leaf node - " + currentNode);
        } else {
            //create children
            for(Entry<Object, ArrayList<Gesture>> entry : valuesAndMatchingGestures.entrySet()){
                Object value = entry.getKey();
                ArrayList<Gesture> gestures = entry.getValue();
                if(gestures.size() > 0){
                    //if there are gestures associated with this valueToCheck
                    DecisionTreeNode child = new DecisionTreeNode();
                    child.setUsedAttributes(usedAttributes);
                    currentNode.setConditionalNode(value, child);
                    System.out.println("   Node is a branch node with children - " + currentNode);
                    build(child, gestures);
                } else {
                    //if no gestures are associated with this valueToCheck
                    currentNode.setConditionalNode(value, null);
                    System.out.println("   Node has no children");
                }
            }
        }
    }
    
    private static HashMap<Object, ArrayList<Gesture>> getPossibleValuesAndMatchingGestures(ArrayList<Gesture> gestureList, DecisionTree.Attribute bestAttribute){
        HashMap<Object, ArrayList<Gesture>> values = new HashMap<>();
        gestureList.forEach((gesture) -> {
            Object value = gesture.getAttributeValue(bestAttribute);
            if (!values.containsKey(value)) {
                ArrayList<Gesture> gestures = new ArrayList<>();
                gestures.add(gesture);
                values.put(gesture.getAttributeValue(bestAttribute), gestures);
            } else {
                values.get(value).add(gesture);
            }
        });
        return values;
    }
    
    public static Boolean nodeListHasNodeThatIsNotGesture(ArrayList<DecisionTreeNode> list){
        if(list == null || list.isEmpty()){
            return false;
        }
        Boolean hasNonGestureNode = false;
        for(DecisionTreeNode node : list){
            if(node != null && node.isGesture() == false){
                hasNonGestureNode = true;
                break;
            }
        }
        return hasNonGestureNode;
    }
    
//    public static ArrayList<DecisionTreeNode> getSearchedNodes(){
//        return new ArrayList<>(nodeList);
//    }
    
    public static Gesture findGesture(Frame frame)throws Exception{
        gestureSearchBegins.trigger();
       // System.out.println("Looking for gesture in frame " + frame);
        nodeList.clear();
        nodeList.add(root);
//        while(nextNode != null && nextNode.isGesture() == false){
        while(nodeListHasNodeThatIsNotGesture(nodeList)){
            nextNode = nodeList.remove(0);
            hand = frame.hands().frontmost();
            valueToCheck = null;
            switch(nextNode.getAttribute()){
                case INDEX_EXTENDED:
                    valueToCheck = (Boolean)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).isExtended();
                    break;
                case MIDDLE_EXTENDED:
                    valueToCheck = (Boolean)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).isExtended();
                    break;
                case RING_EXTENDED:
                    valueToCheck = (Boolean)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).isExtended();
                    break;
                case PINKY_EXTENDED:
                    valueToCheck = (Boolean)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).isExtended();
                    break;
                case THUMB_EXTENDED:
                    valueToCheck = (Boolean)hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0).isExtended();
                    break;
                case PALM_NORMAL:
                    valueToCheck = (Vector)hand.palmNormal();
                    break;
                case INDEX_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).direction();
                    break;
                case MIDDLE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).direction();
                    break;
                case RING_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).direction();
                    break;
                case PINKY_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).direction();
                    break;
                case THUMB_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0).direction();
                    break;
                case INDEX_METACARPAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).bone(Bone.Type.TYPE_METACARPAL).direction();
                    break;
                case INDEX_INTERMEDIATE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction();
                    break;
                case INDEX_PROXIMAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).bone(Bone.Type.TYPE_PROXIMAL).direction();
                    break;
                case INDEX_DISTAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_INDEX).get(0).bone(Bone.Type.TYPE_DISTAL).direction();
                    break;
                case MIDDLE_METACARPAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).bone(Bone.Type.TYPE_METACARPAL).direction();
                    break;
                case MIDDLE_INTERMEDIATE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction();
                    break;
                case MIDDLE_PROXIMAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).bone(Bone.Type.TYPE_PROXIMAL).direction();
                    break;
                case MIDDLE_DISTAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE).get(0).bone(Bone.Type.TYPE_DISTAL).direction();
                    break;
                case RING_METACARPAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).bone(Bone.Type.TYPE_METACARPAL).direction();
                    break;
                case RING_INTERMEDIATE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction();
                    break;
                case RING_PROXIMAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).bone(Bone.Type.TYPE_PROXIMAL).direction();
                    break;
                case RING_DISTAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_RING).get(0).bone(Bone.Type.TYPE_DISTAL).direction();
                    break;
                case PINKY_METACARPAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).bone(Bone.Type.TYPE_METACARPAL).direction();
                    break;
                case PINKY_INTERMEDIATE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction();
                    break;
                case PINKY_PROXIMAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).bone(Bone.Type.TYPE_PROXIMAL).direction();
                    break;
                case PINKY_DISTAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_PINKY).get(0).bone(Bone.Type.TYPE_DISTAL).direction();
                    break;
                case THUMB_INTERMEDIATE_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0).bone(Bone.Type.TYPE_INTERMEDIATE).direction();
                    break;
                case THUMB_PROXIMAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0).bone(Bone.Type.TYPE_PROXIMAL).direction();
                    break;
                case THUMB_DISTAL_DIRECTION:
                    valueToCheck = (Vector)hand.fingers().fingerType(Finger.Type.TYPE_THUMB).get(0).bone(Bone.Type.TYPE_DISTAL).direction();
                    break;
            }
            nodeList.addAll(nextNode.getNextNodesByValue(valueToCheck));
        }
        gesturesFound.clear();
        for(DecisionTreeNode node : nodeList){
            if(node != null && node.isGesture()){
                gesturesFound.add((Gesture)node);
            }
        }
        if(gesturesFound.size() == 1){
            return gesturesFound.get(0);
        } else if (gesturesFound.size() > 1){
            gesturesTooSimilar.addDetail("gestures", new ArrayList<>(gesturesFound));
            gesturesTooSimilar.trigger();
        }
        return null;
//        if(nextNode != null && nextNode.isGesture()){
//            return (Gesture)nextNode;
//        } else {
//            return null;
//        }

    }
    
    public static void view(){

    }

}
