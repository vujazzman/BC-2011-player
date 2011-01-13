package hex.data;

import java.util.ArrayList;

import battlecode.common.*;

public class Extra {
	
	/** Return the direction from loc to tar */
	public static Direction dirTo(MapLocation loc, MapLocation tar) {
		return loc.directionTo(tar);
	}

	public static ArrayList<MapLocation> locsNextTo(MapLocation loc) {
		ArrayList<MapLocation> locs = new ArrayList<MapLocation>();
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y<2; y++) {
				if (!(x==0 && y==0))
					locs.add(loc.add(x,y));
			}
		}
		return locs;
	}

	public static boolean canMove(MovementController motor, MapLocation loc, MapLocation tar) {
		return motor.canMove(dirTo(loc, tar));
	}

	//Returns a copy of an ArrayList
	//at least for integers. not sure how to do it for generic object
	public static ArrayList<Integer> copyIntAL(ArrayList<Integer> orig) {
		ArrayList<Integer> clone = new ArrayList<Integer>();
		for (int or : orig) {
			clone.add(or);
		}
		return clone;
	}
	//Returns a copy of an ArrayList
	//at least for Directions. not sure how to do it for generic object
	public static ArrayList<Direction> copyDirAL(ArrayList<Direction> orig) {
		ArrayList<Direction> clone = new ArrayList<Direction>();
		for (Direction or : orig) {
			clone.add(or);
		}
		return clone;
	}
	
	public static boolean senseIfClear(RobotControls control, MapLocation loc) throws GameActionException {
		boolean a= control.hasSensor() && control.sensor.canSenseSquare(loc) && control.sensor.senseObjectAtLocation(loc, RobotLevel.ON_GROUND) == null;
		boolean b = !control.hasSensor() || !control.sensor.canSenseSquare(loc);
		return a || b;
	}
}