package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;


public class PropertyActions {
	IViewerDirty dirtyObject;
	private IPropertyCategory mCategoryObject;
	public PropertyActions(IViewerDirty dirtyObject,IPropertyCategory categoryObject){
		this.dirtyObject = dirtyObject;
		mCategoryObject = categoryObject;
	}
	
	/**
	 * �Զ��巽������������Action���󣬲�ͨ��������������ToolBarManager����������
	 */
	public void fillActionToolBars(ToolBarManager actionBarManager) {
		// ���ɰ�ť����ť����һ������Action
		Action addAction = new AddPropertyAction(dirtyObject);

		Action removeAction = new RemovePropertyAction(dirtyObject);
		Action refreshAction = new RefreshAction(dirtyObject);

		CategroyAction categroyAction = new CategroyAction(mCategoryObject);
		CharSortAction charSortAction = new CharSortAction(mCategoryObject);
		/*
		 * ����ťͨ��������������ToolBarManager����������,�����add(action)
		 * Ҳ�ǿ��Եģ�ֻ����ֻ������û��ͼ��Ҫ��ʾͼ����Ҫ��Action��װ��
		 * ActionContributionItem�����������ǽ���װ�Ĵ������д����һ��������
		 * 
		 */
		actionBarManager.add(createActionContributionItem(removeAction));
		actionBarManager.add(createActionContributionItem(refreshAction));
		actionBarManager.add(createActionContributionItem(addAction));
		actionBarManager.add(createActionContributionItem(categroyAction));
		actionBarManager.add(createActionContributionItem(charSortAction));

		// ���¹�������û����һ�䣬�������ϻ�û���κ���ʾ
		actionBarManager.update(true);
	}
	ActionContributionItem createActionContributionItem(IAction action) {
		ActionContributionItem aci = new ActionContributionItem(action);
		aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);// ��ʾͼ��+����
		return aci;
	}
}
