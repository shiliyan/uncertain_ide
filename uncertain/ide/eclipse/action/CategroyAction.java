package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import aurora_ide.Activator;

import uncertain.ide.eclipse.editor.service.ServicePropertyEditor;

public class CategroyAction extends Action {
	
	private IPropertyCategory mDirtyAction;
	public CategroyAction(IPropertyCategory dirtyObject) {
		// ��������µ�ͼ��
		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
		setText("������ʾ");
		mDirtyAction = dirtyObject;
	}

	public void run() {
		mDirtyAction.setIsCategory(true);
		mDirtyAction.refresh();
	}
	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/category.gif");
		return imageDescriptor;
	}
}
