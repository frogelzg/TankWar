package model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 可显示图像的抽象类
 * 
 * @author asus-pc
 *
 */

public class VisibleImage {
	/**
	 * 横、纵坐标
	 */
	public int x;
	public int y;
	/**
	 * 长、宽
	 */
	int width;
	int height;

	/**
	 * 图像对象
	 */
	BufferedImage image;

	/**
	 * 构造方法
	 * 
	 * @param x      -横坐标
	 * @param y      -纵坐标
	 * @param width  -宽
	 * @param height -高
	 */
	public VisibleImage(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// 实例化图片
	}

	/**
	 * 构造方法
	 * 
	 * @param x   -横坐标
	 * @param y   -纵坐标
	 * @param url -图片路径
	 */
	public VisibleImage(int x, int y, String url) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(new File(url));// 获取此路径的图片对象
			this.width = image.getWidth();
			this.height = image.getHeight();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片
	 * 
	 * @return 所显示的图片
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * 设置图片
	 * 
	 * @param image -所显示的图片
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * 设置图片
	 * 
	 * @param image -所显示的图片
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
	 * 判断是否发生碰撞
	 * 
	 * @param v - 目标图片对象
	 * @return 如果两者相交，返回true，否则返回false
	 */
	public boolean hit(VisibleImage v) {
		return hit(v.getBounds());// 执行重载方法
	}

	/**
	 * 判断是否发生碰撞
	 * 
	 * @param r - 目标边界
	 * @return 如果两者相交，返回true，否则返回false
	 */
	public boolean hit(Rectangle r) {
		if (r == null) {
			return false; // 不发生碰撞
		}
		return getBounds().intersects(r); // 判断边界对象是否相交
	}

	/**
	 * 获取边界对象
	 */
	public Rectangle getBounds() {
		// 创建一个坐标在（x,y）位置，宽高为（width,height）的矩形边界对象并返回
		return new Rectangle(x, y, width, height);
	}

	/**
	 * setter,getter方法获取并设置长宽高
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
