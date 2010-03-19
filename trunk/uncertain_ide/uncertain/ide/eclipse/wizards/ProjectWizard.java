package uncertain.ide.eclipse.wizards;

import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class ProjectWizard extends BasicNewProjectResourceWizard {

	public static final String copyright = "(c) Copyright HAND Enterprise Solutions Company Ltd.";

	ProjectWizardPage fMainPage = new ProjectWizardPage();

	public void addPages() {
		super.addPages();
		// addPage(fMainPage);
	}

	public boolean performFinish() {
		if (!super.performFinish())
			return false;
		initProject();
		return true;
	}

	private void initProject() {
		try {
			
			//TODO ��ʼ����������ʱ��ȱ
//			IFile file = getNewProject().getFile(Common.NewProjectFile);
//			String fileFullPath = Common.getIfileLocalPath(file);
//			File root = new File(fileFullPath);
//			if (!root.exists()) {
//				try {
//					root.createNewFile();
//				} catch (IOException e) {
//					 throw new RuntimeException(e.getMessage());
//				}
//			}
//			// file.create(null, true, null);
//			Activator.openFileInEditor(file, Common.ServiceEditor);
//			getNewProject().refreshLocal(IResource.DEPTH_ONE, null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
