package view.action;

import controller.MainSearcher;

public class StopAction extends PapaAction {
	@Override
	public void performAction() {		
		new Thread(new StopThread()).start();;
	}
	
	public class StopThread implements Runnable {

		@Override
		public void run() {
			MainSearcher singleton = MainSearcher.getSingleton(null);
			singleton.terminate();
		}	
	}
}
