package frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Container;

public class MainFrame extends JFrame {
	public MainFrame() {
		setTitle("Tank War");
		setSize(800, 600);
		setResizable(false);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addListener();
		setPanel(new LoginPanel(this));
	}

	private void addListener() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int closeCode = JOptionPane.showConfirmDialog(MainFrame.this, "是否退出游戏", "提示！",
						JOptionPane.YES_NO_OPTION);
				if (closeCode == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	public void setPanel(JPanel panel) {
		Container c = getContentPane();
		c.removeAll();
		c.add(panel);
		c.validate();
	}
}
