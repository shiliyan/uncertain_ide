package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.graphics.Image;

import aurora_ide.Activator;

import uncertain.composite.CompositeMap;

public 	class AddElementAction extends Action {
	private IViewerDirty viewerDirty;

	CompositeMap parentCM;
	String prefix;
	String uri;
	String name;

	public AddElementAction(IViewerDirty mDirtyObject, CompositeMap parentCM, String _prefix,
			String _uri, String _name) {
		this.viewerDirty = mDirtyObject;
		this.parentCM = parentCM;
		this.prefix = _prefix;
		this.uri = _uri;
		this.name = _name;
		// ��������µ�ͼ��
		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
		setText(_name);
	}

	/**
	 * ������ʾ����δӱ����ɾ����ѡ�ļ�¼����ѡ�����
	 */
	public void run() {
		CompositeMapAction.addElement(parentCM, prefix, uri, name);
		if (viewerDirty != null) {
			viewerDirty.setDirty(true);
			viewerDirty.refresh();
		}
	}
	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/element_obj.gif");
		return imageDescriptor;
	}
}
