package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import model.Base;
import model.Boom;
import model.Bot;
import model.Bullet;
import model.Map;
import model.Tank;
import modelwall.Wall;
import type.GameType;
import type.TankType;
import util.ImageUtil;

public class GamePanel extends JPanel implements KeyListener {
	public static final int FRESH = 20;
	private BufferedImage image;
	private Graphics2D g2;
	private MainFrame frame;
	private GameType gameType;
	private Tank play1, play2;
	private boolean y_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num1_key;
	private int level;
	private List<Bullet> bullets;
	private volatile List<Tank> allTanks;
	private List<Tank> botTanks;
	private final int botCount = 20;
	private int botReadyCount = botCount;
	private int botSurplusCount = botCount;
	private int botMaxInMap = 6;
	private int botX[] = { 10, 367, 754 };
	private List<Tank> playerTanks;
	private volatile boolean finish = false;
	private Base base;
	private List<Wall> walls;
	private List<Boom> boomImage;
	private Random r = new Random();
	private int createBotTimer = 0;// 生产电脑计时器
	private Tank survivor;

	public GamePanel(MainFrame frame, int level, GameType gameType) {
		this.frame = frame;
		this.level = level;
		this.gameType = gameType;
		setBackground(Color.WHITE);
		init();
		Thread t = new FreshThread();
		t.start();
		addListener();
	}

