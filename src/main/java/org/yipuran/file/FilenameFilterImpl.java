package org.yipuran.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * ファイル名フィルタ.
 * <pre>
 * java.io.File#list(FilenameFilter) を実行するための実装クラス
 * （例）
 *      File f = new File("c:/tmp");
 *      // f のディレクトリで　CSVファイル名を検索
 *      String[] fnames = f.list(new FilenameFilterImpl("^.*\\.csv$"));
 * </pre>
 * @since 1.1.6
 */
public class FilenameFilterImpl implements FilenameFilter{
	private Pattern pattern;
	/**
	 * コンストラクタ.
	 * @param pattern java.util.regex.Pattern#compileを生成するための文字列
	 */
	public FilenameFilterImpl(String pattern){
		this.pattern = Pattern.compile(pattern);
	}
	/***
	 * java.io.FilenameFilter#accept(java.io.File,java.lang.String)の実装
	 * @param dir ディレクトリを指すFile
	 * @param name ファイル名、java.util.regex.Pattern#matcherを実行するための文字列
	 * @return true=ファイルリストに含める
	 */
	@Override
	public boolean accept(File dir,String name){
		File f = new File(dir.getPath()+File.separator+name);
		if (f.isDirectory()) return false;        // ディレクトリは除く
		return this.pattern.matcher(name).matches();
	}

}
