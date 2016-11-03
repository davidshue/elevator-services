package com.challenge.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by dshue1 on 10/28/16.
 */
public class CallSystem {
	private final Log logger = LogFactory.getLog(getClass());

	private ArrayList<Elevator> elevators = new ArrayList<>();

	/*
	public static void main(String[] args) throws Exception {
		Random random = new Random();

		CallSystem cs = new CallSystem(8, 100);
		for(int i = 0; i < 100; i++) {
			CallRequestThread t1 = new CallRequestThread(cs, new CallRequest(Direction.up, random.nextInt(99) + 1));
			CallRequestThread t2 = new CallRequestThread(cs, new CallRequest(Direction.down, random.nextInt(99) + 2));
			t1.start();
			t2.start();
		}

		// Every 5 seconds, print out a snapshot of all elevator status
		Timer timer = new Timer();
		timer.schedule(new SnapshotTask(cs), 5000, 20000);

		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.println("Please indicate your current floor level between 1 and 100 and your intention up/down, like 35 up, or 25 down:");
			try {
				int floor = in.nextInt();
				String dir = in.next();
				Direction direction = Direction.valueOf(dir);
				Elevator elev = cs.request(new CallRequest(direction, floor));
				System.out.println("*** elevator " + elev.getNumber() + " is coming to you now. ***");
			} catch(Exception e) {
				System.out.println("wrong format.");
			}
		}
	}
	*/

	/**
	 * setting up the call system, with the total number of elevators and total number of stories
	 * @param totalEvevators
	 * @param totalLevels
	 */
	public CallSystem(int totalEvevators, int totalLevels) {
		for (int i = 1; i <= totalEvevators; i++) {
			elevators.add(new Elevator(i, totalLevels));
		}
		elevators.forEach((elevator) -> elevator.start());
	}

	public List<Elevator> getElevators() {
		return elevators;
	}

	public Elevator getElevator(int num) {
		return elevators.get(num - 1);
	}


	// When a call request comes in, it will reprioritize all the running elevators to that request and dispatch the one with the highest priority
	// to the user
	public Elevator request(CallRequest callRequest) {
		List<Elevator> elevatorsCopy = (List) elevators.clone();
		Collections.sort(elevatorsCopy, (o1, o2) -> o2.getPriority(callRequest) - o1.getPriority(callRequest));

		Elevator e = elevatorsCopy.get(0);
		// The elevator that is responding to the call request will behave just like someone has pressed a button that
		// corresponding to the requested level
		e.respond(callRequest.getCurrentLevel());
		logger.info("elevator " + e.getNumber() + " responded to request " + callRequest);
		return e;
	}
}
