package Chat;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
/**
 * Standart Frame um Swing GUI anzeigen zu können
 * @author Dominik Backhausen
 * @version 1.0
 */
public class MyFrame extends JFrame{
	private JPanel p;
	
	public MyFrame(JPanel p,JMenuBar m, String text, int x, int y, int h, int w, boolean haup){
		this.p = p;
		this.setLayout(new BorderLayout());
		this.setBounds(x, y, h, w);
		this.setTitle(text);
		this.add(p);
		this.setVisible(true);
		this.setJMenuBar(m);
		if(haup)
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		else
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