	private void init() {
		bullets = new ArrayList<Bullet>();
		allTanks = new ArrayList<>();
		walls = new ArrayList<>();
		boomImage = new BufferedImage(794, 572, BufferedImage.TYPE_INT_BGR);
		g2 = image.createGraphics();

		playerTanks = new ArrayList<>();
		play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.player1);
		if (gameType == GameType.TWO_PLAYER) {
			play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.player2);
			playerTanks.add(play2);
		}
		playerTanks.add(play1);
		botTanks = new Vector<>();
		botTanks.add(new Bot(botX[0], 1, this, TankType.bot));
		botTanks.add(new Bot(botX[1], 1, this, TankType.bot));
		botTanks.add(new Bot(botX[2], 1, this, TankType.bot));
		botReadyCount -= 3;
		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);
		base = new Base(367, 532);
		initWalls();
	}

	private void addListener() {
		frame.addKeyListener(this);
	}

	private void initWalls() {
		Map map = Map.getMap(level);
		walls.addAll(map.getWalls());
		walls.add(base);
	}

	public void paint(Graphics g) {
		paintTankActoin();
		CreateBot();
		paintImage();
		g.drawImage(image, 0, 0, this);
	}

	private void paintImage() {

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		panitBoom();
		paintBotTanks();
		paintBotCount();
		panitPlayerTanks();
		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);
		paintWalls();
		paintBullets();
		if (botSurplusCount == 0) {
			stopThread();
			paintBotCount();
			g2.setFont(new Font("楷体", Font.BOLD, 50));
			g2.setColor(Color.GREEN);
			g2.drawString("胜利", 250, 400);
			gotoNextLevel();
		}
		if (gameType == GameType.ONE_PLAYER) {
			if (!play1.isAlive()) {
				stopThread();
				boomImage.add(new Boom(play1.x, play1.y));
				panitBoom();
				paintGameOver();
				gotoPrevisousLevel();
			}

		} else {// 双人模式
			if (play1.isAlive() && !play2.isAlive()) {
				survivor = play1;
			} else if (!play1.isAlive() && play2.isAlive()) {
				survivor = play2;
			} else if (!(play1.isAlive() || play2.isAlive())) {
				stopThread();
				boomImage.add(new Boom(survivor.x, survivor.y));
				panitBoom();
				paintGameOver();
				gotoPrevisousLevel();// 重新进入本关
			}
		}
		if (!base.isAlive()) {
			stopThread();
			paintGameOver();
			base.setImage(ImageUtil.BREAK_BASE_IMAGE_URL);
			gotoPrevisousLevel();

		}
		g2.drawImage(base.getImage(), base.x.base.y, this);
	}

	private void paintBotCount() {
		g2.setColor(Color.BLUE);
		g2.drawString("敌方坦克剩余" + botSurplusCount, 337, 15);

	}

	private void paintGameOver() {
		g2.setFont(new Font("楷体", Font.BOLD, 50));
		g2.setColor(Color.RED);
		g2.drawString("Game Over !", 250, 400);

	}

	private void panitBoom() {
		for (int i = 0; i < boomImage.size(); i++) {
			Boom boom = boomImage.get(i);
			if (boom.isAlive()) {
				boom.show(g2);
			} else {
				boomImage.remove(i);
				i--;
			}
		}

	}

	private void paintWalls() {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (w.isAlive()) {
				g2.drawImage(w.getImage(), w.x, w.notify(), this);

			} else {
				walls.remove(i);
				i--;
			}
		}
	}

	private void paintBullets() {
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (b.isAlive()) {
				b.move();
				b.hitBase();
				b.hitWall();
				b.hitTank();
				g2.drawImage(b.getImage(), b.x, b.y, this);
			} else {
				bullets.remove(i);
				i--;
			}
		}
	}

	private void paintBotTanks() {
		for (int i = 0; i < botTanks.size(); i++) {
			Bot t = (Bot) botTanks.get(i);
			if (t.isAlive()) {
				t.go();
				g2.drawImage(t.getImage(), t.x, t.y, this);
			} else {
				botTanks.remove(i);
				i--;
				boomImage.add(new Boom(t.x, t.y));
				decreaseBot();
			}
		}
	}

	private void panitPlayerTanks() {
		for (int i = 0; i < playerTanks.size(); i++) {
			Tank t = playerTanks.get(i);
			if (t.isAlive()) {
				t.go();
				g2.drawImage(t.getImage(), t.x, t.y, this);
			} else {
				playerTanks.remove(i);
				i--;
				boomImage.add(new Boom(t.x, t.y));

			}
		}
	}

	private synchronized void stopThread() {
		frame.removeKeyListener(this);
		finish = true;
	}

	private class FreshThread extends Thread {
		public void run() {
			while (!finish) {
				repaint();
				try {
					Thread.sleep(FRESH);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void CreateBot() {
		createBotTimer += FRESH;
		if (botTanks.size() < botMaxInMap && botReadyCount > 0 && createBotTimer >= 4000) {
			int index = r.nextInt(3);
			Rectangle bornRect = new Rectangle(botX[index], 1, 35, 35);
			for (int i = 0, lengh = allTanks.size(); i < lengh; i++) {
				Tank t = allTanks.get(i);
				if (t.isAlive() && t.hit(bornRect)) {
					return;
				}
			}
			botTanks.add(new Bot(botX[index], 1, GamePanel.this, TankType.bot));
			botReadyCount--;
			createBotTimer = 0;
		}
	}

	private void gotoNextLevel() {
		Thread jump = new JumpPageThread(Level.nextLevel());
		jump.start();
	}

	private void gotoPrevisousLevel() {
		Thread jump = new JumpPageThread(Level.previsousLevel());
		jump.start();
	}

	public void decreaseBot() {
		botSurplusCount--;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自动生成的方法存根
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:
			y_key = true;
			break;
		case KeyEvent.VK_W:
			w_key = true;
			a_key = false;
			s_key = false;
			d_key = false;
			break;
		case KeyEvent.VK_A:
			w_key = false;
			a_key = true;
			s_key = false;
			d_key = false;
			break;
		case KeyEvent.VK_S:
			w_key = false;
			a_key = false;
			s_key = true;
			d_key = false;
			break;
		case KeyEvent.VK_D:
			w_key = false;
			a_key = false;
			s_key = false;
			d_key = true;
			break;
		case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key = true;
			break;
		case KeyEvent.VK_UP:
			up_key = true;
			down_key = false;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_DOWN:
			up_key = false;
			down_key = true;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_LEFT:
			up_key = false;
			down_key = false;
			right_key = false;
			left_key = true;
			break;
		case KeyEvent.VK_RIGHT:
			up_key = false;
			down_key = false;
			right_key = true;
			left_key = false;
			break;
		}
	}

	private void paintTankActoin() {
		if (y_key) {
			play1.attack();
		}
		if (w_key) {
			play1.upward();
		}
		if (d_key) {
			play1.rightward();
		}
		if (a_key) {
			play1.leftward();
		}
		if (s_key) {
			play1.downward();
		}
		if (gameType == GameType.TWO_PLAYER) {
			if (num1_key) {
				play2.attack();
			}

			if (up_key) {
				play2.upward();
			}

			if (right_key) {
				play2.rightward();
			}

			if (left_key) {
				play2.leftward();
			}
			if (down_key) {
				play2.downward();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:
			y_key = false;
			break;
		case KeyEvent.VK_W:
			w_key = false;
			break;
		case KeyEvent.VK_A:
			a_key = false;
			break;
		case KeyEvent.VK_S:
			s_key = false;
			break;
		case KeyEvent.VK_D:
			d_key = false;
			break;
		case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key = false;
			break;
		case KeyEvent.VK_UP:
			up_key = false;
			break;
		case KeyEvent.VK_DOWN:
			down_key = false;
			break;
		case KeyEvent.VK_LEFT:
			left_key = false;
			break;
		case KeyEvent.VK_RIGHT:
			right_key = false;
			break;
		}
	}

	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	public List<Wall> getWall() {
		return walls;
	}

	public Base getBase() {
		return base;
	}

	public List<Tank> getTanks() {
		return allTanks;
	}

	private class JumpPageThead extends Thread {
		int level;

		public JumpPageThead(int level) {
			this.level = level;
		}

		public void run() {
			try {
				Thread.sleep(1000);
				frame.setPanel(new LevelPanel(level, frame, gameType));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自动生成的方法存根

	}

}
