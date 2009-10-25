package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import aurora_ide.Activator;

/**
 * ˢ�µ�Action��
 */
public class RefreshAction extends Action {
	IViewerDirty mDirtyObject;
	public RefreshAction(IViewerDirty dirtyAction) {
		// ��Action����ͼ��getImageDescΪ�Զ��巽�����õ�һ��ͼ��
		// setHoverImageDescriptor(getImageDesc("refresh.gif"));
		setImageDescriptor(getImageDescriptor());
		setText("ˢ��");
		mDirtyObject = dirtyAction;
	}

	public void run() {
		mDirtyObject.refresh();// ���ñ���ˢ�·���
	}
	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/refresh.gif");
		return imageDescriptor;
	}
}
