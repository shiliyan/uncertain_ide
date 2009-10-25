package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import aurora_ide.Activator;

import uncertain.ide.eclipse.editor.service.ServicePropertyEditor;

public class CharSortAction extends Action {
	
	private IPropertyCategory mDirtyAction;
	public CharSortAction(IPropertyCategory dirtyObject) {
		// ��������µ�ͼ��
		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
		setText("A-Z����");
		mDirtyAction = dirtyObject;
	}

	/**
	 * ������ʾ����δӱ����ɾ����ѡ�ļ�¼����ѡ�����
	 */
	public void run() {
		mDirtyAction.setIsCategory(false);
		mDirtyAction.refresh();
	}
	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/asc.gif");
		return imageDescriptor;
	}
}
