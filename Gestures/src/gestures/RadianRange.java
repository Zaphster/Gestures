/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class RadianRange implements JSONWritableReadable{
    public static Boolean debug = Capstone2_Group5.debug;
    static{
//        debug = ;
    }

    private static final String rads = "rads";
    private Double lowerBound;
    private Double upperBound;
    private Double center = 0.0;
    private Double range = 0.0;
    private static final Float RADIANSPERCIRCLE = (float)(2*Math.PI);
    private static final Float MINRANGE = (float)(0.0001f * (180/Math.PI));
    
    public RadianRange(){
        
    }

    public RadianRange(Double center, Double range) throws Exception {
        setBounds(center, range);
    }

    public Boolean contains(Double toCheck) {
        if (toCheck == null) {
            return false;
        }
        toCheck = preventNegative(toCheck) % RADIANSPERCIRCLE;
        Debugger.print("Checking that " + toCheck + " is between " + lowerBound + " and " + upperBound);
        if (lowerBound < upperBound || (Objects.equals(lowerBound, upperBound) && Objects.equals(range, MINRANGE))) {
            //normal expected behavior
            return lowerBound <= toCheck && toCheck <= upperBound;
        } else {
            //when the bounds wrap around the 0/360 mark
            return toCheck >= lowerBound || toCheck <= upperBound;
        }
    }

    private Double preventNegative(Double num) {
        while (num < 0) {
            num += RADIANSPERCIRCLE;
        }
        return num;
    }
    
    private void setBounds(Double center, Double range) throws Exception{
        if (range < 0) {
            throw new Exception("Range must be >= 0");
        }
        center = (center) % RADIANSPERCIRCLE; //center can be anywhere on full circle
        this.center = center;
        //never allow range to go below .0001 
        //this allows for the error in float calculations to still give good results
        range = Math.max(MINRANGE, Math.min(RADIANSPERCIRCLE/2, range));   //range can extend 180 degrees in either direction.
        this.range = range;

        // range of 0 means must be exactly on the center
        // range of PI means can it be anywhere in the circle
        lowerBound = preventNegative(center - range) % RADIANSPERCIRCLE;
        upperBound = preventNegative(center + range) % RADIANSPERCIRCLE;
    }
    
    public void setCenter(Double newCenter) throws Exception{
        setBounds(newCenter, range);
    }
    
    public void setRange(Double newRange) throws Exception{
        setBounds(center, newRange);
    }

    @Override
    public String toString() {
        return "{" + lowerBound + " " + rads + " - " + upperBound + " " + rads + "}";
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
        } catch (Exception e){
            Logger.getLogger(RadianRange.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
}
