package view.action;

import java.util.ArrayList;
import java.util.List;

import view.field.PapaField;
import view.field.ParseImmediateField;
import controller.MainSearcher;

public class SearchAction extends PapaAction {

	private static List<Thread> threadPool = new ArrayList<Thread>();

	public SearchAction() {
		super();
	}

	@Override
	public void performAction() {
		List<String> argList = new ArrayList<String>();
		for (PapaField field : PapaField.getFieldList()) {
			if (field instanceof ParseImmediateField){  
				if (((ParseImmediateField)field).getCheckbox().isSelected()) {
					argList.add(field.getName());
					argList.add("v");
				}
			} else {
				if (field.getTextField() != null) {				
					String value = field.getTextField().getText();
					if (value != null) { 
						argList.add(field.getName()); // The command
						for (String v : value.split(" ")) // The value(s)
							argList.add(v);
					}
				} 
			}
		}
		Thread t = new Thread(new SearchThread(argList.toArray(new String[argList.size()])));
		threadPool.add(t);
		t.start();
	}

	public class SearchThread implements Runnable {

		String[] argArr;

		public SearchThread(String[] argArr) {
			this.argArr = argArr;
		}

		@Override
		public void run() {
			MainSearcher.setStop(false);
			MainSearcher mainSearcher = MainSearcher.getSingleton(argArr);
			mainSearcher.activate(argArr);
			threadPool.remove(this);
		}	
	}

	public static List<Thread> getThreadPool() {
		return threadPool;
	}
}
