package aurora.ide.meta.gef.editors.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class AuroraComponent implements Cloneable, Serializable, IProperties {

	transient protected PropertyChangeSupport listeners = new PropertyChangeSupport(
			this);
	static final long serialVersionUID = 1;

	private Point location = new Point();

	private Dimension size = new Dimension();

	private Rectangle bounds = new Rectangle(location, size);

	private String name = "";

	private String type = "";

	private String prompt = "prompt : ";

	private String bindTarget = "";

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		listeners.firePropertyChange(prop, old, newValue);
	}

	protected void fireStructureChange(String prop, Object child) {
		listeners.firePropertyChange(prop, null, child);
	}

	public Object getEditableValue() {
		return this;
	}

	public Object getPropertyValue(Object propName) {
		// TODO
		return null;
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		listeners = new PropertyChangeSupport(this);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	public void resetPropertyValue(Object propName) {
		// TODO
	}

	public void setPropertyValue(Object propName, Object val) {
		// TODO
	}

	public void setLocation(Point p) {
		if (eq(this.location, p)) {
			return;
		}
		Point old = this.location;
		this.location = p;
		this.bounds.setLocation(p);
		firePropertyChange(LOCATION, old, p);
	}

	public Point getLocation() {
		return location;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		if (eq(this.size, size)) {
			return;
		}
		Dimension old = this.size;
		this.size = size;
		this.bounds.setSize(size);
		firePropertyChange(SIZE, old, size);
	}

	public Rectangle getBounds() {
		return bounds.getCopy();
	}

	public void setBounds(Rectangle bounds) {
		if (eq(this.bounds, bounds)) {
			return;
		}
		Rectangle old = this.bounds;
		this.bounds = bounds;
		this.location = bounds.getLocation();
		this.size = bounds.getSize();
		firePropertyChange(BOUNDS, old, bounds);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (eq(this.name, name)) {
			return;
		}
		String old = this.name;
		this.name = name;
		firePropertyChange(NAME, old, name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (eq(this.type, type)) {
			return;
		}
		String old = this.type;
		this.type = type;
		firePropertyChange(TYPE, old, type);
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		if (eq(this.prompt, prompt)) {
			return;
		}
		String old = this.prompt;
		this.prompt = prompt;
		firePropertyChange(PROMPT, old, prompt);
	}

	public String getBindTarget() {
		return bindTarget;
	}

	public void setBindTarget(String bindTarget) {
		this.bindTarget = bindTarget;
	}

	protected boolean eq(Object o1, Object o2) {
		if (o1 == null)
			return o2 == null;
		return o1.equals(o2);
	}

}
