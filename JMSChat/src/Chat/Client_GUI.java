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

public class Client_GUI extends JPanel implements ActionListener{
	private JTextField textField;
	private JButton btnSenden;
	private JTextArea textArea;
	private Client_Controller cc;
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
	}
	public void setChat(String msg){
		textArea.setText(textArea.getText() + msg + "\n");
	}
	
}