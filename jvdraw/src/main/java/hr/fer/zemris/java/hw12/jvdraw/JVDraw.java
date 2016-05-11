package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Program for rendering simple pictures.
 * 
 * @author Marin Maršić
 *
 */
public class JVDraw extends JFrame {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Background color chooser.
	 */
	private JColorArea background = new JColorArea();
	/**
	 * Foreground color chooser.
	 */
	private JColorArea foreground = new JColorArea(Color.BLACK);
	/**
	 * Button group.
	 */
	private ButtonGroup buttonGroup;
	/**
	 * {@link ObjectsModel}.
	 */
	private ObjectsModel om = new ObjectsModel();
	/**
	 * Canvas for drawing.
	 */
	private JDrawingCanvas canvas;
	/**
	 * List of drawn objects.
	 */
	private DrawingObjectListModel listModel;
	/**
	 * Path to opened file.
	 */
	private Path openedFilePath;

	/**
	 * Constructor for the {@link JVDraw}.
	 */
	public JVDraw() {
		setLocation(20, 50);
		setSize(800, 700);
		setTitle("JVDraw");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	/**
	 * Method for initializing the GUI.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		background.setName("background");
		foreground.setName("foreground");

		buttonGroup = new ButtonGroup();
		JToggleButton button1 = new JToggleButton("Line");
		JToggleButton button2 = new JToggleButton("Circle");
		JToggleButton button3 = new JToggleButton("Filled circle");

		buttonGroup.add(button1);
		buttonGroup.add(button2);
		buttonGroup.add(button3);

		createMenu();
		createToolbars();
		createCanvas();
	}

	/**
	 * Method for creating the menu.
	 */
	private void createMenu() {
		openAction.putValue(Action.NAME, "Open");
		saveAction.putValue(Action.NAME, "Save");
		saveAsAction.putValue(Action.NAME, "Save as");
		exportAction.putValue(Action.NAME, "Export");

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openAction));
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.add(new JMenuItem(exportAction));

