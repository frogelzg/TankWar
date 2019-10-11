package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * ��ͼ���ݹ���
 * 
 * @author asus-pc
 *
 */
public class MapIO {
	// ��ͼ�����ļ�·��
	public final static String DATA_PATH = "map/data/";
	// ��ͼԤ��ͼ·��
	public final static String IMAGE_PATH = "map/image/";
	// ��ͼ�����ļ���׺
	public final static String DATA_SUFFIX = ".map";
	// ��ͼԤ��ͼ��׺
	public final static String IMAGE_SUFFIX = ".jpg";

	/**
	 * ��ȡָ�����Ƶ�ͼ������ǽ�鼯��
	 */
	public static List<Wall> readMap(String mapName) {
		// ������Ӧ���Ƶĵ�ͼ�ļ�
		File file = new File(DATA_PATH + mapName + DATA_SUFFIX);
		return readMap(file);
	}

	/**
	 * ��ȡ��ͼ�ļ�������ǽ�鼯��
	 * 
	 * @param file -��ͼ�ļ�
	 * @return �˵�ͼ�е�����ǽ�鼯��
	 */
	public static List<Wall> readMap(File file) {
		Properties pro = new Properties();// �������Լ�����
		List<Wall> walls = new ArrayList<>();// ������ǽ�鼯��
		try {
			pro.load(new FileInputStream(file));// ���Լ������ȡ��ͼ�ļ�
			String brickStr = (String) pro.get(WallType.brick.name());// שǽ
			String grassStr = (String) pro.get(WallType.grass.name());// �ݵ�
			String riverStr = (String) pro.get(WallType.river.name());// ����
			String ironStr = (String) pro.get(WallType.iron.name());// ��ǽ
			if (brickStr != null) {
				walls.addAll(readWall(brickStr, WallType.brick));// �������ݣ��������е�ǽ�鼯����ӵ���ǽ�鼯����
			}
			if (grassStr != null) {
				walls.addAll(readWall(grassStr, WallType.grass));
			}
			if (riverStr != null) {
				walls.addAll(readWall(riverStr, WallType.river));
			}
			if (ironStr != null) {
				walls.addAll(readWall(ironStr, WallType.iron));
			}
			return walls;// ������ǽ�鼯��
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����ǽ������
	 * 
	 * @param data - ǽ�������ַ���
	 * @param type - ǽ������
	 * @return ǽ�鼯��
	 */
	private static List<Wall> readWall(String data, WallType type) {
		String walls[] = data.split(";"); // ʹ�� ; �ָ��ַ���
		Wall wall; // ����ǽ�����
		List<Wall> w = new Linkedlist<>();// ����ǽ�鼯��
		switch (type) {// �ж�����
		case brick: // �����שǽ
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ڶ���Ϊ������
				wall = new BrickWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case river: // ����Ǻ���
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ڶ���Ϊ������
				wall = new RiverWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case grass: // ����ǲݵ�
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ڶ���Ϊ������
				wall = new GrassWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case iron: // ����Ǻ���
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// ����ǽ����󣬷ָ�ĵ�һ��ֵΪ�����꣬�ڶ���Ϊ������
				wall = new IronWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		}
		return w;// ����ǽ�鼯��
	}
}
