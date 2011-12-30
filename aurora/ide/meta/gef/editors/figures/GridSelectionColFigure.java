package aurora.ide.meta.gef.editors.figures;

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import aurora.ide.meta.gef.editors.ImagesUtils;
import aurora.ide.meta.gef.editors.models.Grid;
import aurora.ide.meta.gef.editors.models.GridSelectionCol;

/**
 * 
 */
public class GridSelectionColFigure extends GridColumnFigure {

	private static final Image img_check = ImagesUtils
			.getImage("palette/checkbox_01.png");
	private static final Image img_radio = ImagesUtils
			.getImage("palette/radio_01.png");
	private static final Image img_border = ImagesUtils.getImage("grid_bg.gif");
	private GridSelectionCol model;

	public GridSelectionColFigure() {
		setLayoutManager(null);
		setBorder(null);
		setFocusTraversable(false);
	}

	@Override
	public void handleFocusGained(FocusEvent event) {
		super.handleFocusGained(event);
	}

	/**
	 * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		if (Grid.SELECT_NONE.equals(model.getSelectionMode()))
			return;
		Rectangle rect = getBounds().getCopy();
		g.setBackgroundColor(ColorConstants.GRID_ROW);
		g.fillRectangle(rect);
		Rectangle columnHeaderRect = new Rectangle(rect.x, rect.y, 25,
				getColumnHight());
		Rectangle imgRect = new Rectangle(img_border.getBounds());
		g.drawImage(img_border, imgRect.setHeight(getColumnHight()),
				columnHeaderRect);

		Image img = img_radio;
		imgRect = new Rectangle(img.getBounds());
		if (Grid.SELECT_MULTI.equals(model.getSelectionMode())) {
			img = img_check;
			imgRect = new Rectangle(img.getBounds());
			g.drawImage(img, rect.x + (rect.width - imgRect.width) / 2, rect.y
					+ (getColumnHight() - imgRect.height) / 2);
		}

		g.setForegroundColor(ColorConstants.GRID_COLUMN_GRAY);
		for (int i = rect.y + getColumnHight(); i < rect.y + rect.height; i += 25) {
			Rectangle rc = new Rectangle(rect.x, i, rect.width, 25);
			g.drawLine(rc.getTopLeft(), rc.getTopRight());
			g.drawImage(img, imgRect, rc.getShrinked(
					(rc.width - imgRect.width) / 2,
					(rc.height - imgRect.height) / 2));
		}
	}

	public void setModel(GridSelectionCol component) {
		this.model = component;

	}

}
