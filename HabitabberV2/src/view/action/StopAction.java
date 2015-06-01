package view.action;

import view.HabitabberGUI;
import controller.MainSearcher;

public class StopAction extends PapaAction {
	
	public StopAction(HabitabberGUI gui) {
		super(gui);
	}

	@Override
	public void performAction() {		
		new Thread(new StopThread()).start();;
//		MainSearcher.getSingleton(null).terminate();
	}
	
	public class StopThread implements Runnable {

		@Override
		public void run() {
			MainSearcher singleton = MainSearcher.getSingleton(gui, null);
			singleton.terminate();
		}	
	}
}
