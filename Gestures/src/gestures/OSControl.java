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
    public void resetHandPosition();
    public void moveMouse();
    public void setMouseClickDelay(int delay);
    public void setKeyPressDelay(int delay);
    public void setPadSensitivityCoefficient(float coefficient);
    public void setUseZAxis(boolean useZAxis);
}
