package bocang.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import bocang.json.JSONObject;

/**
 * 配置文件的读写<br />
 * 初始化时需设定CONFIG_DIRECTORY<br />
 * 
 * @author adam
 * 
 */
public class AppConfigFile {

	/**
	 * 配置文件目录
	 */
	public static String CONFIG_DIRECTORY = null;

	/**
	 * 数据
	 */
	public JSONObject data;

	private String fileName = null;

	public AppConfigFile() {
		fileName = "IssueApplication";
		data = open();
	}

	public AppConfigFile(String fileName) {
		this.fileName = fileName;
		data = open();
	}

	private JSONObject open() {

		// 变量.定义
		File file = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		String text = null;

		// 变量.初始化
		sb.append(CONFIG_DIRECTORY);
		sb.append('/');
		sb.append(fileName);
		sb.append(".json");

		// 处理.
		file = new File(sb.toString());
		if (!file.exists()) {
			return new JSONObject();
		}

		try {
			reader = new BufferedReader(new FileReader(file));

			while ((text = reader.readLine()) != null) {
				content.append(text).append(
						System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			// ignore
		} catch (IOException e) {
			// ignore
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// ignore
			}
		}

		if (content.length() == 0) {
			return new JSONObject();
		} else {
			return new JSONObject(content.toString());
		}

	}

	public synchronized void save() {

		//
		StringBuilder sb = new StringBuilder();
		File file = null;
		FileWriter fw = null;

		try {
			sb.append(CONFIG_DIRECTORY);
			sb.append('/');
			sb.append(fileName);
			sb.append(".json");

			file = new File(sb.toString());
			fw = new FileWriter(file, false);

			fw.append(data.toString());

			fw.flush();
		} catch (IOException e) {
			// ignore
			// e.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

	}

}
