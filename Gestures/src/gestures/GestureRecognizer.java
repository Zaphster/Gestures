/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;
import com.leapmotion.leap.Frame;
/**
 *
 * @author Cameron
 */
public interface GestureRecognizer {
    public void scan(Frame frame);
}
