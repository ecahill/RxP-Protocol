package com.cs3251;

public interface RxPClientInterface {
	public void startClientServer();//must bind to even number socket
	public void connectToServer();
	public void get();
	public void post();
	public void setWindow(int windowSize);
	public void disconnect();
	public void setTimeout(int timeout);
}
