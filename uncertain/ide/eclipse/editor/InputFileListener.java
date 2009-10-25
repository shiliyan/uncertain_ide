package uncertain.ide.eclipse.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.EditorPart;

public class InputFileListener implements IResourceChangeListener, IResourceDeltaVisitor {
	private EditorPart editor;

	public InputFileListener(EditorPart editor) {
		this.editor = editor;
	}
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType()==IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			try {
				delta.accept(this);
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			IFile file = (IFile)resource;
			if (file.equals(((IFileEditorInput)editor.getEditorInput()).getFile())) {
				if (delta.getKind()==IResourceDelta.REMOVED ||
						delta.getKind()==IResourceDelta.REPLACED)
					closeEditor();
				return false;
			}
		}
		return true;
	}
	
	private void closeEditor() {
		Display display = editor.getSite().getShell().getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				editor.getSite().getPage().closeEditor(editor, false);
			}
		});
	}

}
