package org.yipuran.util.process;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yipuran.file.FilenameFilterImpl;

/**
 * クラスユーティリティ.
 * <pre>
 * クラス名の検索等のメソッドを提供
 * </pre>
 */
public final class ClassUtil{
	/** private constructor. */
	private ClassUtil(){}

	/**
	 * クラス名の検索.
	 * <pre>
	 * 指定パッケージ配下の className を検索する
	 * package名 を指定して、配下のクラス名（インナークラスを除く）を検索する。
	 * </pre>
	 * @param packagename package名
	 * @return クラス名配列
	 */
	public static String[] findClass(String packagename){
		List<String> list = new ArrayList<String>();
		if (packagename==null || packagename.length() < 0) return list.toArray(new String[]{});
		try{
			String target = packagename.replaceAll("\\.", "/");
			if (!target.endsWith("/")) target = target + "/";
			URL url = ClassLoader.getSystemClassLoader().getResource(target);
			if (url.getProtocol().equals("jar")){
				String path = url.toString().replaceFirst("jar:file:", "");
				path = path.substring(0, path.indexOf("!"));
				try(JarFile jarfile = new JarFile(new File(path))){
					for(Enumeration<JarEntry> en = jarfile.entries(); en.hasMoreElements();){
						JarEntry entry = en.nextElement();
						String s = entry.getName();
						if (s.startsWith(target) && s.indexOf("$") < 0 && s.endsWith(".class")){
							list.add(entry.getName().replaceAll("\\.class$", "").replaceAll("/", "."));
						}
					}
				}
			}else if (url.getProtocol().equals("file")){
				String pname = target.replaceAll("/", ".");
				File f = new File(url.toURI());
				String[] fary = f.list(new FilenameFilterImpl("^\\w+.class$"));
				for(String fname : fary){
				   list.add(pname + fname.replaceAll("\\.class$", ""));
			   }
			}
		}catch(Exception e){
		}
		return list.toArray(new String[]{});
	}
}
