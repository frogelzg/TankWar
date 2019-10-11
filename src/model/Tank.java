package model;

import java.awt.Point;

public class Tank extends VisibleImage {
	GamePanel gamePanel; // ��Ϸ����,�ṩ��ǰ�������ݽ����ж�
	Direction direction; // �ƶ�����
	protectedbooleanalive=true; // �Ƿ���
	protectedintspeed=3; // �ƶ��ٶ�
	privatebooleanattackCoolDown=true;// ������ȴ״̬
	privateintattackCoolDownTime=500;// ������ȴʱ�������500ms�����ӵ�
	TankType type;// ̹������
	private String upImage; // �����ƶ�ʱ��ͼƬ
	private String downImage;// �����ƶ�ʱ��ͼƬ
	private String rightImage;// �����ƶ�ʱ��ͼƬ
	private String leftImage;// �����ƶ�ʱ��ͼƬ

	// ̹�˹��췽����ʼ����������xy
	// urlĬ��Ϊ���ϵ�ͼƬ
	public Tank(int x, int y, String url, GamePanel gamePanel, TankType type) {
		super(x, y, url);
		this.gamePanel = gamePanle;
		this.type = type;
		direction = Directon.UP;
		switch (type) {
		case player1:// ���1
			upImage = ImageUtil.PLAYER1_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER1_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER1_LEFT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER1_RIGHT_IMAGE_URL;
			break;
		case player2:// ���2
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

	// ����
	public void leftward() {
		if (direxction != Directon.LEFT)
			setImage(leftImage);// ��֮ǰ���ƶ����������ƣ���������ͼƬ
		direxction = Directon.LEFT;
		if (!hitWall(x - speed, y) && hitTank(x - speed, y)) {
			x -= speed;// ������ݼ�
			moveToborder();// �ж��Ƿ��ƶ�����Ϸ���ı߽�
		}
	}

	// ����
	public void rightward() {
		if (direxction != Directon.RIGHT)
			setImage(rightImage);
		direxction = Directon.RIGHT;
		if (!hitWall(x + speed, y) && hitTank(x + speed, y)) {
			x += speed;
			moveToborder();// �ж��Ƿ��ƶ�����Ϸ���ı߽�
		}
	}

	// ����
	public void upward() {
		if (direxction != Directon.UP)
			setImage(upImage);
		direxction = Directon.UP;
		if (!hitWall(x, y - speed) && hitTank(x, y - speed)) {
			y -= speed;
			moveToborder();// �ж��Ƿ��ƶ�����Ϸ���ı߽�
		}
	}

	// ����
	public void downward() {
		if (direxction != Directon.DOWN)
			setImage(downImage);
		direxction = Directon.DOWN;
		if (!hitWall(x, y + speed) && hitTank(x, y + speed)) {
			y += speed;
			moveToborder();// �ж��Ƿ��ƶ�����Ϸ���ı߽�
		}
	}

	// �Ƿ�ײ��ǽ��
	public boolean hitWall(int x,int y){
		Rectangle next=new Rectangle(x,y,width,height);//����̹���ƶ����Ŀ������
		List<Wall> walls=gamePanel.getWalls();//��ȡ��������е�ǽ�����
		for(int i=0,length=walls.size();i<length;i++){
			Wall w=walls.get(i);//��ȡǽ�����
			if(w instanceof GrassWall){//������ƺ
				continue; 
			}elseif(w.hit(next)){ //ײ��ǽ
				return true;
			}
		}
		return false;//ûײ��ǽ
    }

	// �Ƿ�ײ��̹��
	public boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// ����̹���ƶ����Ŀ������
		List<Tank> tanks = gamePanel.getTanks();// ��ȡ��������е�̹�˶���
		for (int i = 0, length = tanks.size(); i < length; i++) {
			Tank t = tanks.get(i);// ��ȡ̹�˶���
			if (!this.equals(t)) {// ��̹����������һ������
				if (t.isAlive() && t.hit(next)) {// ��̹�˴�����������ײ
					return true;
				}
			}
		}
		return false;// ûײ��̹��
	}

	// �ƶ������ı߽�
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

	// ��ȡ̹��ͷ
	private Point getHeadPoint(){
		Point p=new Point();//����ͷ������Ϊͷ��
		switch(direxction){//�ж��ƶ�����
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
		case LEFT��
			p.x=x;
				p.y=y+height/2; 
				break;
				default:
					p=null;
		}
		return p;//����ͷ��
	    }

	// ����
	public void attack() {
		if (attackCoolDown) {// ����������������ȴ
			Point p = getHeadPoint();// ��ȡ̹��ͷ����
			Bullet b = new Bullet(p.x - Bullet.LENGTH / 2, p.y - Bullet.LENGTH / 2, direction, gamePanel, type);
			gamePanel.addBullet(b);// ��Ϸ�������ӵ�
			new AttackCD().start();// ��ʼ��ȴ����ʱ
		}
	}

	// ̹���Ƿ���
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	// ̹�˵��ٶ�
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	// ������ȴʱ���߳�
	private class AttackCD extends Thread {
		public void run() {
			attackCoolDown = false;// ��������������Ϊ��ȴ״̬
			try {
				Tread.sleep(attackCoolDown);// ����0.5��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attackCoolDown = true;// ���������ܽ����ȴ״̬
		}
	}

	// ��ȡ���������Ƿ�����ȴ
	public boolean isAttackCoolDown() {
		return attackCoolDown;
	}

	public void setAttackCoolDownTime(int attackCoolDownTime) {
		this.attackCoolDownTime = attackCoolDownTime;
	}
}