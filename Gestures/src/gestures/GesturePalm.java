/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Cameron
 */
public class GesturePalm implements JSONWritableReadable{
    public GestureVector allowedVector;
    
    public GesturePalm(){
        allowedVector = new GestureVector();
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
        obj.put("allowedVector", allowedVector.toJSONObject());
        return obj;
    }

    @Override
    public void makeSelfFromJSONObject(JSONObject jsonObject) {
        allowedVector.makeSelfFromJSONObject((JSONObject)jsonObject.get("allowedVector"));
    }
}
