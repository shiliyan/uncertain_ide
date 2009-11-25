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
		Shell shell = new Shell();
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING
				| SWT.OK | SWT.CANCEL);
		messageBox.setText("Warning");
		messageBox.setMessage("ȷ��ɾ����������?");
		int buttonID = messageBox.open();
		switch (buttonID) {
		case SWT.OK:
//			final CompositeMap data = (CompositeMap) viewer.getObject()
//					.getInput();
//			System.out.println(data.toXML());
//			ISelection selection = viewer.getObject().getSelection();
//			Object obj = ((IStructuredSelection) selection)
//					.getFirstElement();
//			AttributeValue av = (AttributeValue) obj;
			final CompositeMap data = viewer.getInput();

			AttributeValue av = viewer.getFocusData();
			String propertyName = av.getAttribute().getLocalName();
			System.out.println(propertyName);
			data.remove(propertyName);
			viewer.refresh(true);
		case SWT.CANCEL:
			break;
		}

	}
	public static ImageDescriptor getDefaultImageDescriptor(){
		return ActionLabelManager.getImageDescriptor(ActionLabelManager.DELETE);
	}
}
