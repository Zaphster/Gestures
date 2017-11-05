/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

/**
 *
 * @author Cameron
 */
public interface OSControl {
    public void performCommand(Command command);
    public void updateHandPosition(Integer x, Integer y, Integer z);
}
