package aurora.ide.editor.textpage;


import java.io.File;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.MarkerRulerAction;

import uncertain.composite.CompositeMap;
import uncertain.composite.XMLOutputter;
import aurora.ide.editor.core.IViewer;
import aurora.ide.editor.textpage.js.validate.JavascriptDocumentListener;
import aurora.ide.helpers.ApplicationException;
import aurora.ide.helpers.AuroraResourceUtil;
import aurora.ide.helpers.CompositeMapUtil;
import aurora.ide.helpers.LocaleMessage;

public class TextPage extends TextEditor implements IViewer {
	/** The ID of this editor as defined in plugin.xml */
	public static final String EDITOR_ID = "aurora.ide.editor.textpage";
	/** The ID of the editor context menu */
	public static final String EDITOR_CONTEXT = EDITOR_ID + ".context";
	/** The ID of the editor ruler context menu */
	public static final String RULER_CONTEXT = EDITOR_CONTEXT + ".ruler";

	protected static final String textPageId = "textPage";
	public static final String textPageTitle = LocaleMessage.getString("source.file");
	private boolean syc = false;
	private ColorManager colorManager;
	private FormEditor editor;
	private boolean modify = false;
	private boolean ignorceSycOnce = false;
	private IAnnotationModel annotationModel;

	public TextPage() {
		super();
	}

	protected void initializeEditor() {
		super.initializeEditor();
		setEditorContextMenuId(EDITOR_CONTEXT);
		setRulerContextMenuId(RULER_CONTEXT);
	}

	// add by shiliyan
	public Object getAdapter(Class adapter) {
		if (Display.getCurrent() != null && IAnnotationModel.class.equals(adapter)) {
			return this.getAnnotationModel();
		}
		return super.getAdapter(adapter);
	}

	// add by shiliyan
	public boolean isIgnorceSycOnce() {
		return ignorceSycOnce;
	}
	private IAnnotationModel getAnnotationModel() {
		if (annotationModel != null)
			return annotationModel;
		annotationModel = getDocumentProvider().getAnnotationModel(getInput());
		if (annotationModel == null) {
			annotationModel = new AnnotationModel();
			annotationModel.connect(getInputDocument());
		}
		return annotationModel;
	}

	public void setIgnorceSycOnce(boolean ignorceSycOnce) {
		this.ignorceSycOnce = ignorceSycOnce;
	}

	public TextPage(FormEditor editor, String id, String title) {
		// super(editor, id, title);
		this.editor = editor;
		setPartName(title);
		setContentDescription(title);
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}

	public TextPage(FormEditor editor) {
		this(editor, textPageId, textPageTitle);
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// add by shiliyan

		getInputDocument().addDocumentListener(new JavascriptDocumentListener(this));

		// add by shiliyan
		getInputDocument().addDocumentListener(new IDocumentListener() {
			public void documentChanged(DocumentEvent event) {
				if (syc) {
					syc = false;
					return;
				}
				refresh(true);
			}
			public void documentAboutToBeChanged(DocumentEvent event) {

			}
		});
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
		ProjectionSupport projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

	}

	public void refresh(boolean dirty) {
		if (dirty) {
			getEditor().editorDirtyStateChanged();
			setModify(true);
		}
	}

	private FormEditor getEditor() {
		return editor;
	}

	public void refresh(String newContent) {
		if (!newContent.equals(getSourceViewer().getTextWidget().getText())) {
			syc = true;
			getSourceViewer().getTextWidget().setText(newContent);
		}
	}

	public void setSyc(boolean isSyc) {
		syc = isSyc;
	}

	public String getContent() {
		return getSourceViewer().getTextWidget().getText();
	}

	public boolean canLeaveThePage() {
		return checkContentFormat();
	}

	public boolean checkContentFormat() {
		try {
			toCompoisteMap();
		} catch (ApplicationException e) {
			return false;
		}
		return true;
	}
	public CompositeMap toCompoisteMap() throws ApplicationException{
		return CompositeMapUtil.loaderFromString(getContent());
	}

	public int getCursorLine() {
		return getSourceViewer().getTextWidget().getLineAtOffset(getSourceViewer().getSelectedRange().x);
	}

	public Point getSelectedRange() {
		return getSourceViewer().getSelectedRange();
	}

	public IFile getFile() {
		IFile ifile = ((IFileEditorInput) getEditor().getEditorInput()).getFile();
		return ifile;
	}

	public int getOffsetFromLine(int lineNumber) {
		int offset = 0;
		if (lineNumber < 0)
			return offset;
		try {
			offset = getInputDocument().getLineOffset(lineNumber);
			if (offset >= getInputDocument().getLength())
				return getOffsetFromLine(lineNumber - 1);
		} catch (BadLocationException e) {
			return getOffsetFromLine(lineNumber - 1);
		}
		return offset;
	}

	public int getLineOfOffset(int offset) {
		try {
			return getInputDocument().getLineOfOffset(offset);
		} catch (BadLocationException e) {
			return -1;
		}
	}

	public int getLengthOfLine(int lineNumber) {
		int length = 0;
		if (lineNumber < 0)
			return length;
		try {
			length = getInputDocument().getLineLength(lineNumber);
		} catch (BadLocationException e) {
			try {
				length = getInputDocument().getLineLength(lineNumber - 1);
			} catch (BadLocationException e1) {
			}
		}
		return length;
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	public IDocument getInputDocument() {
		IDocument document = getDocumentProvider().getDocument(getInput());
		return document;
	}

	public IEditorInput getInput() {
		return getEditorInput();
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse
	 * .swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler,
	 * int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	protected void createActions() {
		super.createActions();
		/**
		 * 只可手工添加，通过plugin.xml配置无效。 因为textEditor作为MultiPageEditorPart时,
		 * 点击左侧垂直条，AbstractTextEditor
		 * .findContributedAction()中getSite().getId()总是为"",判断失效。
		 * */
		Action action = new MarkerRulerAction(ResourceBundle
				.getBundle("org.eclipse.ui.texteditor.ConstructedTextEditorMessages"), "Editor.ManageBookmarks.", this,
				getVerticalRuler(), IMarker.BOOKMARK, true);
		setAction(ITextEditorActionConstants.RULER_DOUBLE_CLICK, action);
	}
	public void doSave(IProgressMonitor monitor){
		try {
			IFile ifile = ((IFileEditorInput) getEditorInput()).getFile();
			File file = new File(AuroraResourceUtil.getIfileLocalPath(ifile));
			XMLOutputter.saveToFile(file,CompositeMapUtil.loaderFromString(getContent()));//parseString(getContent()) );//
			ifile.refreshLocal(IResource.DEPTH_ZERO, null);
//			super.doSave(monitor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}