package com.challenge.elevator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by dshue1 on 10/28/16.
 */
public class Elevator extends Thread {
	private final static Log logger = LogFactory.getLog(Elevator.class);

	private int number;
	private Status status = Status.idle;
	private int currentLevel = 1;

	@JsonIgnore
	private int totalLevels = 0;

	private Set<Integer> pendingRequests = new HashSet<>();

	public Elevator(int number, int totalLevels) {
		this.number = number;
		this.totalLevels = totalLevels;
	}

	public int getNumber() {
		return number;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getTotalLevels() {
		return totalLevels;
	}

	public synchronized void respond(int level) {
		logger.debug("elevator " + getNumber() + " got request going to " + level + " level");
		pendingRequests.add(level);
		notify();
	}

	@Override
	public void run()  {

		while(true) {
			if (pendingRequests.isEmpty()) {
				synchronized (this) {
					try {
						logger.debug(this);
						this.status = Status.idle;
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			int nextLevel = getNextStop();
			if (currentLevel > nextLevel) {
				this.status = Status.going_down;
			}
			else {
				this.status = Status.going_up;
			}
			this.currentLevel = nextLevel;
			logger.debug("elevator " + this.getNumber() + " " + this.getStatus() + " to " + nextLevel);

			pendingRequests.remove(this.currentLevel);
			// Spend 2000 ms stopping at a level
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.debug("elevator " + this.getNumber() + " " + this.getStatus() + " reached " + nextLevel);
		}


	}

	/**
	 * the next stop is always the one that is closest to the current level, plus in the right direction
	 * @return
	 */
	private int getNextStop() {
		return getQueue().get(0);
	}

	private List<Integer> getQueue() {
		final List<Integer> priority = new ArrayList<>(pendingRequests);
		Collections.sort(priority, (o1, o2) -> {
			if (status.getDirection() == Direction.up) {
				if (o1 > currentLevel && o2 > currentLevel) {
					return o1 - o2;
				}
				if (o1 < currentLevel && o2 < currentLevel) {
					return o2 - o1;
				}
				if (o1 > currentLevel) return -1;
				if (o2 > currentLevel) return 1;
				return 0;
			}
			else {
				if (o1 < currentLevel && o2 < currentLevel) {
					return o2 - o1;
				}
				if (o1 > currentLevel && o2 > currentLevel) {
					return o1 - o2;
				}
				if (o1 > currentLevel) return 1;
				if (o2 > currentLevel) return -1;
				return 0;
			}
		});

		return priority;
	}


	/**
	 * the priority is decided following these orders: status, level
	 * An idle/same-direction elevator on the same level you are requesting has the highest priority. Tiebreak goes to idle elevators
	 * An elevator that is moving towards you is higher, a moving elevator that has just moved past you has the lower priority
	 *
	 *
	 * @param callRequest
	 * @return priority score in integer
	 */
	public int getPriority(CallRequest callRequest) {
		int diff = callRequest.getCurrentLevel() - this.currentLevel;

		if (diff >=0) {
			diff = totalLevels - diff;
		}
		else {
			diff = -totalLevels - diff;
		}

		int score = 0;
		if (this.status.getDirection() != null) {
			// if going up, and my level is below this elevator's level, my priority for this elevator will be negative
			// Same for going down, and my level is above this elevator's level, it will hurt the priority big time
			// On the other hand, it going up and I happen to be right above the elevator, my priority will be super high
			int factor = this.status.getDirection().getFactor() * callRequest.getDirection().getFactor();
			if (factor == 1) {
				// same direction
				score += callRequest.getDirection().getFactor() * diff;
				if (score < 0) score += totalLevels + totalLevels;
				score *= 100;
			}
			else {
				score -= Math.abs(diff) * 100;
			}
		}
		else{
			score += 100 * Math.abs(diff) - 10;
		}


		return score;
	}

	public int getRequestValue() {
		if (status.getDirection() == null) {
			return currentLevel;
		}
		return currentLevel * status.getDirection().getFactor();
	}

	@Override
	public String toString() {
		return "elevator " + getNumber() + " " + getStatus() + " -> level " + getCurrentLevel() + " " + getQueue();
	}
}
