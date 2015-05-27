package view.action;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import view.field.PapaField;
import view.field.ParseImmediateField;
import view.field.RegExField;
import controller.MainSearcher;

public class SearchAction extends PapaAction {

	//	private static List<Thread> threadPool = new ArrayList<Thread>();
	private static List<Task<Void>> taskPool = new ArrayList<Task<Void>>();
	private Task<Void> task;
	private Service<Void> service;

	public SearchAction() {
		super();
	}

	@Override
	public void performAction() {
		String regExLine = null;
		List<String> argList = new ArrayList<String>();
		for (PapaField field : PapaField.getFieldMap().values()) {
			if (field instanceof ParseImmediateField){  
				if (((ParseImmediateField)field).getCheckbox().isSelected()) {
					argList.add(field.getName());
					argList.add("v");
				}
			} else if (!(field instanceof RegExField)) {
				if (field.getTextField() != null) {				
					String value = field.getTextField().getText();
					if (value != null) { 
						argList.add(field.getName()); // The command
						for (String v : value.split(" ")) // The value(s)
							argList.add(v);
					}
				} 
			} else { // RegEx
				if (field.getTextField() != null) {
					String value = field.getTextField().getText();
					if (value != null && !value.isEmpty()) {
						if (regExLine == null) {
							regExLine = "-r";
							argList.add(regExLine); 
						}
						argList.add(value);
						regExLine += value;
					}
				}
			}
		}
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
			MainSearcher mainSearcher = MainSearcher.getSingleton(argv);
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
