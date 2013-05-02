package Chat;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;

public class Client_GUI extends JPanel implements ActionListener{
	private JTextField textField;
	private JButton btnSenden;
	private JTextArea textArea;
	private Client_Controller cc;
	private JMenuBar menuBar;
	private JMenu mnEinstellungen;
	private JCheckBoxMenuItem chckbxmntmAutoMailbox;
	public Client_GUI(Client_Controller cc) {
		this.cc = cc;
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.SOUTH);
		splitPane.setDividerLocation(350);
		splitPane.setEnabled(false);
		
		btnSenden = new JButton("Senden");
		splitPane.setRightComponent(btnSenden);
		btnSenden.addActionListener(this);
		
		textField = new JTextField();
		splitPane.setLeftComponent(textField);
		textField.setColumns(10);
		textField.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		menuBar = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar);
		
		mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		
		chckbxmntmAutoMailbox = new JCheckBoxMenuItem("Auto Mailbox");
		chckbxmntmAutoMailbox.addActionListener(this);
		mnEinstellungen.add(chckbxmntmAutoMailbox);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==textField || e.getSource() ==btnSenden){
			String message = textField.getText();
			if ( !message.equals("") ) {
				cc.sendMessage( message);
				textField.setText("");
			}
			

		}
		if(e.getSource() == chckbxmntmAutoMailbox){
			if(chckbxmntmAutoMailbox.isSelected()){
				System.out.println("Selected");
			}else if(!chckbxmntmAutoMailbox.isSelected()){
				System.out.println("Unselected");
			}
		}
	}
	public void setChat(String msg){
		textArea.setText(textArea.getText() + msg + "\n");
	}
	
}