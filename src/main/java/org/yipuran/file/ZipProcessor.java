package org.yipuran.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.yipuran.function.ThrowableConsumer;

/**
 * ZIP 圧縮／解凍プロセッサ.
 * <PRE>
 * （規則）
 *     ZIPファイル名拡張子 → .zip
 * （圧縮）
 * Supplier<Collection<FileCollection>> = ファイルコレクション（FileCollection）で指定する対象を
 * Collection<String> compress(String targzPath) で圧縮する。
 * メソッド戻り値は、tarエントリ名 Collection
 * （展開）
 * void decompress(String zipPath, String dirPath) で展開する。
 * @since 1.1
 *  </PRE>
 */
public interface ZipProcessor extends Supplier<Collection<FileCollection>>{

	/**
	 * ZIP 圧縮実行.
	 * <PRE>
	 * Supplier で渡す FileCollection の渡し方で単一か複数か決まる。
	 * 例１）
	 *    // targetPath配下を圧縮対象にする場合
	 *    List<FileCollection> fileCollections =
	 *    Arrays.stream(new File(targetPath).listFiles()).map(e->FileCollection.of(e.getAbsolutePath())).collect(Collectors.toList());
	 *    ZipProcessor processor = ()->fileCollections;
	 *    Collection<String> entries = processor.compress(zipPath);
	 *
	 * 例２）
	 *    // １つのディレクトリツリーで圧縮
	 *    FileCollection fileCollection = FileCollection.of(targetPath);
	 *    ZipProcessor processor = ()->Arrays.asList(fileCollection);
	 *    Collection<String> entries = processor.compress(zipPath);
	 *
	 * </PRE>
	 * @param zipPath 作成する ZIPファイルパス、 *.zip
	 * @return ZIPエントリ名 Collection
	 */
	public default Collection<String> compress(String zipPath){
		Collection<String> entries = new ArrayList<>();
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(zipPath)))){
			get().forEach(fc->{
				String prefix = fc.getFile().getParentFile().getAbsolutePath().replaceAll("\\\\", "/");
				fc.scan(ThrowableConsumer.of(f->{
					String entryName = f.getAbsolutePath().replaceAll("\\\\", "/").replaceFirst(prefix, "");
					ZipEntry entry = new ZipEntry(f.isDirectory() ? entryName + "/" : entryName);
					entries.add(entry.getName().charAt(0)=='/' ? entry.getName().substring(1) : entry.getName());
					zos.putNextEntry(entry);
					if (f.isFile()){
						try(FileInputStream fis = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(fis)){
							int size = 0;
							byte[] buf = new byte[1024];
							while((size = bis.read(buf)) > 0){
								zos.write(buf, 0, size);
							}
						}
					}
				}));
			});
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
		return entries;
	}
	/**
	 * zip 圧縮実行（対象制限）.
	 * <PRE>
	 * Predicate<File> で、tar作成対象を制限する。任意ディレクトリパスなど制限するために使用する。
	 * </PRE>
	 * @param zipPath 作成する zip ファイルパス、 *.zip
	 * @param p Predicate<File>制限規則の付与
	 * @return ZIPエントリ名 Collection
	 */
	public default Collection<String> compress(String zipPath, Predicate<File> p){
		Collection<String> entries = new ArrayList<>();
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(zipPath)))){
			get().forEach(fc->{
				String prefix = fc.getFile().getParentFile().getAbsolutePath().replaceAll("\\\\", "/");
				fc.scan(p, ThrowableConsumer.of(f->{
					String entryName = f.getAbsolutePath().replaceAll("\\\\", "/").replaceFirst(prefix, "");
					ZipEntry entry = new ZipEntry(f.isDirectory() ? entryName + "/" : entryName);
					entries.add(entry.getName().charAt(0)=='/' ? entry.getName().substring(1) : entry.getName());
					zos.putNextEntry(entry);
					if (f.isFile()){
						try(FileInputStream fis = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(fis)){
							int size = 0;
							byte[] buf = new byte[1024];
							while((size = bis.read(buf)) > 0){
								zos.write(buf, 0, size);
							}
						}
					}
				}));
			});
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
		return entries;
	}
	/**
	 * ZIPファイル展開.
	 * @param zipPath ZIPファイルパス
	 * @param dirPath 展開先ディレクトリパス
	 * @return Set<String> ZIPエントリ名 Collection
	 */
	public static Set<String> decompress(String zipPath, String dirPath){
		TreeSet<String> entries = new TreeSet<>();
		// 展開
		try(ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))){
			ZipEntry entry;
			while((entry = zin.getNextEntry()) != null){
				entries.add(entry.getName());
				if (entry.isDirectory()){
					String s = entry.getName();
					new File(dirPath + "/" + s.substring(0, s.length()-1)).mkdir();
				}else{
					String[] d = entry.getName().split("/");
					String dir = dirPath;
					for(int i=0;i < d.length-1;i++){
						dir += "/" + d[i];
						new File(dir).mkdir();
					}
					try(FileOutputStream fos = new FileOutputStream(dirPath+"/"+entry.getName()); BufferedOutputStream bos = new BufferedOutputStream(fos)){
						int size = 0;
						byte[] buf = new byte[1024];
						while((size = zin.read(buf)) > 0){
							bos.write(buf, 0, size);
						}
					}
				}
			}
		}catch(IOException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return entries;
	}
	/**
	 * エントリ名コレクション.
	 * @param zipPath ZIPファイルパス、 *.zip
	 * @return Set<String> ZIPエントリ名 Collection
	 */
	public static Set<String> viewPath(String zipPath){
		TreeSet<String> entries = new TreeSet<>();
		try(ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))){
			ZipEntry entry;
			while((entry = zin.getNextEntry()) != null){
				entries.add(entry.getName());
			}
		}catch(IOException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return entries;
	}
}
