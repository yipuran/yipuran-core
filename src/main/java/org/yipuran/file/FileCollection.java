package org.yipuran.file;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * ファイルコレクション.
 * <PRE>
 * ファイルツリー操作→コレクション
 * （注意）シンボリックリンクが存在する場合、通常に解釈される。
 * </PRE>
 */
public final class FileCollection implements Serializable{
	private File file;
	private FileCollection(String path){
		file = new File(path);
	}
	/**
	 * インスタンス取得.
	 * @param path コレクションするファイルパス
	 * @return FileCollection
	 */
	public static FileCollection of(String path){
		return new FileCollection(path);
	}
	/**
	 * インスタンスが示す収集対象のFile参照.
	 * @return file 収拾対象のFile
	 */
	public File getFile(){
		return file;
	}
	/**
	 * 走査実行→コレクション取得.
	 * @return List&lt;File&gt;
	 */
	public List<File> scan(){
		return parse(file, new ArrayList<>());
	}
	/**
	 * Predicate走査実行→コレクション取得.
	 * @param p 検査するPredicate&lt;File&gt;
	 * @return List&lt;File&gt;
	 */
	public List<File> scan(Predicate<File> p){
		return parse(file, new ArrayList<>(), p);
	}
	/**
	 * 走査 Consumer実行.
	 * @param c Consumer&lt;File&gt;
	 */
	public void scan(Consumer<File> c){
		parse(file, c);
	}
	/**
	 * Predicate走査 Consumer実行.
	 * @param p 検査するPredicate&lt;File&gt;
	 * @param c Consumer&lt;File&gt;
	 */
	public void scan(Predicate<File> p, Consumer<File> c){
		parse(file, p, c);
	}
	/**
	 * Predicate検査一致の成否.
	 * @param p 検査するPredicate&lt;File&gt;
	 * @return true=１つ以上一致するものがある。false=一致するものが存在しない。
	 */
	public boolean anyMatch(Predicate<File> p){
		return findmatch(file, p);
	}

	private List<File> parse(File file, List<File> list){
		list.add(file);
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				parse(f, list);
			}
		}
		return list;
	}
	private void parse(File file, Consumer<File> c){
		c.accept(file);
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				parse(f, c);
			}
		}
	}
	private List<File> parse(File file, List<File> list, Predicate<File> p){
		if (p.test(file)) list.add(file);
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				parse(f, list, p);
			}
		}
		return list;
	}
	private void parse(File file, Predicate<File> p, Consumer<File> c){
		if (p.test(file)) c.accept(file);
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				parse(f, p, c);
			}
		}
	}
	private boolean findmatch(File file, Predicate<File> p){
		if (p.test(file)) return true;
		if (file.isDirectory()){
			for(File f:file.listFiles()){
				if (findmatch(f, p)) return true;
			}
		}
		return false;
	}
}
