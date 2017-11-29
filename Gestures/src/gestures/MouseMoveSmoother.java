/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;

/**
 *
 * @author Cameron
 */
public class MouseMoveSmoother {
    private Robot robot;
    private ArrayList<Point> points;
    private double a = 0.2;
    private int minLength = 4;
    private int maxLength = 6;
    
    public MouseMoveSmoother(Robot robot){
        this.robot = robot;
        if(robot == null) {
            throw new NullPointerException();
        }
        points = new ArrayList<>();
    }
    
    public void smooth(int x, int y){
        Point point = new Point(x, y);
        points.add(point);
        if(points.size() >= 2){
            Point last = points.get(points.size() - 2);
            point.x = (int)(point.x * a + last.x * (1 - a));
            point.y = (int)(point.y * a + last.y * (1 - a));
        }
        robot.mouseMove(point.x, point.y);
//expsmoothedCanvas.smooth = function (ps) {
//    var a = 0.2;
//    var p = ps[ps.length - 1];
//    var p1 = ps[ps.length - 2];
//    ps[ps.length - 1] = {
//        x: p.x * a + p1.x * (1 - a),
//        y: p.y * a + p1.y * (1 - a)
//    };
//};
    }
}
