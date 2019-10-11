package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Level;
import type.GameType;
import util.ImageUtil;

public class LoginPanel extends JPanel implements KeyListener {
	private MainFrame frame;
	private GameType type;
	private Image backgroud;
	private Image tank;
	private int y1 = 362, y2 = 422;
	private int tankY = y1;

	public LoginPanel(MainFrame frame) {
		this.frame = frame;
		addListener();
		try {
			backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUND_IMAGE_URL));
			tank = ImageIO.read(new File(ImageUtil.PLAYER1_RIGHT_IMAGE_URL));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
		Font font = new Font("黑体", Font.BOLD, 35);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("1 PLAYER", 350, 400);
		g.drawString("2 PLAYER", 350, 460);
		g.drawImage(tank, 280, tankY, this);
	}

	private void gotoLevelPanel() {
		frame.removeKeyListener(this);
		frame.setPanel(new LevelPanel(Level.nextLevel(), frame, type));

	}

	private void addListener() {
		frame.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO ×Ô¶¯Éú³ÉµÄ·½·¨´æ¸ù
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			if (tankY == y1) {
				tankY = y2;
			} else {
				tankY = y1;
			}
			repaint();
			break;
		case KeyEvent.VK_Y:
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_ENTER:
			if (tankY == y1) {
				type = GameType.ONE_PLAYER;
			} else {
				type = GameType.TWO_PLAYER;
			}
			gotoLevelPanel();// Ìø×ª¹Ø¿¨Ãæ°å
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO ×Ô¶¯Éú³ÉµÄ·½·¨´æ¸ù

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO ×Ô¶¯Éú³ÉµÄ·½·¨´æ¸ù

	}

}
