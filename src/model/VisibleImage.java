package model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * ����ʾͼ��ĳ�����
 * 
 * @author asus-pc
 *
 */

public class VisibleImage {
	/**
	 * �ᡢ������
	 */
	public int x;
	public int y;
	/**
	 * ������
	 */
	int width;
	int height;

	/**
	 * ͼ�����
	 */
	BufferedImage image;

	/**
	 * ���췽��
	 * 
	 * @param x      -������
	 * @param y      -������
	 * @param width  -��
	 * @param height -��
	 */
	public VisibleImage(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// ʵ����ͼƬ
	}

	/**
	 * ���췽��
	 * 
	 * @param x   -������
	 * @param y   -������
	 * @param url -ͼƬ·��
	 */
	public VisibleImage(int x, int y, String url) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(new File(url));// ��ȡ��·����ͼƬ����
			this.width = image.getWidth();
			this.height = image.getHeight();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡͼƬ
	 * 
	 * @return ����ʾ��ͼƬ
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param image -����ʾ��ͼƬ
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param image -����ʾ��ͼƬ
	 */
	public void setImage(String url) {
		try {
			this.image = ImageIO.read(new File(url));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * �ж��Ƿ�����ײ
	 * 
	 * @param v - Ŀ��ͼƬ����
	 * @return ��������ཻ������true�����򷵻�false
	 */
	public boolean hit(VisibleImage v) {
		return hit(v.getBounds());// ִ�����ط���
	}

	/**
	 * �ж��Ƿ�����ײ
	 * 
	 * @param r - Ŀ��߽�
	 * @return ��������ཻ������true�����򷵻�false
	 */
	public boolean hit(Rectangle r) {
		if (r == null) {
			return false; // ��������ײ
		}
		return getBounds().intersects(r); // �жϱ߽�����Ƿ��ཻ
	}

	/**
	 * ��ȡ�߽����
	 */
	public Rectangle getBounds() {
		// ����һ�������ڣ�x,y��λ�ã����Ϊ��width,height���ľ��α߽���󲢷���
		return new Rectangle(x, y, width, height);
	}

	/**
	 * setter,getter������ȡ�����ó����
	 */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "VisibleImage [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
