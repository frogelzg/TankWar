package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import modelwall.Wall;
import type.Direction;

public class Bullet extends VisibleImage {

	Direction direction;
	static final int LENGTH = 8; // �ӵ��ģ������壩�߳�
	private GamePanal gamePanal;// ��Ϸ���
	private int speed = 7;// �ƶ��ٶ�
	private boolean alive = true;// �ӵ��Ƿ���Ч
	Color color = Color.ORANGE;// �ӵ���ɫ
	TankType owner;// �����ӵ���̹������

	/**
	 * �ӵ����췽��
	 * 
	 * @param x         - �ӵ��ĳ�ʼ������
	 * @param y         - �ӵ��ĳ�ʼ������
	 * @param direction - �ӵ��ķ��䷽��
	 * @param gamePanel - ��Ϸ������
	 * @param Owner     - �����ӵ���̹������
	 */
	public Bullet(int x, int y, Direction direction, TankType owner) {
		super(x, y, LENGTH, LENGTH); // ���ø��๹�췽��
		// TODO �Զ����ɵĹ��캯�����
		this.direction = direction;
		this.gamePanal = gamePanal;
		this.owner = owner;
		init();// ��ʼ�����
	}

	/**
	 * ��ʼ�����
	 */
	private void init() {
		// TODO �Զ����ɵķ������
		Graphics g = image.getGraphics();// ��ȡͼƬ�Ļ�ͼ����
		g.setColor(Color.WHITE);// ʹ�ð�ɫ��ͼ
		g.fillRect(0, 0, LENGTH, LENGTH);// ����һ����������ͼƬ�İ�ɫʵ�ľ���
		g.setColor(color);// ʹ���ӵ���ɫ
		g.fillOval(0, 0, LENGTH, LENGTH);// ��������ͼƬ��ʵ��Բ��
		g.setColor(Color.BLACK); // ʹ�ú�ɫ
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1); // ���ƺ�ɫ�߿򣬷�ֹ����
	}

	/**
	 * �ӵ��ƶ�
	 */
	public void move() {
		switch (direction) { // �ж��ƶ�����
		case UP:
			upward();
			break;
		case DOWN:
			downword();
			break;
		case LEFT:
			leftword();
			break;
		case RIGHT:
			rightword();
			break;
		}
	}

	/**
	 * �ƶ�����
	 */
	private void rightword() {
		// TODO �Զ����ɵķ������
		x += speed;
		moveToBorder();
	}

	private void leftword() {
		// TODO �Զ����ɵķ������
		x -= speed;
		moveToBorder(); // �ƶ������������ӵ�
	}

	private void downword() {
		// TODO �Զ����ɵķ������
		y += speed;
		moveToBorder();
	}

	private void upward() {
		// TODO �Զ����ɵķ������
		y -= speed;
		moveToBorder();
	}

	/**
	 * ����̹��
	 */
	public void hitTank() {
		List<Tank> tanks = gamePanel.getTanks(); // ��ȡ̹�˵ļ���
		for (int i = 0, length = tanks.size(); i < length; i++) {// ����̹�˼���
			Tank t = tanks.get(i);
			if (t.isAlive() && this.hit(t)) {// ̹�˻��Ų����ӵ�����̹��
				switch (owner) {
				case player1:// ��ҵ��ӵ�
				case player2:
					if (t instanceof Bot) {// ���е���
						alive = false;// �ӵ�����
						t.setAlive(false);// ����̹������
					} else if (t instanceof Tank) {// �������
						alive = false;
					}
					break;
				case bot:// ���Ե��ӵ�
					if (t instanceof Bot) {
						alive = false;
					} else if (t instanceof Tank) {
						alive = false;
						t.setAlive(false);
					}
					break;
				default:
					alive = false;
					t.setAlive(false);
					break;
				}

			}
		}
	}

	// ���л���
	public void hitBase() {
		Base b = gamePanel.getBase();
		if (this.hit(b)) {
			alive = false;
			b.setAlive(false);
		}
	}

	public void hitWall
	{
		List<Wall> walls = gamePanel.getWalls();
		for (int i = 0, length = walls.size(); i < length; i++) {
			Wall w = walls.get(i);
			if (this.hit(w)) {
				if (w instanceof BrickWall) {
					alive = false;
					w.setAlive(false);
				}
				if (w instanceof IronWall) {
					alive = false;
				}
			}
		}
	}

	// �Ƴ����߽�ʱ�����ӵ�
	private void moveToBorder() {
		if (x < 0 || x > gamePanel.getWidth() - getWidth() || y < 0 || y > gamePanel.getHeight() - getHeight()) {
			dispose();
		}
	}

	//
	private synchronized void dispose() {
		alive = false;
	}

	//
	public boolean isAlive() {
		return alive;
	}

}