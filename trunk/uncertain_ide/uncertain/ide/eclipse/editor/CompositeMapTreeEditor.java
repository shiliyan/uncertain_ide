package uncertain.ide.eclipse.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.StructuredSelection;

import uncertain.composite.CompositeMap;
import uncertain.ide.eclipse.action.CompositeMapLocatorParser;
import uncertain.ide.eclipse.editor.widgets.CustomDialog;


public abstract class CompositeMapTreeEditor extends BaseCompositeMapEditor {

	
	protected CompositeMapTreePage treePage;
	
	public CompositeMapTreeEditor() {
		super();
	}
	
	public CompositeMapPage initMainViewerPage(){
		this.treePage = initTreePage();
		return treePage;
	}
	public abstract CompositeMapTreePage initTreePage();
	
	protected void pageChange(int newPageIndex){
		int currentPage = getCurrentPage();
		super.pageChange(newPageIndex);
		if(currentPage==mainViewerIndex&&newPageIndex ==textPageIndex){
			locateTextPage();
		}else if(currentPage==textPageIndex&&newPageIndex ==mainViewerIndex){
			locateTreePage();
		}
	}
	private void locateTreePage(){
		CompositeMapLocatorParser parser = new CompositeMapLocatorParser();

		try {
			InputStream content = new ByteArrayInputStream(textPage.getContent()
					.getBytes("UTF-8"));
			CompositeMap  cm = parser.getCompositeMapFromLine(content, textPage.getCursorLine());
			if(cm != null){
//				treePage.getTreeViewer().expandToLevel(
//						cm, 0);
				treePage.getTreeViewer().setSelection(
						new StructuredSelection(cm), true);
			}
		} catch (Exception e){
			CustomDialog.showExceptionMessageBox(e);
		}

	}
	private void locateTextPage(){
		CompositeMap selection = treePage.getSelection();
		if(selection == null)
			return;
		CompositeMapLocatorParser parser = new CompositeMapLocatorParser();

		int line = 0;
		try {
			InputStream content = new ByteArrayInputStream(treePage.getFullContent()
					.getBytes("UTF-8"));
			line = parser.LocateCompositeMapLine(content, selection);
		} catch (Exception e) {
			CustomDialog.showExceptionMessageBox(e);
		}
		int offset = textPage.getOffsetFromLine(line);
		textPage.setHighlightRange(offset, 10, true);
	}
}