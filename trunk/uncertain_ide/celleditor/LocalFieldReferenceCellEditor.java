package celleditor;

import helpers.ApplicationException;
import helpers.DialogUtil;

import java.util.Iterator;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import editor.widgets.GridViewer;
import editor.widgets.core.IGridViewer;

import bm.editor.GridDialog;

import uncertain.composite.CompositeMap;

public class LocalFieldReferenceCellEditor extends StringTextCellEditor {

	public LocalFieldReferenceCellEditor(CellInfo cellProperties) {
		super(cellProperties);
	}


	protected void addCellListener() {
		getCellControl().addMouseListener(new MouseListener() {
			
			public void mouseUp(MouseEvent e) {		
			}
			
			public void mouseDown(MouseEvent e) {
				try {
					fireEvent();
				} catch (ApplicationException ex) {
					DialogUtil.showExceptionMessageBox(ex);
				}
			}
			
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		getCellControl().addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if(isTableItemEditor())
					fillTableCellEditor(cellProperties.getTableItem());				
			}
			public void focusGained(FocusEvent e) {
				
			}
		});
	}
	private void fireEvent() throws ApplicationException{
		CompositeMap root= getData();
		if(root == null)
			return ;
		CompositeMap fields = root.getChild("fields");
		CompositeMap filedNames = new CompositeMap();
		for(Iterator it = fields.getChildsNotNull().iterator();it.hasNext();){
			CompositeMap child = (CompositeMap)it.next();
			String targetNode = child.getString("name");
			if(targetNode == null)
				continue;
			CompositeMap newChild = new CompositeMap();
			newChild.put("name", targetNode);
			filedNames.addChild(newChild);

		}
		String[] columnProperties = {"name"};
		GridViewer grid = new GridViewer(IGridViewer.filterBar|IGridViewer.NoToolBar);
		grid.setData(filedNames);
		grid.setFilterColumn("name");
		grid.setColumnNames(columnProperties);
		GridDialog dialog = new GridDialog(new Shell(),grid);
		if (dialog.open() == Window.OK&&dialog.getSelected() != null) {
			String value = dialog.getSelected().getString("name");
			setValue(value);
			if(isTableItemEditor()){
				cellProperties.getRecord().put(cellProperties.getColumnName(), value);
			}else{
				TableItem item =cellProperties.getTableViewer().getViewer().getTable().getSelection()[0];
				CompositeMap data = (CompositeMap)item.getData();
				data.put(cellProperties, value);
//				Object oldValue = data.get(property);
				
			}
			cellProperties.getTableViewer().refresh(true);
		}
	}
	private CompositeMap getData(){
		if(cellProperties.getTableItem() != null){
			return cellProperties.getRecord().getRoot();
		}
		TableViewer tableViewer = cellProperties.getTableViewer().getViewer();
		if(tableViewer == null)
			return null;
		Object dataObject = tableViewer.getInput();
		if( dataObject == null)
			return null;
		return (CompositeMap)dataObject;
		
	}
	private boolean isTableItemEditor(){
		return cellProperties.getTableItem() != null;
	}
	
}
