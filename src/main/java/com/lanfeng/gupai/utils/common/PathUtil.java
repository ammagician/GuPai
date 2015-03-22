package com.lanfeng.gupai.utils.common;

import java.io.File;

public class PathUtil {
	private static String classPath;
	private static String resPath;

	public static String getClassPath() {
		if (classPath == null) {
			classPath = PathUtil.class.getResource("/").getPath();
			if (classPath.contains("test-classes")) {
				classPath = classPath.replace("test-classes", "classes");
			}
		}

		return classPath;
	}

	public static String getResourcePath() {
		return getClassPath();
	}

	public static void deleteFolder(String folderName) {
		File folder = new File(folderName);
		if (!folder.exists()) {
			return;
		}

		deleteAll(folder);
	}

	private static void deleteAll(File file) {
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File file2 : files) {
				deleteAll(file2);
			}

			if (file.exists()) {
				file.delete();
			}
		}
	}
}
