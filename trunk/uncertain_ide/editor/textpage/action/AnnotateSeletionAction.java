package editor.textpage.action;

import helpers.DialogUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import editor.textpage.TextPage;
import editor.textpage.scanners.XMLPartitionScanner;


public class AnnotateSeletionAction implements IEditorActionDelegate {

	IEditorPart activeEditor;
	ISelection selection;
	public AnnotateSeletionAction() {
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		activeEditor = targetEditor;
	}

	public void run(IAction action) {
		if (activeEditor == null || !(activeEditor instanceof TextPage)) {
			DialogUtil.showErrorMessageBox("这个类不是" + TextPage.class.getName());
			return;
		}
		TextPage tp = (TextPage) activeEditor;
		IDocument document = tp.getInputDocument();
		if (!(selection instanceof ITextSelection)) {
			return;
		}
		ITextSelection ts  = (ITextSelection) selection;
		String content = ts.getText();
		if("".equals(content))
			return ;
		try {
			ITypedRegion  partitionRegion = document.getPartition(ts.getOffset());
			if(XMLPartitionScanner.XML_CDATA.equals(partitionRegion.getType())){
				content ="/*"+content+"*/";
			}else if( XMLPartitionScanner.XML_START_TAG.equals(partitionRegion.getType())|| IDocument.DEFAULT_CONTENT_TYPE.equals(partitionRegion.getType())){
				content ="<!--"+content+"-->";
			}
			document.replace(ts.getOffset(), ts.getLength(), content);
		} catch (BadLocationException e) {
			DialogUtil.showExceptionMessageBox(e);
			return ;
		}

		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
