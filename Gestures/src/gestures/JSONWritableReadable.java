/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import org.json.simple.JSONObject;

/**
 *
 * @author Cameron
 */
public interface JSONWritableReadable {
    public String makeJSONString();
    public JSONObject toJSONObject();
    public void makeSelfFromJSON(String json);
    public void makeSelfFromJSONObject(JSONObject jsonObject);
}
