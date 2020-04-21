import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.undo.*;
import javax.swing.event.*;

public class Java_Notepad extends JFrame
{
	static JTextArea ta;
	JMenuBar mb;
	JMenu mFile,mEdit,mFormat,mHelp;
	JMenuItem iNew,iOpen,iSave,iSaveAs,iExit,iCut,iCopy,iPaste,iFontColor,iFind,iReplace,iFont;
	JCheckBoxMenuItem wordWrap;
	String filename;
	JFileChooser fc;
	String fileContent;
	UndoManager undo;
	UndoAction undoAction;
	RedoAction redoAction;
	// public static Java_Notepad frmMain = new Java_Notepad();
	FontHelper font;
	
	public Java_Notepad()
	{
		//initialization method
		initComponent();
		
		//save action listener
		iSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
		
		//saveas action listener
		iSaveAs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				saveAs();
			}
		});
		
		//open action listener
		iOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				open();
			}
		});
		
		//new action listener
		iNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new_file();
			}
		});
		
		//exit action listener
		iExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				close();
			}
		});
		
		///////////Edit Menu//////////////////
		
		//cut action listener//
		iCut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta.cut();
			}
		});
		//copy action listener//
		iCopy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta.copy();
			}
		});
		//cut action listener//
		iPaste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta.paste();
			}
		});
		
		ta.getDocument().addUndoableEditListener(new UndoableEditListener(){
			public void undoableEditHappened(UndoableEditEvent e){
				undo.addEdit(e.getEdit());
				undoAction.update();
				redoAction.update();
			}
		});
		
		ta.setWrapStyleWord(true);
		wordWrap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(wordWrap.isSelected()){
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
				}else{
					ta.setLineWrap(false);
					ta.setWrapStyleWord(false);
				}
			}
		});
		
		iFontColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Color c=JColorChooser.showDialog(rootPane, "Choose Font Color", Color.yellow);
				ta.setForeground(c);
			}
		});
		
		iFind.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new FindAndReplace(null, false);
			}
		});
		
		iReplace.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new FindAndReplace(null, true);
			}
		});
		
		iFont.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				font.setVisible(true);
			}
		});
		font.getOk().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ta.setFont(font.font());
				font.setVisible(false);
			}
		});
		font.getCancel().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				font.setVisible(false);
			}
		});
	}
	
	private void initComponent()
	{
		fc = new JFileChooser(".");
		ta = new JTextArea();
		font = new FontHelper();
		undo = new UndoManager();
		ImageIcon undoIcon = new ImageIcon(getClass().getResource("undo.gif"));
		ImageIcon redoIcon = new ImageIcon(getClass().getResource("redo.gif"));
		
		undoAction = new UndoAction(undoIcon);
		redoAction = new RedoAction(redoIcon);
		
		getContentPane().add(ta);
		getContentPane().add(new JScrollPane(ta),BorderLayout.CENTER);
		
		///////////////////////// MENU //////////////////////////
		
		/////Menubar/////
		mb = new JMenuBar();
		UIManager.put("MenuBar.background", Color.ORANGE);
		//Menu///
		mFile = new JMenu("File");
		mEdit = new JMenu("Edit");
		mFormat = new JMenu("Format");
		mHelp = new JMenu("Help");
		
		//Image icon setup for File menu
		ImageIcon newIcon = new ImageIcon(getClass().getResource("new.gif"));
		ImageIcon openIcon = new ImageIcon(getClass().getResource("open.gif"));
		ImageIcon saveIcon = new ImageIcon(getClass().getResource("Save.gif"));
		ImageIcon saveAsIcon = new ImageIcon(getClass().getResource("saveAs.gif"));
		ImageIcon exitIcon = new ImageIcon(getClass().getResource("exit.gif"));
		
		//Image icon set up for Edit menu
		ImageIcon cutIcon = new ImageIcon(getClass().getResource("cut.gif"));
		ImageIcon copyIcon = new ImageIcon(getClass().getResource("copy.gif"));
		ImageIcon pasteIcon = new ImageIcon(getClass().getResource("paste.gif"));
		
		ImageIcon findIcon = new ImageIcon(getClass().getResource("find.gif"));
		ImageIcon replaceIcon = new ImageIcon(getClass().getResource("replace.gif"));
		ImageIcon fonIcon = new ImageIcon(getClass().getResource("font.gif"));
		
		//Menu Item for File///
		
		iNew = new JMenuItem("New", newIcon);
		iOpen = new JMenuItem("Open",openIcon);
		iSave = new JMenuItem("Save", saveIcon);
		iSaveAs = new JMenuItem("SaveAs", saveAsIcon);
		iExit = new JMenuItem("Exit", exitIcon);
		
		//Menu Item for Edit
		iCut = new JMenuItem("Cut", cutIcon);
		iCopy = new JMenuItem("Copy",copyIcon);
		iPaste = new JMenuItem("Paste", pasteIcon);
		iFind = new JMenuItem("Find",findIcon);
		iReplace = new JMenuItem("Replace", replaceIcon);
		
		//Menu item for Format//
		wordWrap = new JCheckBoxMenuItem("Word Wrap");
		iFontColor = new JMenuItem("Font Color");
		iFont = new JMenuItem("Font Style", fonIcon);
		
		
		//adding shortcut to File Menu
		iNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		iOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		iSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		
		//adding shortcut to Edit Menu
		iCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		iCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		iPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		
		
		
		//Adding Item to menu File/////
		mFile.add(iNew);mFile.add(iOpen);mFile.add(iSave);mFile.add(iSaveAs);mFile.addSeparator();mFile.add(iExit);
		
		//Adding item to menu Edit////
		mEdit.add(undoAction);mEdit.add(redoAction);mEdit.add(iCut);mEdit.add(iCopy);mEdit.add(iPaste);mEdit.add(iFind);mEdit.add(iReplace);
		
		//Adding item to menu Format////
		mFormat.add(wordWrap);mFormat.add(iFontColor);mFormat.add(iFont);
		
		//Adding menu to menubar////
		
		mb.add(mFile);mb.add(mEdit);mb.add(mFormat);mb.add(mHelp);
		
		setJMenuBar(mb);
		
		
		setTitle("Untitled Notepad");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void save(){
		FileOutputStream fout = null;
		// int retval=-1;
		try{
			
				
			if(filename==null){
				saveAs();
			}
			else{
				fout=new FileOutputStream(filename);
				String data = ta.getText();
				byte b[] = data.getBytes();
				fout.write(b);
				fout.close();
			
				JOptionPane.showMessageDialog(rootPane, "File Saved");
				fileContent=ta.getText();
			}
				
			
		}catch(IOException ex){}
		finally{
			
		}
	}
	
	private void saveAs(){
		FileOutputStream fout = null;
		int retval=-1;
		try{
		retval = fc.showSaveDialog(this);
		if(retval==JFileChooser.APPROVE_OPTION){
			fout=new FileOutputStream(fc.getSelectedFile());
			
			String data = ta.getText();
			
			byte b[] = data.getBytes();
			fout.write(b);
			fout.close();
			
			JOptionPane.showMessageDialog(rootPane, "File Saved");
			fileContent=ta.getText();
			filename = fc.getSelectedFile().getName();
			setTitle(filename);
		}
				
		
			
		
		}catch(IOException ex){ex.printStackTrace();}
		
	}
	
	private void open(){
		
		int i=fc.showOpenDialog(this);
		if(i==JFileChooser.APPROVE_OPTION)
		{
			File f = fc.getSelectedFile();
			String filepath = f.getPath();
			try{
				BufferedReader ob=new BufferedReader(new FileReader(filepath));
				String s1="",s2="";
				while((s1=ob.readLine())!=null)
					s2+=s1+"\n";
				ta.setText(s2);
				ob.close();
				
				filename = fc.getSelectedFile().getName();
				setTitle(filename);
				
			}catch(Exception ex){ex.printStackTrace();}
		}
	}
	
	private void new_file(){
		if(!ta.getText().equals("") && !ta.getText().equals(fileContent))
		{
			if(filename==null){
				int option = JOptionPane.showConfirmDialog(rootPane,"Do you want to save the changes?");
				if(option==0){
					saveAs();
					clear();
				}
				else{
					clear();
				}
			}
			else{
				int option = JOptionPane.showConfirmDialog(rootPane,"Do you want to save the changes?");
				if(option==0){
					save();
					clear();
				}
				else{
					clear();
				}
			}
		}
		else{
			clear();
		}
	}
	
	private void clear(){
		ta.setText(null);
		setTitle("Untitled Notepad");
		filename=null;
		fileContent=null;
	}
	
	class UndoAction extends AbstractAction{
		
		public UndoAction(ImageIcon undoIcon){
			super("Undo", undoIcon);
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e){
			try{
				undo.undo();
			}catch(CannotUndoException ex){
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}
		protected void update(){
			if(undo.canUndo()){
				setEnabled(true);
				putValue(Action.NAME, "Undo");
			}
			
		}
	}
	
		
	class RedoAction extends AbstractAction{
		
		public RedoAction(ImageIcon redoIcon){
			super("Redo", redoIcon);
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e){
			try{
				undo.redo();
			}catch(CannotRedoException ex){
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}
		protected void update(){
			if(undo.canRedo()){
				setEnabled(true);
				putValue(Action.NAME, "Redo");
			}
			else{
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}
	
	
	private void close(){
		if(!ta.getText().equals("") && !ta.getText().equals(fileContent))
		{
			if(filename==null){
				int option = JOptionPane.showConfirmDialog(rootPane,"Do you want to save the changes and close?");
				if(option==0){
					saveAs();
					System.exit(1);
				}
				else{
					System.exit(1);
				}
			}
			else{
				int option = JOptionPane.showConfirmDialog(rootPane,"Do you want to save the changes and close?");
				if(option==0){
					save();
					System.exit(1);
				}
				else{
					System.exit(1);
				}
			}
		}
		else{
			System.exit(1);
		}
	}
	
	
	
	public static void main(String args[])
	{
		Java_Notepad jn=new Java_Notepad();
	}
	
	public static JTextArea getArea(){
		return ta;
	}
}

		