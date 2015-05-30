package view.action;

import result.Page;
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
	
	public PopupAction(String name, Page page) {
		this.page = page;
		this.name = name;
	}
	
	@Override
	public void performAction() {
		
		final Stage myDialog = new Stage();
        myDialog.initModality(Modality.WINDOW_MODAL);
		
        HTMLEditor editor = new HTMLEditor();
        String html = "<html><body>";
        for (Object o : page.getContent().get("media")) {
        	if (!o.toString().isEmpty()) {
        		html += "<img src=\"" + o.toString() + "\">";
        	}
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
      
        myDialog.setScene(myDialogScene);
		
		myDialog.show();	
	}
}
