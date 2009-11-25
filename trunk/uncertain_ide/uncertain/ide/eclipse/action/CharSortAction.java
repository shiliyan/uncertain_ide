package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import uncertain.ide.eclipse.editor.ActionLabelManager;

public class CharSortAction extends Action {
	
	private IPropertyCategory viewer;
	public CharSortAction(IPropertyCategory viewer) {
//		setText("A-Z����");
		this.viewer = viewer;
	}
	public CharSortAction(IPropertyCategory viewer,ImageDescriptor imageDescriptor,String text) {
		// ��������µ�ͼ��
		if(imageDescriptor != null)
			setHoverImageDescriptor(imageDescriptor);
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
		if(text != null)
			setText(text);
		this.viewer = viewer;
	}

	public void run() {
		viewer.setIsCategory(false);
		viewer.refresh(false);
	}
	public static ImageDescriptor getDefaultImageDescriptor(){
		return ActionLabelManager.getImageDescriptor(ActionLabelManager.ASC);
	}
}
