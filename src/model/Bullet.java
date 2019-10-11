package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import modelwall.Wall;
import type.Direction;

public class Bullet extends VisibleImage {

	Direction direction;
	static final int LENGTH = 8; // 子弹的（正方体）边长
	private GamePanal gamePanal;// 游戏面板
	private int speed = 7;// 移动速度
	private boolean alive = true;// 子弹是否有效
	Color color = Color.ORANGE;// 子弹颜色
	TankType owner;// 发出子弹的坦克类型

	/**
	 * 子弹构造方法
	 * 
	 * @param x         - 子弹的初始横坐标
	 * @param y         - 子弹的初始纵坐标
	 * @param direction - 子弹的发射方向
	 * @param gamePanel - 游戏面板对象
	 * @param Owner     - 发出子弹的坦克类型
	 */
	public Bullet(int x, int y, Direction direction, TankType owner) {
		super(x, y, LENGTH, LENGTH); // 调用父类构造方法
		// TODO 自动生成的构造函数存根
		this.direction = direction;
		this.gamePanal = gamePanal;
		this.owner = owner;
		init();// 初始化组件
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		// TODO 自动生成的方法存根
		Graphics g = image.getGraphics();// 获取图片的绘图方法
		g.setColor(Color.WHITE);// 使用白色绘图
		g.fillRect(0, 0, LENGTH, LENGTH);// 绘制一个铺满整个图片的白色实心矩形
		g.setColor(color);// 使用子弹颜色
		g.fillOval(0, 0, LENGTH, LENGTH);// 铺满整个图片的实心圆形
		g.setColor(Color.BLACK); // 使用黑色
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1); // 绘制黑色边框，防止出界
	}

	/**
	 * 子弹移动
	 */
	public void move() {
		switch (direction) { // 判断移动方向
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
	 * 移动函数
	 */
	private void rightword() {
		// TODO 自动生成的方法存根
		x += speed;
		moveToBorder();
	}

	private void leftword() {
		// TODO 自动生成的方法存根
		x -= speed;
		moveToBorder(); // 移动出面板后销毁子弹
	}

	private void downword() {
		// TODO 自动生成的方法存根
		y += speed;
		moveToBorder();
	}

	private void upward() {
		// TODO 自动生成的方法存根
		y -= speed;
		moveToBorder();
	}

	/**
	 * 击中坦克
	 */
	public void hitTank() {
		List<Tank> tanks = gamePanel.getTanks(); // 获取坦克的集合
		for (int i = 0, length = tanks.size(); i < length; i++) {// 遍历坦克集合
			Tank t = tanks.get(i);
			if (t.isAlive() && this.hit(t)) {// 坦克活着并且子弹击中坦克
				switch (owner) {
				case player1:// 玩家的子弹
				case player2:
					if (t instanceof Bot) {// 击中电脑
						alive = false;// 子弹销毁
						t.setAlive(false);// 电脑坦克死亡
					} else if (t instanceof Tank) {// 击中玩家
						alive = false;
					}
					break;
				case bot:// 电脑的子弹
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

	// 击中基地
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

	// 移除面板边界时销毁子弹
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