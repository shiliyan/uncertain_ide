package editor.textpage.hyperlinks;

import helpers.DialogUtil;
import ide.AuroraPlugin;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;


public class ScreenFileHyperlink implements IHyperlink {
	private IRegion region;
	private ITextViewer viewer;

	public ScreenFileHyperlink(IRegion region, ITextViewer viewer) {
		this.region = region;
		this.viewer = viewer;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getHyperlinkText() {
		return null;
	}

	public String getTypeLabel() {
		return null;
	}

	public void open() {

		IFile currentFile = AuroraPlugin.getActiveIFile();
		String parentFullPath = currentFile.getParent().getLocation()
				.toOSString();
		IDocument doc = viewer.getDocument();
		try {
			String path = doc.get(region.getOffset(), region.getLength());
			char ch = File.separatorChar;
			String fullPath = parentFullPath + ch + path;
			IWorkbenchPage page = AuroraPlugin.getActivePage();
			IProject project = currentFile.getProject();
			String fullFile = (new File(fullPath)).getAbsolutePath();
			String projectFile = (new File(project.getLocation().toOSString()))
					.getAbsolutePath();
			if (fullFile.indexOf(projectFile) == -1) {
				return;
			}
			String filePath = fullFile.substring(projectFile.length());
			IFile java_file = project.getFile(filePath);
			IDE.openEditor(page, java_file);
		} catch (Exception e) {
			DialogUtil.showExceptionMessageBox(e);
		}
	}
}
