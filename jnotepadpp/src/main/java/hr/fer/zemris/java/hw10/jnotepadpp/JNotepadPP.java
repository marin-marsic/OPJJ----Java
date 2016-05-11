package hr.fer.zemris.java.hw10.jnotepadpp;

import hr.fer.zemris.java.hw10.jnotepadpp.i18n.FormLocalizationProvider;
import hr.fer.zemris.java.hw10.jnotepadpp.i18n.LocalizableAction;
import hr.fer.zemris.java.hw10.jnotepadpp.i18n.LocalizationProvider;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Program representing simple text editor based on Notepad++.
 * 
 * @author Marin Maršić
 *
 */
public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	private ArrayList<Path> paths = new ArrayList<>();
	private ArrayList<Boolean> changed = new ArrayList<>();
	private ArrayList<JTextArea> editors = new ArrayList<>();
	private ImageIcon unsavedIcon = new ImageIcon(this.getClass().getResource("unsaved.png"));
	private ImageIcon savedIcon = new ImageIcon(this.getClass().getResource("saved.png"));
	private String textToPaste = "";
	private boolean toPaste = false;
	private boolean canceledClosing = false;
	private JLabel statsLabel = new JLabel();
	private JMenuItem invert = new JMenuItem();
	private JMenuItem toUpper = new JMenuItem();
	private JMenuItem toLower = new JMenuItem();
	private JMenuItem unique = new JMenuItem();
	private JMenu sortSubmenu = new JMenu();
	private JLabel timeLabel = new TimeComponent();
	private FormLocalizationProvider flp = new FormLocalizationProvider(
			LocalizationProvider.getInstance(), this);

	/**
	 * Default constructor for frame of this window which initializes all it's
	 * content.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("JNotepad++");
		setBounds(100, 100, 800, 500);
		addWindowListener(windowListener);
		intitGUI();
	}

	/**
	 * Initializes the GUI of the program. Creates all needed components and
	 * links them to actions.
	 */
	private void intitGUI() {
		getContentPane().setLayout(new BorderLayout());

		JTextArea editor = new JTextArea();
		editor.getDocument().addDocumentListener(textChangedListener);
		editor.addCaretListener(statusBarListener);
		editors.add(editor);
		paths.add(null);
		changed.add(false);

		JScrollPane pane = new JScrollPane(editor);

		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(tabChangedListener);
		tabbedPane.add("new doc", pane);
		tabbedPane.setIconAt(0, savedIcon);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		createActions();
		createMenus();
		createToolbars();
		createStatusBars();
	}

	/**
	 * adding actions to frame and setting them up.
	 */
	private void createActions() {
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Open existing documents.");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		newDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Open new document.");
		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Save document.");
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Save document as...");
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control alt S"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		closeDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				"Closes current tab.");
		closeDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control W"));
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);

		copyAction.putValue(Action.SHORT_DESCRIPTION, "Copies selected text.");
		copyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutAction.putValue(Action.SHORT_DESCRIPTION, "Cuts selected text.");
		cutAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		pasteAction.putValue(Action.SHORT_DESCRIPTION, "Pastes selected text.");
		pasteAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

		documentInfoAction.putValue(Action.SHORT_DESCRIPTION,
				"Showes some informations about current document.");
		documentInfoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control I"));
		documentInfoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
	}

	/**
	 * Creates menu containing all the needed actions.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(new LocalizableAction("file", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(documentInfoAction);

		JMenu editMenu = new JMenu(new LocalizableAction("edit", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));

		JMenu languagesMenu = new JMenu(
				new LocalizableAction("languages", flp) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
					}
				});
		menuBar.add(languagesMenu);

		languagesMenu.add(new JMenuItem(hrLanguage));
		languagesMenu.add(new JMenuItem(enLanguage));
		languagesMenu.add(new JMenuItem(deLanguage));

		JMenu toolsMenu = new JMenu(new LocalizableAction("tools", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(toolsMenu);

		invert = new JMenuItem(invertCaseAction);
		toUpper = new JMenuItem(toUpperCaseAction);
		toLower = new JMenuItem(toLowerCaseAction);
		toolsMenu.add(invert);
		toolsMenu.add(toUpper);
		toolsMenu.add(toLower);
		toolsMenu.addSeparator();
		sortSubmenu = new JMenu(new LocalizableAction("sort", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		toolsMenu.add(sortSubmenu);
		unique = new JMenuItem(uniqueAction);
		toolsMenu.add(uniqueAction);
		sortSubmenu.add(ascendnigSortAction);
		sortSubmenu.add(descendingSortAction);
		this.setJMenuBar(menuBar);
		
		unique.setEnabled(false);
		invert.setEnabled(false);
		toUpper.setEnabled(false);
		toLower.setEnabled(false);
		sortSubmenu.setEnabled(false);

	}

	/**
	 * Creates tool bar containing all the needed actions.
	 */
	private void createToolbars() {
		JToolBar toolbar = new JToolBar("Tools");
		toolbar.setFloatable(true);

		toolbar.add(new JButton(openDocumentAction));
		toolbar.add(new JButton(newDocumentAction));
		toolbar.add(new JButton(saveDocumentAction));
		toolbar.add(new JButton(saveAsDocumentAction));
		toolbar.add(new JButton(closeDocumentAction));
		toolbar.add(new JButton(documentInfoAction));
		toolbar.addSeparator();
		toolbar.add(new JButton(cutAction));
		toolbar.add(new JButton(copyAction));
		toolbar.add(new JButton(pasteAction));

		getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}

	/**
	 * Creates status bar showing some document info and current time.
	 */
	private void createStatusBars() {
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 2));
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

		statsLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statsLabel);

		timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		statusBar.add(timeLabel);

		getContentPane().add(statusBar, BorderLayout.PAGE_END);
	}

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to set language to Croatian.
	 */
	private Action hrLanguage = new LocalizableAction("hr", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
			repaint();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to set language to English.
	 */
	private Action enLanguage = new LocalizableAction("en", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
			repaint();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to set language to German.
	 */
	private Action deLanguage = new LocalizableAction("de", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
			repaint();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to invert cases in text.
	 */
	private Action invertCaseAction = new LocalizableAction("invert", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);
			Document doc = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				String text = doc.getText(start, length);
				text = toggleCase(text);
				doc.remove(start, length);
				doc.insertString(start, text, null);
			} catch (BadLocationException e1) {
			}
		}

		private String toggleCase(String text) {
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (Character.isUpperCase(c)) {
					chars[i] = Character.toLowerCase(c);
				} else if (Character.isLowerCase(c)) {
					chars[i] = Character.toUpperCase(c);
				}
			}
			return new String(chars);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to set all the cases to upper case.
	 */
	private Action toUpperCaseAction = new LocalizableAction("toUpper", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);
			Document doc = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				String text = doc.getText(start, length);
				text = toUpperCase(text);
				doc.remove(start, length);
				doc.insertString(start, text, null);
			} catch (BadLocationException e1) {
			}
		}

		private String toUpperCase(String text) {
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (Character.isLowerCase(c)) {
					chars[i] = Character.toUpperCase(c);
				}
			}
			return new String(chars);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to set all the cases to lower case.
	 */
	private Action toLowerCaseAction = new LocalizableAction("toLower", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);
			Document doc = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				String text = doc.getText(start, length);
				text = toLowerCase(text);
				doc.remove(start, length);
				doc.insertString(start, text, null);
			} catch (BadLocationException e1) {
			}
		}

		private String toLowerCase(String text) {
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (Character.isUpperCase(c)) {
					chars[i] = Character.toLowerCase(c);
				}
			}
			return new String(chars);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user selects
	 * another tab.
	 */
	private ChangeListener tabChangedListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			if (selected < 0) {
				return;
			}
			if (paths.size() - 1 >= selected && paths.get(selected) != null) {
				String path = paths.get(selected).toString();
				JNotepadPP.this.setTitle(path + " - JNotepad++");
			} else {
				String path = tabbedPane.getTitleAt(selected);
				JNotepadPP.this.setTitle(path + " - JNotepad++");
			}

			statusBarListener.caretUpdate(null);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when text in
	 * editor changes.
	 */
	private DocumentListener textChangedListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			changedUpdate(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			changed.set(selected, true);
			tabbedPane.setIconAt(selected, unsavedIcon);
			repaint();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to open another document in editor.
	 */
	private Action openDocumentAction = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open file");
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path file = fc.getSelectedFile().toPath();

			for (Path p : paths) {
				if (p != null && p.equals(file)) {
					JOptionPane.showMessageDialog(JNotepadPP.this,
							"This file is already opened.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
			if (!Files.isReadable(file)) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Cannot read this file: " + file, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			JTextArea editor = new JTextArea();
			JScrollPane pane = new JScrollPane(editor);

			try {
				byte[] bytes = Files.readAllBytes(file);
				editor.setText(new String(bytes, StandardCharsets.UTF_8));
				editor.getDocument().addDocumentListener(textChangedListener);
				editor.addCaretListener(statusBarListener);
				editors.add(editor);
				paths.add(file);
				changed.add(false);
				tabbedPane.add(file.getFileName().toString(), pane);
				tabbedPane.setIconAt(tabbedPane.getTabCount() - 1, savedIcon);
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
				repaint();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Error while reading file: " + file, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			tabbedPane.setToolTipTextAt(tabbedPane.getTabCount() - 1,
					file.toString());
			tabChangedListener.stateChanged(null);
			statusBarListener.caretUpdate(null);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to create new document in editor.
	 */
	private Action newDocumentAction = new LocalizableAction("new", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			JTextArea editor = new JTextArea();
			editor.getDocument().addDocumentListener(textChangedListener);
			editor.addCaretListener(statusBarListener);
			editors.add(editor);
			JScrollPane pane = new JScrollPane(editor);

			paths.add(null);
			changed.add(true);

			tabbedPane.add("new doc", pane);
			tabbedPane.setIconAt(tabbedPane.getTabCount() - 1, savedIcon);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			tabChangedListener.stateChanged(null);
			repaint();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to open save current progress of a specific tab.
	 */
	private Action saveDocumentAction = new LocalizableAction("save", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			Path file = paths.get(selected);
			JTextArea editor = editors.get(selected);

			if (file == null) {
				saveAsDocumentAction.actionPerformed(e);
				tabChangedListener.stateChanged(null);
				return;
			}

			tabbedPane.setTitleAt(selected, file.getFileName().toString());
			tabbedPane.setIconAt(selected, savedIcon);
			paths.set(selected, file);

			try {
				Files.write(file,
						editor.getText().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Error while saving file.", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			tabbedPane.setToolTipTextAt(selected, file.toString());
			tabChangedListener.stateChanged(null);
			changed.set(selected, false);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to open save selected document as a different one.
	 */
	private Action saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			Path file = paths.get(selected);
			JTextArea editor = editors.get(selected);

			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save document");
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				canceledClosing = true;
				return;
			}

			file = fc.getSelectedFile().toPath();

			if (Files.exists(file)) {
				int rez = JOptionPane.showConfirmDialog(JNotepadPP.this,
						"Document already exists! Do you want to replace it?",
						"Warning", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (rez != JOptionPane.YES_OPTION) {
					return;
				}
			}

			tabbedPane.setToolTipTextAt(selected, file.toString());
			tabbedPane.setTitleAt(selected, file.getFileName().toString());
			tabbedPane.setIconAt(selected, savedIcon);
			paths.set(selected, file);

			try {
				Files.write(file,
						editor.getText().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Error while saving file.", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			changed.set(selected, false);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to close current tab.
	 */
	private Action closeDocumentAction = new LocalizableAction("close", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			if (changed.get(selected)) {
				int result = JOptionPane
						.showConfirmDialog(
								JNotepadPP.this,
								"Document '"
										+ tabbedPane.getTitleAt(selected)
										+ "' has some unsaved changes! Do you want to save them?",
								"Warning", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (result == JOptionPane.CANCEL_OPTION) {
					return;
				}
				if (result != JOptionPane.YES_OPTION) {
					tabbedPane.remove(selected);
					paths.remove(selected);
					editors.remove(selected);
					changed.remove(selected);
					return;
				}

				saveDocumentAction.actionPerformed(e);
			}

			if (canceledClosing) {
				canceledClosing = false;
				return;
			}
			tabbedPane.remove(selected);
			paths.remove(selected);
			editors.remove(selected);
			changed.remove(selected);
			return;
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to cut selected text.
	 */
	private Action cutAction = new LocalizableAction("cut", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			Document document = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				textToPaste = document.getText(start, length);
				toPaste = true;
				document.remove(start, length);
				textChangedListener.changedUpdate(null);
			} catch (BadLocationException e1) {
			}
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to copy selected text.
	 */
	private Action copyAction = new LocalizableAction("copy", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			Document document = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				textToPaste = document.getText(start, length);
				toPaste = true;
			} catch (BadLocationException e1) {
			}
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to paste copied text.
	 */
	private Action pasteAction = new LocalizableAction("paste", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!toPaste) {
				return;
			}
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			Document document = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int length = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark())
					- start;

			try {
				document.remove(start, length);
				start = editor.getCaret().getDot();
				document.insertString(start, textToPaste, null);
				textChangedListener.changedUpdate(null);
			} catch (BadLocationException e1) {
			}
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to get informations about current document. It shows number of
	 * characters, number of characters without blank spaces and number of
	 * lines.
	 */
	private Action documentInfoAction = new LocalizableAction("info", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			String text = editor.getText();
			int symbols = text.length();
			int characters = 0;
			int lines = editor.getLineCount();
			for (int i = 0; i < text.length(); i++) {
				if (Character.isWhitespace((text.charAt(i)))) {
					characters++;
				}
			}

			StringBuilder strb = new StringBuilder();
			strb.append("Your document has " + symbols + " characters, ");
			strb.append(characters + " non-blank characters and ");
			strb.append(lines + " lines. ");
			String message = strb.toString();

			JOptionPane.showMessageDialog(JNotepadPP.this, message, "Info",
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to close the program. Program asks user if he wants to save unsaved
	 * documents if there are any.
	 */
	private WindowListener windowListener = new WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent e) {

			for (int i = 0; i < editors.size(); i++) {
				tabbedPane.setSelectedIndex(i);

				if (changed.get(i)) {
					int result = JOptionPane
							.showConfirmDialog(
									JNotepadPP.this,
									"Document '"
											+ tabbedPane.getTitleAt(i)
											+ "' has some unsaved changes! Do you want to save them?",
									"Warning",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.CANCEL_OPTION) {
						return;
					}
					if (result == JOptionPane.YES_OPTION) {
						saveDocumentAction.actionPerformed(null);
						if (canceledClosing) {
							return;
						}
					}
				}
			}
			((TimeComponent) timeLabel).setStopRequested(true);
			dispose();
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user changes
	 * caret's position. Status bar will be updated.
	 */
	private CaretListener statusBarListener = new CaretListener() {

		@Override
		public void caretUpdate(CaretEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			String text = editor.getText();
			int symbols = text.length();

			int sel = Math.abs(editor.getCaret().getDot()
					- editor.getCaret().getMark());
			int line;
			int column;
			try {
				line = editor.getLineOfOffset(editor.getCaret().getDot());
				int start = editor.getLineStartOffset(line);
				int end = editor.getCaret().getDot();
				column = end - start;
			} catch (BadLocationException e1) {
				return;
			}

			StringBuilder strb = new StringBuilder();
			strb.append("length: " + symbols);
			strb.append("  Ln: " + (line + 1));
			strb.append("  Col: " + (column + 1));
			strb.append("  Sel: " + sel);
			String message = strb.toString();

			if (sel == 0) {
				unique.setEnabled(false);
				invert.setEnabled(false);
				toUpper.setEnabled(false);
				toLower.setEnabled(false);
				sortSubmenu.setEnabled(false);
			} else {
				unique.setEnabled(true);
				invert.setEnabled(true);
				toUpper.setEnabled(true);
				toLower.setEnabled(true);
				sortSubmenu.setEnabled(true);
			}
			statsLabel.setText(message);
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to sort the lines by ascending order.
	 */
	private Action ascendnigSortAction = new LocalizableAction("asc", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sort(Collections.reverseOrder().reversed());
		}
	};

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to sort the lines by descending order.
	 */
	private Action descendingSortAction = new LocalizableAction("desc", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sort(Collections.reverseOrder());
		}
	};

	/**
	 * Method for sorting selected part of the text in text editor.
	 * 
	 * @param e
	 *            Comparator to sort by.
	 */
	public void sort(Comparator e) {
		int selected = tabbedPane.getSelectedIndex();
		JTextArea editor = editors.get(selected);

		Document document = editor.getDocument();
		int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
				.getMark());
		int end = Math.max(editor.getCaret().getDot(), editor.getCaret()
				.getMark());
		TreeSet<String> lines = new TreeSet<>(e);

		try {
			int line = editor.getLineOfOffset(start);
			start = editor.getLineStartOffset(line);
			line = editor.getLineOfOffset(end);
			end = editor.getLineEndOffset(line);
			String text = document.getText(start, end - start);
			document.remove(start, end - start);
			String[] linesArray = text.split("\n");
			for (String s : linesArray) {
				lines.add(s);
			}
			StringBuilder strb = new StringBuilder();
			for (String s : lines) {
				strb.append(s + "\n");
			}
			document.insertString(start, strb.toString(), null);
			repaint();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Listener implementing action which needs to be executed when user wants
	 * to remove duplicated lines from selected part of the text.
	 */
	private Action uniqueAction = new LocalizableAction("unique", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int selected = tabbedPane.getSelectedIndex();
			JTextArea editor = editors.get(selected);

			Document document = editor.getDocument();
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			int end = Math.max(editor.getCaret().getDot(), editor.getCaret()
					.getMark());
			LinkedHashSet<String> lines = new LinkedHashSet<>();

			try {
				int line = editor.getLineOfOffset(start);
				start = editor.getLineStartOffset(line);
				line = editor.getLineOfOffset(end);
				end = editor.getLineEndOffset(line);
				String text = document.getText(start, end - start);
				document.remove(start, end - start);
				String[] linesArray = text.split("\n");
				for (String s : linesArray) {
					lines.add(s);
				}
				StringBuilder strb = new StringBuilder();
				for (String s : lines) {
					strb.append(s + "\n");
				}
				document.insertString(start, strb.toString(), null);
				repaint();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	};

	/**
	 * Method that executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not used here.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		SwingUtilities.invokeLater(() -> {
			LocalizationProvider.getInstance().setLanguage("en");
			new JNotepadPP().setVisible(true);
		});
	}

}
