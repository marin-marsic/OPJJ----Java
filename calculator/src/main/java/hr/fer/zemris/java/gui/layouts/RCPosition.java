package hr.fer.zemris.java.gui.layouts;

/**
 * Class representing a position of a specific Component in CalcLayout layout.
 * It has 31 accessible positions: <br>
 * <br>
 * (1,1), (1,6), (1,7)<br>
 * (2,1), (2,2), (2,3), (2,4), (2,5), (2,6), (2,7)<br>
 * (3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7)<br>
 * (4,1), (4,2), (4,3), (4,4), (4,5), (4,6), (4,7)<br>
 * (5,1), (5,2), (5,3), (5,4), (5,5), (5,6), (5,7)<br>
 * <br>
 * 
 * Creating instances of RCPosition with any other combination will result in
 * throwing exception. Position is a read-only object, which means it cannot be
 * altered.
 * 
 * @author Marin Maršić
 *
 */
public class RCPosition {

	private int row;
	private int column;

	/**
	 * Constructor for creating RCP instance with specific column and row
	 * indexes. In case of inaccessible position,
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param row
	 *            Row index.
	 * @param column
	 *            Column index.
	 * @throws IllegalArgumentException
	 *             if the position is inaccessible.
	 */
	public RCPosition(int row, int column) throws IllegalArgumentException {
		if (row <= 0 || row > 5) {
			throw new IllegalArgumentException("Illegal row index: " + row);
		}
		if (column <= 0 || column > 7) {
			throw new IllegalArgumentException("Illegal column index: "
					+ column);
		}
		if (row == 1
				&& (column == 2 || column == 3 || column == 4 || column == 5)) {
			throw new IllegalArgumentException("Unaccessible index: " + row
					+ "," + column);
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * @return Returns row index.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return Returns column index.
	 */
	public int getColumn() {
		return column;
	}

}
