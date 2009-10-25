package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import aurora_ide.Activator;

import uncertain.composite.CompositeMap;

public 	class AddPropertyAction extends Action {
	IViewerDirty mDirtyObject;
	public AddPropertyAction(IViewerDirty dirtyAction) {
		// ��������µ�ͼ��
		setHoverImageDescriptor(getImageDescriptor());
		// �ûң�removeAction.setEnabled(false)������µ�ͼ��
		// setDisabledImageDescriptor(getImageDesc("disremove.gif"));
		setText("�������");
		this.mDirtyObject = dirtyAction;
	}

	/**
	 * ������ʾ����δӱ����ɾ����ѡ�ļ�¼����ѡ�����
	 */
	public void run() {
//		final CompositeMap data = (CompositeMap) mDirtyObject.getObject().getInput();
		final CompositeMap data = (CompositeMap) mDirtyObject.getFocusData();
		System.out.println(data.toXML());
		final Shell shell = new Shell();
		shell.setSize(400, 200);

		Label propertyLabe = new Label(shell, SWT.NONE);
		propertyLabe.setText("������:");
		propertyLabe.setBounds(20, 20, 50, 30);

		final Text propertyText = new Text(shell, SWT.SHADOW_IN);
		propertyText.setBounds(80, 20, 300, 20);

		Label valueLabel = new Label(shell, SWT.NONE);
		valueLabel.setText("ֵ:");
		valueLabel.setBounds(20, 50, 50, 30);

		final Text valueText = new Text(shell, SWT.SHADOW_IN);
		valueText.setBounds(80, 50, 300, 20);

		final Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setBounds(220, 120, 70, 25);

		final Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setBounds(300, 120, 70, 25);
		SelectionListener listener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				Widget w = e.widget;
				if (w == ok) {
					System.out.println("ok........");
					System.out.println(propertyText.getText() + ":"
							+ valueText.getText());
					data.put(propertyText.getText(), valueText.getText());
					System.out.println(data.toXML());
					mDirtyObject.setDirty(true);
					mDirtyObject.refresh();
					shell.dispose();
				} else if (w == cancel) {
					shell.dispose();
				}

			}
		};
		ok.addSelectionListener(listener);
		cancel.addSelectionListener(listener);
		shell.open();

		mDirtyObject.refresh();
	}
	public ImageDescriptor getImageDescriptor(){
		ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/add_obj.gif");
		return imageDescriptor;
	}
}

