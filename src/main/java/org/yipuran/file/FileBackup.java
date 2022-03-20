package org.yipuran.file;

import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileBackup  ファイルバックアップ
 * 連番を付与するローテーションバックアップを行う
 * 連番は、'#' 文字が置換される
 */
public class FileBackup{
	private String name;
	private String prefix;
	private String suffix;
	private int rotateMax;

	public FileBackup(String name,String format,int rotateMax){
		this.name = name;
		if (format.indexOf("#") < 0) throw new RuntimeException("format Error '#' required");
		this.rotateMax = rotateMax;
		String[] s = format.split("#");
		if (s.length==1){
			if (format.endsWith("#")){
				this.prefix = s[0];
				this.suffix = "";
			}else{
				this.prefix = "";
				this.suffix = s[0];
			}
		}else{
			this.prefix = s[0];
			this.suffix = s[1];
		}
	}
	public void rotateNum(String targetDir,String backupDir){
		this.rotateNum(new File(targetDir),new File(backupDir));
	}
	public void rotateNum(File targetDir,File backupDir){
		if (this.rotateMax < 1) return;
		if (!targetDir.exists() || !targetDir.isDirectory()) throw new RuntimeException("No such directory "+targetDir.getPath());
		if (!backupDir.exists() || !backupDir.isDirectory()) throw new RuntimeException("No such directory "+backupDir.getPath());
		File targetFile = new File(targetDir.getPath()+"/"+this.name);
		if (!targetFile.exists()) throw new RuntimeException("Not found target file "+targetFile.getPath());
		String[] fnames = backupDir.list(new FilenameFilterImpl("^"+this.prefix+"\\d+"+this.suffix+"$"));
		Map<Integer,String> map = new TreeMap<Integer,String>(new Comparator<Integer>(){
			@Override
			public int compare(Integer i1,Integer i2){
				return i1.compareTo(i2) * -1;
			}
		});
		for(String fname : fnames){
			String i = fname.replaceAll("^"+this.prefix,"").replaceAll(this.suffix+"$","");
			map.put(Integer.parseInt(i),fname);
		}
		Logger logger = LoggerFactory.getLogger(this.getClass());
		String to=null;
		for(Iterator<Integer> it=map.keySet().iterator();it.hasNext();){
			Integer i = it.next();
			String from = map.get(i);
			if (to==null){
				if (i < this.rotateMax){
					Integer n = i + 1;
					File fromFile = new File(backupDir.getPath()+"/"+from);
					File toFile = new File(backupDir.getPath()+"/"+this.prefix+n.toString()+this.suffix);
					if (toFile.exists()) toFile.delete();
					fromFile.renameTo(toFile);
					logger.trace("## backup ## from ["+fromFile.getPath()+"]  to ["+toFile.getPath()+"]");
			   }
			}else{
				File fromFile = new File(backupDir.getPath()+"/"+from);
				File toFile = new File(backupDir.getPath()+"/"+to);
				if (toFile.exists()) toFile.delete();
				fromFile.renameTo(toFile);
				logger.trace("## backup ## from ["+fromFile.getPath()+"]  to ["+toFile.getPath()+"]");
			}
			to = new String(from);
		}
		File fromFile = new File(targetDir.getPath()+"/"+this.name);
		File toFile = new File(backupDir.getPath()+"/"+this.prefix+"1"+this.suffix);
		if (toFile.exists()) toFile.delete();
		fromFile.renameTo(toFile);
		logger.trace("## backup ## from ["+fromFile.getPath()+"]  to ["+toFile.getPath()+"]");
   }
}
