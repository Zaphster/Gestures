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
//    private RadianRange xRange;
//    private RadianRange yRange;
//    private RadianRange zRange;
    
//    private Double x;
//    private Double y;
//    private Double z;
    private Vector thisVector;
    private static Double maxAngleTo = ((Double)Math.PI);
    
    
    
    public GestureVector(){
//        this.xRange = new RadianRange();
//        this.yRange = new RadianRange();
//        this.zRange = new RadianRange();
    }
    
//    public GestureVector(Double xCenter, Double xRange, Double yCenter, Double yRange, Double zCenter, Double zRange) throws Exception{
//        this.xRange = new RadianRange(xCenter, xRange);
//        this.yRange = new RadianRange(yCenter, yRange);
//        this.zRange = new RadianRange(zCenter, zRange);
//    }
    
    public GestureVector(Double x, Double y, Double z) throws Exception{
//        this.x = x;
//        this.y = y;
//        this.z = z;
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(x.floatValue());
        thisVector.setY(y.floatValue());
        thisVector.setZ(z.floatValue());
    }
    
    public GestureVector(Vector vector) throws Exception{
//        this.x = toDouble(vector.getX());
//        this.y = toDouble(vector.getY());
//        this.z = toDouble(vector.getZ());
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(vector.getX());
        thisVector.setY(vector.getY());
        thisVector.setZ(vector.getZ());
    }
    
//    public GestureVector(Vector vector, Double range) throws Exception{
//        this.xRange = new RadianRange(toDouble(vector.getX()), range);
//        this.yRange = new RadianRange(toDouble(vector.getY()), range);
//        this.zRange = new RadianRange(toDouble(vector.getZ()), range);
//    }
    
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
    
//    private Boolean contains(Double x, Double y, Double z){
//        return xRange.contains(x) && yRange.contains(y) && zRange.contains(z);
//    }
    
//    public Boolean contains(Vector vector){
//        return this.contains(toDouble(vector.getX()), toDouble(vector.getY()), toDouble(vector.getZ()));
//    }
    
    private void setVector(Double x, Double y, Double z) throws Exception{
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setVector(Vector vector) throws Exception{
        setVector(toDouble(vector.getX()), toDouble(vector.getY()), toDouble(vector.getZ()));
    }
    
//    public void setAllRanges(Double range) throws Exception{
//        setXRange(range);
//        setYRange(range);
//        setZRange(range);
//    }
    
//    public void setAllRanges(float range) throws Exception{
//        setAllRanges(toDouble(range));
//    }
    
//    public void setAllRanges(Double x, Double y, Double z) throws Exception{
//        setXRange(x);
//        setYRange(y);
//        setZRange(z);
//    }
    
    private void setX(Double x) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setX(x.floatValue());
    }
    
//    public void setXRange(Double x) throws Exception{
//        xRange.setRange(x);
//    }
    
    private void setY(Double y) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setY(y.floatValue());
    }
    
//    public void setYRange(Double y) throws Exception{
//        yRange.setRange(y);
//    }
    
    private void setZ(Double z) throws Exception{
        if(thisVector == null){
            thisVector = new Vector();
        }
        thisVector.setZ(z.floatValue());
    }
    
//    public void setZRange(Double z) throws Exception{
//        zRange.setRange(z);
//    }
    
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
//        xRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("xRange"));
//        yRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("yRange"));
//        zRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("zRange"));
        
    }
}
