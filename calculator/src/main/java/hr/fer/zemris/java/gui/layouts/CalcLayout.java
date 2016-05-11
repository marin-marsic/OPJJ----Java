package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * Layout designed to represent for a simple calculator. It has 31 accessible
 * positions for adding components: <br>
 * <br>
 * (1,1), (1,6), (1,7)<br>
 * (2,1), (2,2), (2,3), (2,4), (2,5), (2,6), (2,7)<br>
 * (3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7)<br>
 * (4,1), (4,2), (4,3), (4,4), (4,5), (4,6), (4,7)<br>
 * (5,1), (5,2), (5,3), (5,4), (5,5), (5,6), (5,7)<br>
 * <br>
 * 
 * Format is similar to a {@link GridLayout}, the difference is that not all
 * components have the same size. Only the first component, the one on position
 * (1,1), has different size (because it is supposed to represent the
 * calculator's display, and the other components are representing buttons). The
 * components also define spacing between them. Default spacing is 0.
 * 
 * @author Marin Maršić
 *
 */
public class CalcLayout implements LayoutManager2 {
	int spacing;
	Component[][] components = new Component[5][7];

	/**
	 * Default constructor. Defines there is no spacing between components.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * ClacLayout constructor. Defines spacing between components.
	 * 
	 * @param spacing
	 *            Desirable spacing between components.
	 */
	public CalcLayout(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * Method for adding component whose position is given in the String.
	 * 
	 * @throws IllegalArgumentException
	 *             if the string failed to parse to RCPosition.
	 * @param component
	 *            Component to add.
	 * @param string
	 *            String position.
	 */
	private void addLayoutComponent(Component component, String string)
			throws IllegalArgumentException {
		String[] separated = string.trim().split(",");
		if (separated.length != 2) {
			throw new IllegalArgumentException("Two integer arguments needed.");
		}
		try {
			int row = Integer.parseInt(separated[0]);
			int column = Integer.parseInt(separated[1]);
			addLayoutComponent(component, new RCPosition(row, column));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Cannot parse to integer values.");
		}

	}

	public void layoutContainer(Container arg0) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				Insets insets = arg0.getInsets();
				Dimension dim = arg0.getSize();
				int w = (dim.width - insets.left - insets.right - 6 * spacing) / 7;
				int width = preferredComponentSize(arg0).width;
				width = Math.max(width, w);
				int h = (dim.height - insets.top - insets.bottom - 4 * spacing) / 5;
				int height = preferredComponentSize(arg0).height;
				height = Math.max(height, h);
				if (components[i][j] != null) {
					if (i == 0 && j == 0) {
						components[0][0].setBounds(0, 0, width * 5 + 4
								* spacing, height);
					} else {
						components[i][j].setBounds(j * (width + spacing), i
								* (height + spacing), width, height);
					}
				}
			}
			arg0.repaint();
		}

	}

	public Dimension minimumLayoutSize(Container arg0) {
		int width = 0;
		int height = 0;

		for (int i = 0; i < 5; i++) {
			int rowWidth = 0;
			for (int j = 0; j < 7; j++) {
				if (components[i][j] != null) {
					if (i == 0 && j == 0) {
						rowWidth += (int) 5
								* (preferredComponentSize(arg0).getWidth() + spacing);
					}
					else{
						rowWidth += (int) preferredComponentSize(arg0).getWidth()
								+ spacing;
					}
				}
			}
			rowWidth -= spacing;
			width = Math.max(width, rowWidth);
		}

		for (int i = 0; i < 7; i++) {
			int columnWidth = 0;
			for (int j = 0; j < 5; j++) {
				if (components[j][i] != null) {
					columnWidth += (int) preferredComponentSize(arg0)
							.getHeight() + spacing;
				}
			}
			columnWidth -= spacing;
			height = Math.max(height, columnWidth);
		}
		return new Dimension(width, height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		int height = (int) (5 * preferredComponentSize(parent).getHeight() + 4 * spacing);
		int width = (int) (7 * preferredComponentSize(parent).getWidth() + 6 * spacing);
		width += insets.left + insets.right;
		height += insets.top + insets.bottom;
		return new Dimension(width, height);
	}

	/**
	 * Method for calculating the size for all buttons to be set to.
	 * 
	 * @param arg0
	 *            Container.
	 * @return Returns preferred size of a component.
	 */
	private Dimension preferredComponentSize(Container arg0) {
		int width = 0;
		int height = 0;
		for (int i = 1; i < arg0.getComponentCount(); i++) {
			Dimension dim = arg0.getComponent(i).getPreferredSize();
			if (dim == null) {
				continue;
			}
			width = (int) Math.max(dim.getWidth(), width);
			height = (int) Math.max(dim.getHeight(), height);
		}
		return new Dimension(width, height);
	}

	public Dimension maximumLayoutSize(Container arg0) {
		int width = 0;
		int height = 0;

		for (int i = 0; i < 5; i++) {
			int rowWidth = 0;
			for (int j = 0; j < 7; j++) {
				if (components[i][j] != null
						&& components[i][j].getMaximumSize() != null) {
					rowWidth += (int) components[i][j].getMaximumSize()
							.getWidth() + spacing;
				}
			}
			rowWidth -= spacing;
			width = Math.max(width, rowWidth);
		}

		for (int i = 0; i < 7; i++) {
			int columnWidth = 0;
			for (int j = 0; j < 5; j++) {
				if (components[j][i] != null
						&& components[i][j].getMaximumSize() != null) {
					columnWidth += (int) components[i][j].getMaximumSize()
							.getHeight() + spacing;
				}
			}
			columnWidth -= spacing;
			height = Math.max(height, columnWidth);
		}
		return new Dimension(width, height);
	}

	public void addLayoutComponent(Component arg0, Object arg1) {
		if (arg1 instanceof RCPosition) {
			RCPosition rcp = (RCPosition) arg1;
			if (components[(rcp.getRow() - 1)][rcp.getColumn() - 1] == null) {
				components[(rcp.getRow() - 1)][rcp.getColumn() - 1] = arg0;
			} else {
				throw new IllegalArgumentException("Position already taken.");
			}
		} else if (arg1 instanceof String) {
			String rcp = (String) arg1;
			addLayoutComponent(arg0, rcp);
		}
	}

	public float getLayoutAlignmentX(Container arg0) {
		return 0;
	}

	public float getLayoutAlignmentY(Container arg0) {
		return 0;
	}

	public void invalidateLayout(Container arg0) {
		// DO NOTHING
	}

	public void addLayoutComponent(String name, Component comp) {
		// DO NOTHING
	}

	public void removeLayoutComponent(Component comp) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				if (components[i][j].equals(comp)) {
					components[i][j] = null;
				}
			}
		}
	}

}
