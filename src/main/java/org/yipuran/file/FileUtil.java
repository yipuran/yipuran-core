package org.yipuran.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtil
 */
public final class FileUtil{
	/** private constructor. */
	private FileUtil(){}

	/**
	 * ディレクトリ指定の特定ファイル移動. （注意：ディレクトリは残る）
	 * @param fromDir 移動元ファイルのディレクトリ、この配下に移動対象が存在すること。
	 * @param fromlist 移動対象の File リスト
	 * @param toDir 移動先ディレクトリ、すでに対象が存在する場合は上書きする。
	 * @return 移動後のファイルの java.nio.file.Path リストを返す。
	 * 移動後のファイルの絶対パス文字列は、Path の toFile().getAbsolutePath() で参照
	 */
	public static List<Path> moveFiles(File fromDir,  List<File> fromlist, File toDir){
		List<Path> results = new ArrayList<>();
		fromlist.stream().filter(e->e.isDirectory())
		.map(e->new File(e.getAbsolutePath().replace(fromDir.getAbsolutePath() + File.separator, "")))
		.forEach(e->new File(toDir.getAbsolutePath() + "/" + e.getPath()).mkdirs());

		fromlist.stream().filter(e->e.isFile())
		.map(e->new File(e.getAbsolutePath().replace(fromDir.getAbsolutePath() + File.separator, "")))
		.forEach(e->{
			try{
				results.add(Files.move(Paths.get(fromDir.getAbsolutePath() + "/" + e.getPath())
					, Paths.get(toDir.getAbsolutePath() + "/" + e.getPath())
					, StandardCopyOption.REPLACE_EXISTING)
				);
			}catch(IOException e1){
				throw new RuntimeException(e1);
			}
		});
		return results;
	}
}
