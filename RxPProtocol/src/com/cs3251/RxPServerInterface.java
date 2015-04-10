package com.cs3251;

public interface RxPServerInterface {

		public void startServer(); // must bind to odd socket number
		public void setWindow(int size);
		public void terminateServer();
		public void setTimeout(int timeout);
		//use connection code to offer state via api??
		
		
}
