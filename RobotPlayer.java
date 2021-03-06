package team146;


import team146.data.*;
import team146.navigation.*;
import team146.state.*;
import team146.state.recycler.RecyclerInitialState;

import battlecode.common.*;

public class RobotPlayer implements Runnable {

	private final RobotController myRC;
	private RobotControls robotComps = new RobotControls();
	private RobotSchematic me;
	
	public RobotPlayer(RobotController rc) {
        myRC = rc;
    }
	
	@Override
	public void run() {
		while (myRC.components().length == 0 || robotComps.sensor == null || robotComps.mover == null) {
			myRC.yield(); // for equipping
			checkForNewComponents();
		}
		
		// we need to watch out here if robots are not becoming fully equipped before moving
		
		for (AbstractState current = getInitialState();;current.run(), current = current.getNextState()); // updates component list every state change, maybe not necessary

	}
	
	private AbstractState getInitialState() {
		if(myRC.getChassis()==Chassis.BUILDING) { //We'll eventually have to differentiate between recyclers, factories, and armories
			return new RecyclerInitialState(myRC, robotComps);
		}
		else if (robotComps.builder !=null && robotComps.builder.type()==ComponentType.CONSTRUCTOR && robotComps.sensor != null) {
			System.out.println("Scout");
			return new MineScoutState(myRC, robotComps, new Navigator(myRC.getLocation().add(Direction.NORTH, 20), robotComps)); //Need a better way of picking target or just telling it to roam
		}
		else {
			return new NavigationState(myRC, robotComps, new A_Star(myRC.getLocation().add(Direction.NORTH, 20), robotComps)); //for testing
		}
		// TODO add code to change to appropriate initial state, perhaps dependant on chassis and components
	}
	
	private void checkForNewComponents() {
		ComponentController [] components = myRC.newComponents();
		
		for (ComponentController c: components) {
			switch (c.componentClass()) {
				case ARMOR:
				case MISC:
					if (c instanceof JumpController)
						robotComps.addJump((JumpController)c);
					else
						robotComps.extra.add(c);
					break;
				case BUILDER:
					robotComps.builder = (BuilderController)c;
					break;
				case COMM:
					robotComps.comUnit = (BroadcastController)c;
					break;
				case MOTOR:
					robotComps.mover = (MovementController)c;
					break;
				case SENSOR:
					robotComps.sensor = (SensorController)c;
					break;
				case WEAPON:
					robotComps.weapons.add((WeaponController)c);
					break;
			}
		}	
	}
	
	//Tests how many ByteCodes a function or piece of code uses
	public void testBytecodeUsage() {
		while (true) {
			myRC.yield();
				//Place method to test here
			System.out.println(Clock.getBytecodeNum());
		}
	}
}