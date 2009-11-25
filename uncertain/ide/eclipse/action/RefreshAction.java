package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import uncertain.ide.eclipse.editor.ActionLabelManager;

/**
 * ˢ�µ�Action��
 */
public class RefreshAction extends Action {
	IViewer viewer;
	public RefreshAction(IViewer viewer) {
		// ��Action����ͼ��getImageDescΪ�Զ��巽�����õ�һ��ͼ��
		// setHoverImageDescriptor(getImageDesc("refresh.gif"));
//		setImageDescriptor(getImageDescriptor());
//		setText("ˢ��");
		this.viewer = viewer;
	}
	public RefreshAction(IViewer viewer,ImageDescriptor imageDescriptor,String text) {
		// ��Action����ͼ��getImageDescΪ�Զ��巽�����õ�һ��ͼ��
		// setHoverImageDescriptor(getImageDesc("refresh.gif"));
		if(imageDescriptor != null)
			setImageDescriptor(imageDescriptor);
		if(text != null)
			setText(text);
		this.viewer = viewer;
	}

	public void run() {
		viewer.refresh(false);// ���ñ���ˢ�·���
	}

	public static ImageDescriptor getDefaultImageDescriptor(){
		return ActionLabelManager.getImageDescriptor(ActionLabelManager.REFRESH);
	}
}
