package view.action;

import result.Page;
import result.SourceElement;
import view.HabitabberGUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupAction extends PapaAction {
	
	Page page;
	String name;
	
	public PopupAction(HabitabberGUI gui, String name, Page page) {
		super(gui);
		this.page = page;
		this.name = name;
	}
	
	@Override
	public void performAction() {
		
		final Stage dialogStage = new Stage();
		dialogStage.getIcons().add(HabitabberGUI.HABITABBER_ICON);
		dialogStage.setTitle("Details");
		
		dialogStage.initModality(Modality.WINDOW_MODAL);
		
        HTMLEditor editor = new HTMLEditor();
        String html = "<html><body>";
        if (page.getContent() != null) {
	        	for (Object o : page.getContent().get("media")) {
	        		String str = o.toString();
	        		if (!str.isEmpty())
	        			html += "<img src=\"" + getRealUrl(str) + "\">";
	        	}
        } else {
        		html += "<p>No media available.</p>";
        }
        
        if (page.getSource() != null) {
        	for (String srcKey : page.getSource().keySet()) {
        		html += srcKey + ":";
        		for (SourceElement srcVal : page.getSource().get(srcKey)) {
        			String title = srcVal.getTitle();
        			if (title != null && !title.isEmpty()) {
        				html += "<b>" + title + "</b>";
        				html += "<br>";
        			}
        			html += "<img src=\"" + getRealUrl(srcVal.getUrl()) 
        			+ ((srcVal.getAlt() != null && !srcVal.getAlt().isEmpty()) ? "\" alt=\"" 
        			+ srcVal.getAlt() : "") + ((srcVal.getTitle() != null && !srcVal.getTitle().isEmpty()) ? "\" title=\"" + srcVal.getTitle() : "") 
        			+ "\">";
        			html += "<br><br>";
        			
        		}
        	}
        } else {
        	html += "<p>No source available.</p>";
        }
        
        if (page.getDescription() != null) {
        	html += page.getDescription().text();
        	System.out.println("description: " + page.getDescription().text());
        }
        html += "</body></html>";
        editor.setHtmlText(html);
       
        
        Scene myDialogScene = new Scene(VBoxBuilder.create()
                .children(editor)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());
      
        dialogStage.setScene(myDialogScene);
		
        dialogStage.show();	
	}
	
	private String getRealUrl(String str) {
		if (str.startsWith("//")) {
			String url = page.getUrl();
			if (url != null && !url.isEmpty())
				str = url.substring(0, url.indexOf("//") + 2) + str.substring(2);
		}
		return str;
	}
}
