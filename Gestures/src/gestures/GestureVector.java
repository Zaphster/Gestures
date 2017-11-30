/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class GestureVector implements JSONWritableReadable{
    private Vector thisVector;
    private static Double maxAngleTo = ((Double)Math.PI);
    
    
    
    public GestureVector(){
    }
    
    public GestureVector(Double x, Double y, Double z) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(x.floatValue());
        thisVector.setY(y.floatValue());
        thisVector.setZ(z.floatValue());
    }
    
    public GestureVector(Vector vector) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(vector.getX());
        thisVector.setY(vector.getY());
        thisVector.setZ(vector.getZ());
    }
    
    private Double toDouble(float value){
        return Double.parseDouble(Float.toString(value));
    }
    
    public float getPercentageDirectionComparison(Vector vector) throws Exception{
        checkVectorIsSet();
        float angleBetween = thisVector.angleTo(vector);
        return (float)((GestureVector.maxAngleTo - angleBetween) * 100 / GestureVector.maxAngleTo);
    }
    
    private void checkVectorIsSet() throws Exception{
        if(thisVector == null){
            throw new Exception("Set x, y, and z first");
        }
    }
    
    public float getPercentageDirectionComparison(Double x, Double y, Double z) throws Exception{
        Vector toCompare = new Vector();
        toCompare.setX(x.floatValue());
        toCompare.setY(y.floatValue());
        toCompare.setZ(z.floatValue());
        return getPercentageDirectionComparison(toCompare);
    }
    
    private void setVector(Double x, Double y, Double z) throws Exception{
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setVector(Vector vector) throws Exception{
        setVector(toDouble(vector.getX()), toDouble(vector.getY()), toDouble(vector.getZ()));
    }
    
    private void setX(Double x) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(x.floatValue());
    }
    
    private void setY(Double y) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setY(y.floatValue());
    }
    
    private void setZ(Double z) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setZ(z.floatValue());
    }
    
    @Override
    public String toString(){
        if(thisVector != null){
            return thisVector.toString();
        } else {
            return "Invalid vector";
        }
    }

    @Override
    public String makeJSONString() {
        return toJSONObject().toJSONString();
    }

    @Override
    public void makeSelfFromJSON(String json) {
        Object obj = JSONValue.parse(json);
        if(obj != null){
            JSONObject jsonObj = (JSONObject)obj;
            makeSelfFromJSONObject(jsonObj);
        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        if(thisVector == null){
            thisVector = new Vector();
        }
        obj.put("x", thisVector.getX());
        obj.put("y", thisVector.getY());
        obj.put("z", thisVector.getZ());
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        if(thisVector == null){
            thisVector = new Vector();
        }
        try {
            this.setX(Double.parseDouble(jsonObject.get("x").toString()));
            this.setY(Double.parseDouble(jsonObject.get("y").toString()));
            this.setZ(Double.parseDouble(jsonObject.get("z").toString()));
        } catch (Exception ex) {
            Logger.getLogger(GestureVector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
