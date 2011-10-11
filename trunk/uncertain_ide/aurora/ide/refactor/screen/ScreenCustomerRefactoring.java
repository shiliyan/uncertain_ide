package aurora.ide.refactor.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextEditChangeGroup;
import org.eclipse.ltk.core.refactoring.TextFileChange;

import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEditGroup;

import uncertain.composite.CompositeMap;
import aurora.ide.search.core.AbstractMatch;
import aurora.ide.search.core.CompositeMapMatch;
import aurora.ide.search.screen.custom.ScreenCustomService;

public class ScreenCustomerRefactoring extends Refactoring {

	private IStructuredSelection selection;

	private List<IResource> scopes = new ArrayList<IResource>();

	
	private Map<IFile, TextFileChange> changeMap = new HashMap<IFile, TextFileChange>();

	public ScreenCustomerRefactoring(IStructuredSelection selection) {
		this.selection = selection;
		init(selection);
	}

	private void init(IStructuredSelection selection) {
		if (selection == null)
			return;
		Iterator iterator = selection.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof IResource) {
				if ("screen".equalsIgnoreCase(((IResource) next)
						.getFileExtension())
						|| ((IResource) next).getType() == IResource.FOLDER) {
					scopes.add((IResource) next);
				}
			}
		}
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		List<AbstractMatch> result = new ArrayList<AbstractMatch>();
		for (IResource scope : scopes) {
			ScreenCustomService service = new ScreenCustomService(scope);
			List _result = service.service(pm);
			result.addAll(_result);
		}
		CompositeChange changes = new CompositeChange("aurora changes");
		changes.markAsSynthetic();
		ScreenCustomerIDGen gen = new ScreenCustomerIDGen();
		for (int i = 0; i < result.size(); i++) {
			CompositeMapMatch object = (CompositeMapMatch) result.get(i);
			IFile file = (IFile) object.getElement();
			CompositeMap map = object.getMap();
			String id = gen.createID(file, map);
			TextFileChange textFileChange = getTextFileChange(file);
			int offset = object.getOriginalOffset();
			ReplaceEdit child = new ReplaceEdit(offset, 0, " " + id);
			textFileChange.addEdit(child);
		}
		changes.addAll(changeMap.values().toArray(
				new TextFileChange[changeMap.size()]));
		return changes;
	}

	private TextFileChange getTextFileChange(IFile file) {
		TextFileChange textFileChange = changeMap.get(file);
		if (textFileChange == null) {
			textFileChange = new TextFileChange("Screens Changes", file);
			textFileChange.setSaveMode(TextFileChange.FORCE_SAVE);
			textFileChange.setEdit(new MultiTextEdit());
			changeMap.put(file, textFileChange);
		}
		return textFileChange;
	}

	@Override
	public String getName() {
		return "screen custom";
	}

}
