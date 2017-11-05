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
public class TestSuite {
    public static void run(){
        try{
            //DegreeRange tests
            RadianRange range = new RadianRange(0.0, (Math.PI));
            //expect any result to be positive
            assert range.contains(0.0) == true:"Full range expected to contain 0";
            assert range.contains((359*(Math.PI/180))) == true:"Full range expected to contain 359";
            assert range.contains((-5*(Math.PI/180))) == true:"Full range expected to contain -5";
            assert range.contains((360*(Math.PI/180))) == true:"Full range expected to contain 360";

            RadianRange halfRange = new RadianRange(0.0, (90*(Math.PI/180)));
            assert halfRange.contains((0*(Math.PI/180))) == true:"270 to 90 expected to contain 0";
            assert halfRange.contains((90*(Math.PI/180))) == true:"270 to 90 expected to contain 90";
            assert halfRange.contains((91*(Math.PI/180))) == false:"270 to 90 not expected to contain 91";
            assert halfRange.contains((270*(Math.PI/180))) == true:"270 to 90 expected to contain 270";
            assert halfRange.contains((269*(Math.PI/180))) == false:"270 to 90 not expected to contain 269";
            assert halfRange.contains((-5*(Math.PI/180))) == true:"270 to 90 (-90 to 90) expected to contain -5";
            assert halfRange.contains((-90*(Math.PI/180))) == true:"270 to 90 (-90 to 90) expected to contain -90";
            assert halfRange.contains((-91*(Math.PI/180))) == false:"270 to 90 (-90 to 90) not expected to contain -91";

            RadianRange smallestRange = new RadianRange((50*(Math.PI/180)), 0.0);
            assert smallestRange.contains((50*(Math.PI/180))) == true:"50 to 50 expected to contain 50";
            assert smallestRange.contains((49*(Math.PI/180))) == false:"50 to 50 not expected to contain 49";
            assert smallestRange.contains((51*(Math.PI/180))) == false:"50 to 50 not expected to contain 51";
            assert smallestRange.contains(((360 + 50)*(Math.PI/180))) == true:"50 to 50 expected to contain 360 + 50 (one full circle + 50)";
            assert smallestRange.contains(((50 - 360)*(Math.PI/180))) == true:"50 to 50 expected to contain 50 - 360 (50 minus a full circle)";
            assert smallestRange.contains(((50 - 400)*(Math.PI/180))) == false:"50 to 50 not expected to contain 50 - 400";
            
            //DistanceRange tests
            DistanceRange distRange = new DistanceRange(0.0, (50*(Math.PI/180)));
            assert distRange.contains((-1*(Math.PI/180))) == false:"0 - 50 not expected to contain -1";
            assert distRange.contains((0*(Math.PI/180))) == true:"0 - 50 expected to contain 0";
            assert distRange.contains((5*(Math.PI/180))) == true:"0 - 50 expected to contain 5";
            assert distRange.contains((50*(Math.PI/180))) == true: "0 - 50 expected to contain 50";
            assert distRange.contains((51*(Math.PI/180))) == false: "0 - 50 not expected to contain 51";
            
            DistanceRange distRange2 = new DistanceRange(5.0, 3.0);
            assert distRange2.contains(1.0) == false:"2 - 8 not expected to contain 1";
            assert distRange2.contains(2.0) == true:"2 - 8 expected to contain 2";
            assert distRange2.contains(8.0) == true:"2 - 8 expected to contain 8";
            assert distRange2.contains(9.0) == false:"2 - 8 not expected to contain 9";
            assert distRange2.contains(-5000.0) == false:"2 - 8 not expected to contain -5000";
            assert distRange2.contains(5000.0) == false:"2 - 8 not expected to contain 5000";
                    
                    
                    
                    
                    
                    
                    
                    
        } catch (Exception e){
            Debugger.print(e.getMessage());
        }
    }
}
