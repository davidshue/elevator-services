package com.challenge.elevator;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by dshue1 on 10/28/16.
 */
public class CallRequest {
	private Direction direction;

	private int currentLevel;

	public CallRequest(Direction direction, int currentLevel) {
		this.direction = direction;
		this.currentLevel = currentLevel;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * going down will have a negative value, going up will have a positive value. This is used for priority calculation later.
	 * @return
	 */
	public int getRequestValue() {
		return currentLevel * direction.getFactor();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
