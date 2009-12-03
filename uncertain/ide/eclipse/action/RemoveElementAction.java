package uncertain.ide.eclipse.action;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import uncertain.ide.eclipse.editor.ActionLabelManager;

public 	class RemoveElementAction extends Action {
	IViewerDirty viewer;
	public RemoveElementAction(IViewerDirty viewer) {
		// ��������µ�ͼ��
//		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
//		setText("ɾ���ӽڵ�");
		this.viewer = viewer;
	}
	public RemoveElementAction(IViewerDirty viewer,ImageDescriptor imageDescriptor,String text) {
		if(imageDescriptor != null)
			setImageDescriptor(imageDescriptor);
		if(text != null)
			setText(text);
		this.viewer = viewer;
	}

	/**
	 * ������ʾ����δӱ����ɾ����ѡ�ļ�¼����ѡ�����
	 */
	public void run() {
		CompositeMapAction.removeElement(viewer);

	}
	public static ImageDescriptor getDefaultImageDescriptor(){
		return ActionLabelManager.getImageDescriptor(ActionLabelManager.DELETE);
	}
	public static String getDefaultText(){
		return ActionLabelManager.getText(ActionLabelManager.DELETE);
	}
}
