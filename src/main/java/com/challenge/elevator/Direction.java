package com.challenge.elevator;

/**
 * Created by dshue1 on 10/28/16.
 */
public enum Direction {
	up(1),
	down(-1);

	private int factor = 1;

	Direction(int factor) {
		this.factor = factor;
	}

	public int getFactor() {
		return factor;
	}
}
