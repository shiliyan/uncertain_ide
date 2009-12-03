package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import uncertain.composite.CompositeMap;
import uncertain.ide.eclipse.editor.ActionLabelManager;
import uncertain.schema.editor.AttributeValue;

public 	class RemovePropertyAction extends Action {
	
	IPropertyCategory viewer;
	public RemovePropertyAction(IPropertyCategory viewer) {
		// ��������µ�ͼ��
//		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
//		setText("ɾ������");
		this.viewer = viewer;
	}
	public RemovePropertyAction(IPropertyCategory viewer,ImageDescriptor imageDescriptor,String text) {
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
		CompositeMapAction.removePropertyAction(viewer);
	}
	public static ImageDescriptor getDefaultImageDescriptor(){
		return ActionLabelManager.getImageDescriptor(ActionLabelManager.DELETE);
	}
}
