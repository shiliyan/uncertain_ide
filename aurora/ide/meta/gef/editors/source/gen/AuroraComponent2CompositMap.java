package aurora.ide.meta.gef.editors.source.gen;

import uncertain.composite.CompositeMap;
import aurora.ide.meta.gef.editors.models.AuroraComponent;
import aurora.ide.meta.gef.editors.models.BOX;
import aurora.ide.meta.gef.editors.models.Button;
import aurora.ide.meta.gef.editors.models.CheckBox;
import aurora.ide.meta.gef.editors.models.Dataset;
import aurora.ide.meta.gef.editors.models.FieldSet;
import aurora.ide.meta.gef.editors.models.Form;
import aurora.ide.meta.gef.editors.models.Grid;
import aurora.ide.meta.gef.editors.models.GridColumn;
import aurora.ide.meta.gef.editors.models.HBox;
import aurora.ide.meta.gef.editors.models.Input;
import aurora.ide.meta.gef.editors.models.QueryDataSet;
import aurora.ide.meta.gef.editors.models.ResultDataSet;
import aurora.ide.meta.gef.editors.models.TabFolder;
import aurora.ide.meta.gef.editors.models.TabItem;
import aurora.ide.meta.gef.editors.models.Toolbar;
import aurora.ide.meta.gef.editors.models.VBox;
import aurora.ide.meta.gef.editors.models.ViewDiagram;

public class AuroraComponent2CompositMap {
	public static final String SCREEN_PREFIX = "a";

	public CompositeMap createScreenCompositeMap() {
		CompositeMap screen = new CompositeMap("screen");
		screen.setNameSpace(SCREEN_PREFIX,
				"http://www.aurora-framework.org/application");
		return screen;
	}

	public CompositeMap createChild(String name) {
		CompositeMap node = new CompositeMap(name);
		node.setPrefix(SCREEN_PREFIX);
		return node;
	}

	public CompositeMap toCompositMap(AuroraComponent c) {
		if (c instanceof Input) {
			return new InputMap((Input)c).toCompositMap();
		}
		if (c instanceof Button) {
			return new ButtonMap(c).toCompositMap();
		}
		if (c instanceof BOX) {
			return new BoxMap(c).toCompositMap();
		}
		if (c instanceof CheckBox) {
			return new CheckBoxMap(c).toCompositMap();
		}
		if (c instanceof Grid) {
			return new GridMap(c).toCompositMap();
		}
		if (c instanceof GridColumn) {
			return new GridColumnMap(c).toCompositMap();
		}
		if (c instanceof Dataset) {
			return new DatasetMap(c).toCompositMap();
		}

		if (c instanceof Toolbar) {
			return this.createChild("toolBar");
		}
		if (c instanceof TabItem) {
			return new TabItemMap(c).toCompositMap();
		}
		if (c instanceof TabFolder) {
			return new TabFolderMap(c).toCompositMap();
		}
		if (c instanceof ViewDiagram) {
			return this.createChild("view");
		}

		return null;
	}
}
