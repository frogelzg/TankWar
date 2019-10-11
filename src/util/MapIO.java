package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 地图数据工具
 * 
 * @author asus-pc
 *
 */
public class MapIO {
	// 地图数据文件路径
	public final static String DATA_PATH = "map/data/";
	// 地图预览图路径
	public final static String IMAGE_PATH = "map/image/";
	// 地图数据文件后缀
	public final static String DATA_SUFFIX = ".map";
	// 地图预览图后缀
	public final static String IMAGE_SUFFIX = ".jpg";

	/**
	 * 获取指定名称地图的所有墙块集合
	 */
	public static List<Wall> readMap(String mapName) {
		// 创建对应名称的地图文件
		File file = new File(DATA_PATH + mapName + DATA_SUFFIX);
		return readMap(file);
	}

	/**
	 * 获取地图文件的所有墙块集合
	 * 
	 * @param file -地图文件
	 * @return 此地图中的所有墙块集合
	 */
	public static List<Wall> readMap(File file) {
		Properties pro = new Properties();// 创建属性集对象
		List<Wall> walls = new ArrayList<>();// 创建总墙块集合
		try {
			pro.load(new FileInputStream(file));// 属性集对象读取地图文件
			String brickStr = (String) pro.get(WallType.brick.name());// 砖墙
			String grassStr = (String) pro.get(WallType.grass.name());// 草地
			String riverStr = (String) pro.get(WallType.river.name());// 河流
			String ironStr = (String) pro.get(WallType.iron.name());// 铁墙
			if (brickStr != null) {
				walls.addAll(readWall(brickStr, WallType.brick));// 解析数据，将数据中的墙块集合添加到总墙块集合中
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
			return walls;// 返回总墙块集合
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析墙块数据
	 * 
	 * @param data - 墙块坐标字符串
	 * @param type - 墙块类型
	 * @return 墙块集合
	 */
	private static List<Wall> readWall(String data, WallType type) {
		String walls[] = data.split(";"); // 使用 ; 分割字符串
		Wall wall; // 创建墙块对象
		List<Wall> w = new Linkedlist<>();// 创建墙块集合
		switch (type) {// 判断类型
		case brick: // 如果是砖墙
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// 创建墙块对象，分割的第一个值为横坐标，第二个为纵坐标
				wall = new BrickWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case river: // 如果是河流
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// 创建墙块对象，分割的第一个值为横坐标，第二个为纵坐标
				wall = new RiverWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case grass: // 如果是草地
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// 创建墙块对象，分割的第一个值为横坐标，第二个为纵坐标
				wall = new GrassWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		case iron: // 如果是河流
			for (String wStr : walls) {
				String axes[] = wStr.split(",");
				// 创建墙块对象，分割的第一个值为横坐标，第二个为纵坐标
				wall = new IronWall(Integer.parseInt(axes[0]), Integer.parseInt(axes[1]));
				w.add(wall);
			}
			break;
		}
		return w;// 返回墙块集合
	}
}
