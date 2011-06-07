package editor;

import helpers.ApplicationException;
import helpers.AuroraResourceUtil;
import helpers.DialogUtil;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.xml.sax.SAXException;

import uncertain.composite.CompositeLoader;
import uncertain.composite.CompositeMap;
import uncertain.composite.XMLOutputter;

public class CompositeMapTreePage extends CompositeMapPage {

	private CompositeMap data;

	protected BaseCompositeMapViewer baseCompositeMapPage;

	Composite shell;

	public CompositeMapTreePage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		shell = form.getBody();
		FillLayout layout = new FillLayout();
		shell.setLayout(layout);
		try {
			CompositeLoader loader = AuroraResourceUtil.getCompsiteLoader();
			data = loader.loadByFile(getFile().getAbsolutePath());

		} catch (IOException e) {
			DialogUtil.showExceptionMessageBox(e);
		} catch (SAXException e) {
			String emptyExcption = "Premature end of file";
			if (e.getMessage() != null && e.getMessage().indexOf(emptyExcption) != -1) {
				data = ScreenUtil.createScreenTopNode();
				data.setComment("本文件为空,现在内容为系统自动创建,请修改并保存");
			} else {
				DialogUtil.showExceptionMessageBox(e);
				return;
			}
		}
		try {
			createContent(shell);
		} catch (ApplicationException e) {
			DialogUtil.showExceptionMessageBox(e);
		}
	}

	protected File getFile() {
		IFile ifile = ((IFileEditorInput) getEditor().getEditorInput()).getFile();
		String fileName = AuroraResourceUtil.getIfileLocalPath(ifile);
		return new File(fileName);
	}

	protected void createContent(Composite shell) throws ApplicationException {
		baseCompositeMapPage = new BaseCompositeMapViewer(this, data);
		baseCompositeMapPage.createFormContent(shell);
	}

	public void doSave(IProgressMonitor monitor) {
		try {
			File file = getFile();
			XMLOutputter.saveToFile(file, data);
			super.doSave(monitor);
		} catch (Exception e) {
			DialogUtil.showExceptionMessageBox(e);
		}
	}

	public void refresh(boolean dirty) {
		if (dirty) {
			getEditor().editorDirtyStateChanged();
		}
		baseCompositeMapPage.refresh(false);
	}

	public void refresh(CompositeMap data) {
		this.data = data;
		baseCompositeMapPage.refresh(data);
	}

	public CompositeMap getData() {
		return data;
	}

	public void setData(CompositeMap data) {
		this.data = data;
	}
	public TreeViewer getTreeViewer() {
		return baseCompositeMapPage.getTreeViewer();
	}
	public CompositeMap getSelection() {
		return baseCompositeMapPage.getSelection();
	}
	public CompositeMap getContent() {
		return data;

	}

	public String getFullContent() {
		String encoding = "UTF-8";
		String xml_decl = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>\n";
		return xml_decl + XMLOutputter.defaultInstance().toXML(data, true);
	}

	public void setContent(CompositeMap content) {
		this.data = content;
		baseCompositeMapPage.refresh(data);
	}
}