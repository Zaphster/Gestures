/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import com.leapmotion.leap.Vector;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class VectorRange implements JSONWritableReadable{
    private RadianRange xRange;
    private RadianRange yRange;
    private RadianRange zRange;
    
    public VectorRange(){
        this.xRange = new RadianRange();
        this.yRange = new RadianRange();
        this.zRange = new RadianRange();
    }
    
    public VectorRange(Double xCenter, Double xRange, Double yCenter, Double yRange, Double zCenter, Double zRange) throws Exception{
        this.xRange = new RadianRange(xCenter, xRange);
        this.yRange = new RadianRange(yCenter, yRange);
        this.zRange = new RadianRange(zCenter, zRange);
    }
    
    public VectorRange(Vector vector, Double range) throws Exception{
        this.xRange = new RadianRange(toDouble(vector.getX()), range);
        this.yRange = new RadianRange(toDouble(vector.getY()), range);
        this.zRange = new RadianRange(toDouble(vector.getZ()), range);
    }
    
    private Double toDouble(float value){
        return Double.parseDouble(Float.toString(value));
    }
    
    private Boolean contains(Double x, Double y, Double z){
        return xRange.contains(x) && yRange.contains(y) && zRange.contains(z);
    }
    
    public Boolean contains(Vector vector){
        return this.contains(toDouble(vector.getX()), toDouble(vector.getY()), toDouble(vector.getZ()));
    }
    
    private void setAllCenters(Double x, Double y, Double z) throws Exception{
        setXCenter(x);
        setYCenter(y);
        setZCenter(z);
    }

    public void setCenter(Vector vector) throws Exception{
        setAllCenters(toDouble(vector.getX()), toDouble(vector.getY()), toDouble(vector.getZ()));
    }
    
    public void setAllRanges(Double range) throws Exception{
        setXRange(range);
        setYRange(range);
        setZRange(range);
    }
    
    public void setAllRanges(float range) throws Exception{
        setAllRanges(toDouble(range));
    }
    
    public void setAllRanges(Double x, Double y, Double z) throws Exception{
        setXRange(x);
        setYRange(y);
        setZRange(z);
    }
    
    private void setXCenter(Double x) throws Exception{
        xRange.setCenter(x);
    }
    
    public void setXRange(Double x) throws Exception{
        xRange.setRange(x);
    }
    
    private void setYCenter(Double y) throws Exception{
        yRange.setCenter(y);
    }
    
    public void setYRange(Double y) throws Exception{
        yRange.setRange(y);
    }
    
    private void setZCenter(Double z) throws Exception{
        zRange.setCenter(z);
    }
    
    public void setZRange(Double z) throws Exception{
        zRange.setRange(z);
    }
    
    @Override
    public String toString(){
        return "{x: " + xRange + ", y: " + yRange + ", z: " + zRange + "}";
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
        obj.put("xRange", xRange.toJSONObject());
        obj.put("yRange", xRange.toJSONObject());
        obj.put("zRange", zRange.toJSONObject());
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        xRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("xRange"));
        yRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("yRange"));
        zRange.makeSelfFromJSONObject((JSONObject)jsonObject.get("zRange"));
        
    }
}
