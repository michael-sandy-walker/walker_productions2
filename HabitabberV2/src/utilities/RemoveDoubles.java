package utilities;

import java.util.TreeMap;

public class RemoveDoubles {

	public static TreeMap<String, String> treeMap = new TreeMap<String, String>();
	public static void main(String[] argv) {
		String str = "search_27052015.txt";
		if (argv != null && argv.length > 0) {
			str = argv[0];
		}

		IOSingleton.getIOSingleton().setReadFileName(str);
		IOSingleton.getIOSingleton().initializeReader();

		String line = null;
		String delimiter = " , Tel: ";
		while ((line = IOSingleton.getIOSingleton().readLine()) != null && !line.isEmpty()) {			
			if (line.contains(delimiter)) {
				String[] keyValArr = line.split(delimiter);
				treeMap.put(keyValArr[1], keyValArr[0]);			
			}
		}

		for (String key : treeMap.keySet()) {
			System.out.println("" + key + " ,Site: " + treeMap.get(key));
		}
	}
}
