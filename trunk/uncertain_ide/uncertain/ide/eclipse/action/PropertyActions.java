package uncertain.ide.eclipse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;


public class PropertyActions {
	private IPropertyCategory mCategoryObject;
	public PropertyActions(IPropertyCategory categoryObject){
		mCategoryObject = categoryObject;
	}
	
	/**
	 * �Զ��巽������������Action���󣬲�ͨ��������������ToolBarManager����������
	 */
	public void fillActionToolBars(ToolBarManager actionBarManager) {
		// ���ɰ�ť����ť����һ������Action
		Action addAction = new AddPropertyAction(mCategoryObject,AddPropertyAction.getDefaultImageDescriptor(),null);

		Action removeAction = new RemovePropertyAction(mCategoryObject,RemovePropertyAction.getDefaultImageDescriptor(),null);
		Action refreshAction = new RefreshAction(mCategoryObject,RefreshAction.getDefaultImageDescriptor(),null);

		CategroyAction categroyAction = new CategroyAction(mCategoryObject,CategroyAction.getDefaultImageDescriptor(),null);
		CharSortAction charSortAction = new CharSortAction(mCategoryObject,CharSortAction.getDefaultImageDescriptor(),null);
		/*
		 * ����ťͨ��������������ToolBarManager����������,�����add(action)
		 * Ҳ�ǿ��Եģ�ֻ����ֻ������û��ͼ��Ҫ��ʾͼ����Ҫ��Action��װ��
		 * ActionContributionItem�����������ǽ���װ�Ĵ������д����һ��������
		 * 
		 */
		actionBarManager.add(createActionContributionItem(addAction));
		actionBarManager.add(createActionContributionItem(refreshAction));
		actionBarManager.add(createActionContributionItem(removeAction));
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
