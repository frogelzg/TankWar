package model;

import java.awt.Point;

public class Tank extends VisibleImage {
	GamePanel gamePanel; // 游戏界面,提供当前面板的内容进行判断
	Direction direction; // 移动方向
	protectedbooleanalive=true; // 是否存活
	protectedintspeed=3; // 移动速度
	privatebooleanattackCoolDown=true;// 攻击冷却状态
	privateintattackCoolDownTime=500;// 攻击冷却时间毫秒间隔500ms发射子弹
	TankType type;// 坦克类型
	private String upImage; // 向上移动时的图片
	private String downImage;// 向下移动时的图片
	private String rightImage;// 向右移动时的图片
	private String leftImage;// 向左移动时的图片

	// 坦克构造方法初始化横纵坐标xy
	// url默认为向上的图片
	public Tank(int x, int y, String url, GamePanel gamePanel, TankType type) {
		super(x, y, url);
		this.gamePanel = gamePanle;
		this.type = type;
		direction = Directon.UP;
		switch (type) {
		case player1:// 玩家1
			upImage = ImageUtil.PLAYER1_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER1_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER1_LEFT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER1_RIGHT_IMAGE_URL;
			break;
		case player2:// 玩家2
			upImage = ImageUtil.PLAYER2_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER2_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYE2_LEFT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER2_RIGHT_IMAGE_URL;
			break;
		case bot:
			upImage = ImageUtil.BOT_UP_IMAGE_URL;
			downImage = ImageUtil.BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.BOT_LEFT_IMAGE_URL;
			leftImage = ImageUtil.BOT_RIGHT_IMAGE_URL;
			break;
		}

	}

	// 向左
	public void leftward() {
		if (direxction != Directon.LEFT)
			setImage(leftImage);// 若之前的移动方向不是左移，跟换左移图片
		direxction = Directon.LEFT;
		if (!hitWall(x - speed, y) && hitTank(x - speed, y)) {
			x -= speed;// 横坐标递减
			moveToborder();// 判断是否移动到游戏面板的边界
		}
	}

	// 向右
	public void rightward() {
		if (direxction != Directon.RIGHT)
			setImage(rightImage);
		direxction = Directon.RIGHT;
		if (!hitWall(x + speed, y) && hitTank(x + speed, y)) {
			x += speed;
			moveToborder();// 判断是否移动到游戏面板的边界
		}
	}

	// 向上
	public void upward() {
		if (direxction != Directon.UP)
			setImage(upImage);
		direxction = Directon.UP;
		if (!hitWall(x, y - speed) && hitTank(x, y - speed)) {
			y -= speed;
			moveToborder();// 判断是否移动到游戏面板的边界
		}
	}

	// 向下
	public void downward() {
		if (direxction != Directon.DOWN)
			setImage(downImage);
		direxction = Directon.DOWN;
		if (!hitWall(x, y + speed) && hitTank(x, y + speed)) {
			y += speed;
			moveToborder();// 判断是否移动到游戏面板的边界
		}
	}

	// 是否撞到墙块
	public boolean hitWall(int x,int y){
		Rectangle next=new Rectangle(x,y,width,height);//创建坦克移动后的目标区域
		List<Wall> walls=gamePanel.getWalls();//获取面板上所有的墙体对象
		for(int i=0,length=walls.size();i<length;i++){
			Wall w=walls.get(i);//获取墙块对象
			if(w instanceof GrassWall){//碰到草坪
				continue; 
			}elseif(w.hit(next)){ //撞到墙
				return true;
			}
		}
		return false;//没撞到墙
    }

	// 是否撞到坦克
	public boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// 创建坦克移动后的目标区域
		List<Tank> tanks = gamePanel.getTanks();// 获取面板上所有的坦克对象
		for (int i = 0, length = tanks.size(); i < length; i++) {
			Tank t = tanks.get(i);// 获取坦克对象
			if (!this.equals(t)) {// 此坦克与自身不是一个对象
				if (t.isAlive() && t.hit(next)) {// 此坦克存活并且与自身相撞
					return true;
				}
			}
		}
		return false;// 没撞到坦克
	}

	// 移动到面板的边界
	protected void moveToBorder(){
		if(x<0){
			x=0;
		}else if(x>gamePanel.getWidth()-width){
			x=gamePanel.getWidth-width;
		}
		
		if(y<0){
			y=0;
		}else if(y>gamePanel.getHeight()-height){
			y=gamePanel.getHeight-height
		}
	    }

	// 获取坦克头
	private Point getHeadPoint(){
		Point p=new Point();//创建头对象，作为头点
		switch(direxction){//判断移动方向
		case UP:
			p.x=x+width/2;
			p.y=y;
			break;
		case DOWN:
			p.x=x+width/2;
			p.y=y+height;
			break;
		case RIGHT:
			p.x=x+width;
			p.y=y+height/2;
			break;
		case LEFT：
			p.x=x;
				p.y=y+height/2; 
				break;
				default:
					p=null;
		}
		return p;//返回头点
	    }

	// 攻击
	public void attack() {
		if (attackCoolDown) {// 如果攻击功能完成冷却
			Point p = getHeadPoint();// 获取坦克头对象
			Bullet b = new Bullet(p.x - Bullet.LENGTH / 2, p.y - Bullet.LENGTH / 2, direction, gamePanel, type);
			gamePanel.addBullet(b);// 游戏面板添加子弹
			new AttackCD().start();// 开始冷却倒计时
		}
	}

	// 坦克是否存活
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	// 坦克的速度
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	// 攻击冷却时间线程
	private class AttackCD extends Thread {
		public void run() {
			attackCoolDown = false;// 将攻击功能设置为冷却状态
			try {
				Tread.sleep(attackCoolDown);// 休眠0.5秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attackCoolDown = true;// 将攻击功能解除冷却状态
		}
	}

	// 获取攻击功能是否处于冷却
	public boolean isAttackCoolDown() {
		return attackCoolDown;
	}

	public void setAttackCoolDownTime(int attackCoolDownTime) {
		this.attackCoolDownTime = attackCoolDownTime;
	}
}