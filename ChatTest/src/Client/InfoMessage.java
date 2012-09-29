package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class InfoMessage extends JFrame {
	Font font = new Font("Verdana", Font.PLAIN, 12);
	ImageIcon iconChat = new ImageIcon("./image/p.png");
	
	public InfoMessage(int width, int height, String message, String title, String filename) {
		 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 setBounds(((screenSize.width - width)/2), ((screenSize.height - height)/2), width, height);
		 setPreferredSize(new Dimension(width, height));
		 setTitle(title);
		 this.setLayout(null);
		 
		 Icon icon = new ImageIcon(filename);
		 JLabel jLabel = new JLabel(message, icon, SwingConstants.LEFT);
		 jLabel.setBounds(20, 15, 350, 32);
	     jLabel.setFont(font);
		getContentPane().add(jLabel);
		
	     setIconImage(iconChat.getImage());
		 
		 JButton OK = new JButton();
		 OK.setBounds(80, 50, 85, 25);
	     OK.setText("OK");
	     OK.setFont(font);
	     
	     OK.addActionListener(new ActionListener() {
	     public void actionPerformed(ActionEvent evt) {
			OKActionPerformed(evt);
	      }

		private void OKActionPerformed(ActionEvent evt) {
			setVisible(false);
			}
		});
	    
	     getContentPane().add(OK);
	    getRootPane().setDefaultButton(OK);
	    
	    setResizable(false);
	}
}
