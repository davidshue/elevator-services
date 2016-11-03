package com.challenge.elevator;

/**
 * Created by dshue1 on 10/29/16.
 */
public class CallRequestThread extends Thread {
	private CallSystem callSystem;
	private CallRequest callRequest;

	public CallRequestThread(CallSystem callSystem, CallRequest callRequest) {
		this.callSystem = callSystem;
		this.callRequest = callRequest;
	}

	@Override
	public void run()  {
		callSystem.request(callRequest);
	}
}
