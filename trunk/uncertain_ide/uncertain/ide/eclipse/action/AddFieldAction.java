package uncertain.ide.eclipse.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import uncertain.composite.CompositeMap;
import uncertain.ide.Activator;
import uncertain.ide.LocaleMessage;
import uncertain.ide.eclipse.editor.IViewer;
import uncertain.ide.eclipse.editor.widgets.ListElementsExchangeDialog;

public class AddFieldAction extends Action {
	private IViewer viewer;

	private CompositeMap source;
	private CompositeMap target;

	private String localName;
	private String prefix;
	private String uri;
	private final static String primary_key = "primary-key";
	private final static String order_by = "order-by";
	private final static String primaryKeyChild = "pk-field";
	private final static String orderField = "order-field";
	private final static String defaultChild = "field";
	public AddFieldAction(IViewer viewer,CompositeMap source,CompositeMap target) {
		this.viewer = viewer;
		this.source = source;
		this.target = target;
		setHoverImageDescriptor(getDefaultImageDescriptor());
	}
	public void run() {
		if(primary_key.equals(target.getName())){
			localName = primaryKeyChild;
		}else if(order_by.equals(target.getName())){
			localName = orderField;
		}else{
			localName = defaultChild;
		}
		String sourceMainAttribute = "name";
		String targetMainAttribute = "name";
		List source_childs_list = source.getChildsNotNull();
		List  source_array = new ArrayList();
		
		List target_childs_list = target.getChildsNotNull();
		List  target_array = new ArrayList();

		for(Iterator it = target_childs_list.iterator();it.hasNext();){
			CompositeMap child = (CompositeMap)it.next();
			String targetNode = child.getString(targetMainAttribute);
			if(targetNode == null)
				continue;
			target_array.add(targetNode);
			if(uri==null){
				uri = child.getNamespaceURI();
				prefix = child.getPrefix();
			}
			
		}
		for(Iterator it = source_childs_list.iterator();it.hasNext();){
			CompositeMap child = (CompositeMap)it.next();
			String node = child.getString(sourceMainAttribute);
			if(node == null)
				continue;
			if(!target_array.contains(node))
				source_array.add(node);
		}
		
		
		String[] source_items = new String[source_array.size()];
		for(int i=0;i<source_array.size();i++){
			source_items[i] = (String)source_array.get(i);
		}
		
		String[] target_items =  new String[target_array.size()];
		for(int i=0;i<target_array.size();i++){
			target_items[i] = (String)target_array.get(i);
		}
		
		ListElementsExchangeDialog dialog = new ListElementsExchangeDialog(LocaleMessage.getString("get.fields"),source.getName(),
				target.getName(),source_items,target_items);
		if(dialog.open()==ListElementsExchangeDialog.CANCEL)
			return;
		String[] result = dialog.getRightItems();
		target.getChilds().clear();
		for(int i=0;i<result.length;i++){
			CompositeMap newNode = CompositeMapAction.addElement(target, prefix, uri, localName);
			newNode.put(targetMainAttribute, result[i]);
		}
		if (viewer != null) {
			viewer.refresh(true);
		}
	}

	public static ImageDescriptor getDefaultImageDescriptor() {
		return Activator.getImageDescriptor(LocaleMessage.getString("add.icon"));
	}
}