		this.setJMenuBar(menuBar);

	}

	/**
	 * Method for creating the toolbar.
	 */
	private void createToolbars() {
		JToolBar toolbar = new JToolBar("Tools");
		toolbar.setFloatable(true);

		toolbar.add(foreground);
		toolbar.add(new JLabel(" "));
		toolbar.add(background);

		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons
				.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			button.addActionListener(objectTypeListener);

			toolbar.add(button);
		}

		getContentPane().add(toolbar, BorderLayout.NORTH);
	}

	/**
	 * Method for creating the canvas and the list of drawn objects.
	 */
	private void createCanvas() {
		SelectedColorsLabel label = new SelectedColorsLabel();
		background.addColorChangeListener(label);
		foreground.addColorChangeListener(label);
		add(label, BorderLayout.SOUTH);
		canvas = new JDrawingCanvas(om, "Line");
		om.addDrawingModelListener(canvas);
		background.addColorChangeListener(canvas);
		foreground.addColorChangeListener(canvas);

		getContentPane().add(canvas, BorderLayout.CENTER);

		listModel = new DrawingObjectListModel(om);
		JList<String> list = new JList<>(listModel);
		JScrollPane scrollPane = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setSize(scrollPane.getMinimumSize());
		scrollPane.setPreferredSize(new Dimension(80, 0));
		om.addDrawingModelListener(listModel);
		getContentPane().add(scrollPane, BorderLayout.EAST);

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {

					// Double-click detected
					int index = list.locationToIndex(evt.getPoint());
					listModel.change(index);
					canvas.objectsChanged(om, index, index);
				}
			}
		});
	}

	/**
	 * Action to perform when user wants to open new file.
	 */
	private Action openAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			if (fc.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();
			if (!Files.isReadable(file)) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Selected file is not readable.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!file.toString().endsWith(".jvd")) {
				JOptionPane.showMessageDialog(JVDraw.this, "Invalid format.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				List<String> lines = Files.readAllLines(file,
						StandardCharsets.UTF_8);
				om.removeAllObjects();
				readObjects(lines);
				openedFilePath = file;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, e1.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		/**
		 * Read objects from file and add them to objects model.
		 * 
		 * @param lines
		 *            Lines from file.
		 * @throws IOException
		 */
		private void readObjects(List<String> lines) throws IOException {
			for (String s : lines) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}
				String[] lineSeparated = s.split("\\s+");
				if (lineSeparated[0].equals("LINE")) {
					readLine(lineSeparated);
				} else if (lineSeparated[0].equals("CIRCLE")) {
					readCircle(lineSeparated);
				} else if (lineSeparated[0].equals("FCIRCLE")) {
					readFilledCircle(lineSeparated);
				}
			}
		}

		/**
		 * Read filled circle from line.
		 * 
		 * @param lineSeparated
		 *            Array representing separated line arguments.
		 * @throws IOException
		 */
		private void readFilledCircle(String[] lineSeparated)
				throws IOException {
			if (lineSeparated.length != 10) {
				throw new IOException("Invalid format for line.");
			}

			try {
				int x = Integer.parseInt(lineSeparated[1]);
				int y = Integer.parseInt(lineSeparated[2]);
				int radius = Integer.parseInt(lineSeparated[3]);
				int r1 = Integer.parseInt(lineSeparated[4]);
				int g1 = Integer.parseInt(lineSeparated[5]);
				int b1 = Integer.parseInt(lineSeparated[6]);
				int r2 = Integer.parseInt(lineSeparated[7]);
				int g2 = Integer.parseInt(lineSeparated[8]);
				int b2 = Integer.parseInt(lineSeparated[9]);
				om.add(new FilledCircle(x, y, radius, new Color(r1, g1, b1),
						new Color(r2, g2, b2)));
			} catch (NumberFormatException e) {
				throw new IOException("Invalid format for line.");
			}
		}

		/**
		 * Read circle from line.
		 * 
		 * @param lineSeparated
		 *            Array representing separated line arguments.
		 * @throws IOException
		 */
		private void readCircle(String[] lineSeparated) throws IOException {
			if (lineSeparated.length != 7) {
				throw new IOException("Invalid format for line.");
			}

			try {
				int x = Integer.parseInt(lineSeparated[1]);
				int y = Integer.parseInt(lineSeparated[2]);
				int radius = Integer.parseInt(lineSeparated[3]);
				int r = Integer.parseInt(lineSeparated[4]);
				int g = Integer.parseInt(lineSeparated[5]);
				int b = Integer.parseInt(lineSeparated[6]);
				om.add(new Circle(x, y, radius, new Color(r, g, b)));
			} catch (NumberFormatException e) {
				throw new IOException("Invalid format for line.");
			}
		}

		/**
		 * Read line object from textual line.
		 * 
		 * @param lineSeparated
		 *            Array representing separated line arguments.
		 * @throws IOException
		 */
		private void readLine(String[] lineSeparated) throws IOException {
			if (lineSeparated.length != 8) {
				throw new IOException("Invalid format for line.");
			}

			try {
				int x1 = Integer.parseInt(lineSeparated[1]);
				int y1 = Integer.parseInt(lineSeparated[2]);
				int x2 = Integer.parseInt(lineSeparated[3]);
				int y2 = Integer.parseInt(lineSeparated[4]);
				int r = Integer.parseInt(lineSeparated[5]);
				int g = Integer.parseInt(lineSeparated[6]);
				int b = Integer.parseInt(lineSeparated[7]);
				om.add(new Line(x1, y1, x2, y2, new Color(r, g, b)));
			} catch (NumberFormatException e) {
				throw new IOException("Invalid format for line.");
			}
		}
	};

	/**
	 * Action to perform when user wants to save file.
	 */
	private Action saveAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (openedFilePath == null) {
				saveAsAction.actionPerformed(e);
				return;
			}

			StringBuilder strb = new StringBuilder();
			for (GeometricalObject go : om.getObjects()) {
				strb.append(go.getStringRepresentation() + "\n");
			}
			try {
				Files.write(openedFilePath,
						strb.toString().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Error while saving file.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	};

	/**
	 * Action to perform when user wants to save the file as some other.
	 */
	private Action saveAsAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save document");
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();

			if (Files.exists(file)) {
				int rez = JOptionPane.showConfirmDialog(JVDraw.this,
						"Document already exists! Do you want to replace it?",
						"Warning", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (rez != JOptionPane.YES_OPTION) {
					return;
				}
			}

			StringBuilder strb = new StringBuilder();
			for (GeometricalObject go : om.getObjects()) {
				strb.append(go.getStringRepresentation() + "\n");
			}
			try {
				Files.write(file,
						strb.toString().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Error while saving file.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	};

	/**
	 * Action to perform when user wants to export the picture.
	 */
	private Action exportAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			FileNameExtensionFilter filterJPG = new FileNameExtensionFilter(
					"*.jpg", "jpg");
			fc.setFileFilter(filterJPG);
			FileNameExtensionFilter filterGIF = new FileNameExtensionFilter(
					"*.gif", "gif");
			fc.setFileFilter(filterJPG);
			FileNameExtensionFilter filterPNG = new FileNameExtensionFilter(
					"*.png", "png");
			fc.setFileFilter(filterJPG);
			fc.setFileFilter(filterGIF);
			fc.setFileFilter(filterPNG);
			fc.setDialogTitle("Export");
			if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path path = fc.getSelectedFile().toPath();
			String extension = ((FileNameExtensionFilter) fc.getFileFilter())
					.getExtensions()[0];

			int width;
			int height;
			int xOffset;
			int yOffset;
			try {
				width = om.maximumX() - om.minimumX();
				height = om.maximumY() - om.minimumY();
				xOffset = -om.minimumX();
				yOffset = -om.minimumY();
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Image empty.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			BufferedImage bim = new BufferedImage(width, height,
					BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = bim.createGraphics();

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bim.getWidth(), bim.getHeight());

			for (GeometricalObject go : om.getObjects()) {
				go.paintOnImage(g, xOffset, yOffset);
			}

			g.dispose();

			File file = new File(path.toString() + "." + extension);
			try {
				ImageIO.write(bim, extension, file);

			} catch (Exception e1) {
				JOptionPane.showMessageDialog(JVDraw.this,
						"Error while exporting file.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

		}
	};

	/**
	 * Listener of the buttons representing type of object to draw.
	 */
	private ActionListener objectTypeListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Enumeration<AbstractButton> buttons = buttonGroup
					.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();

				if (button.isSelected()) {
					System.out.println(button.getText());
					canvas.setGeometricalType(button.getText());
				}
			}
		}
	};

	/**
	 * Method which executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not needed here.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JVDraw();
			frame.setVisible(true);
		});
	}

}
