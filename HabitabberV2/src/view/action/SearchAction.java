package view.action;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import view.HabitabberGUI;
import view.field.CheckboxField;
import view.field.CookieField;
import view.field.PapaField;
import view.field.ParseImmediateField;
import view.field.RegExField;
import controller.MainSearcher;

public class SearchAction extends PapaAction {

	//	private static List<Thread> threadPool = new ArrayList<Thread>();
	private static List<Task<Void>> taskPool = new ArrayList<Task<Void>>();
	private Task<Void> task;
	private Service<Void> service;

	public SearchAction(HabitabberGUI gui) {
		super(gui);
	}

	@Override
	public void performAction() {
		List<String> argList = gui.getArgumentList();
		
		gui.startProgressIndicator();
		
		task = new SearchTask(argList.toArray(new String[argList.size()]));		
		
		taskPool.add(task);
		setService(new SearchService());
		getService().setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent t) {
				System.out.println("Done.");
			}
		});
		service.start();		
	}

	public class SearchTask extends Task<Void> {

		String[] argv;
		
		public SearchTask(String[] argv) {
			this.argv = argv;
		}
		@Override
		protected Void call() throws Exception {
			MainSearcher.setStop(false);
			MainSearcher mainSearcher = MainSearcher.getSingleton(gui, argv);
			mainSearcher.setGui(gui);
			mainSearcher.activate(argv);
			return null;
		}

	}

	public class SearchService extends Service<Void> {

		@Override
		protected Task<Void> createTask() {
			return task;
		}

	}

	public static List<Task<Void>> getTaskPool() {
		return taskPool;
	}

	/**
	 * @return the service
	 */
	public Service<Void> getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Service<Void> service) {
		this.service = service;
	}


}
