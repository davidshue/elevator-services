package com.challenge.elevator;

/**
 * Created by dshue1 on 10/28/16.
 */
public enum Status {
	going_up(Direction.up),
	going_down(Direction.down),
	idle;

	private Direction direction = null;

	Status(Direction direction) {
		this.direction = direction;
	}

	Status() {}

	public Direction getDirection() {
		return direction;
	}

	public static Status getByDirection(Direction direction) {
		switch(direction) {
			case up:
				return going_up;
			case down:
				return going_down;
			default:
				return idle;
		}
	}
}
