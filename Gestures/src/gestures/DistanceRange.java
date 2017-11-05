/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class DistanceRange implements JSONWritableReadable{
    private Double lowerBound;
    private Double upperBound;
    private Double center;
    private Double range;
    
    public DistanceRange(Double center, Double range) throws Exception{
        setBounds(center, range);
    }
    
    public Boolean contains(Double toCheck){
//        Debugger.print("Checking that " + toCheck + " is within the range " + lowerBound + " - " + upperBound);
        return lowerBound <= toCheck && toCheck <= upperBound;
    }
    
    private void setBounds(Double center, Double range) throws Exception{
        if(center >= 0 && range >= 0){
            this.center = center;
            this.range = range;
            lowerBound = Math.max(0, center - range);
            upperBound = center + range;
        } else {
            throw new Exception("Invalid center <" + center + "> or range <" + range + ">");
        }
    }
    
    public void setCenter(Double newCenter) throws Exception{
        setBounds(newCenter, range);
    }
    
    public void setRange(Double newRange) throws Exception{
        setBounds(center, newRange);
    }
    
    @Override
    public String toString(){
        return "{" + lowerBound + " - " + upperBound + "}";
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
        obj.put("center", center);
        obj.put("range", range);
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        center = Double.parseDouble(jsonObject.get("center").toString());
        range = Double.parseDouble(jsonObject.get("range").toString());
        try{
            this.setBounds(center, range);
        }catch(Exception e){
            Logger.getLogger(DistanceRange.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
