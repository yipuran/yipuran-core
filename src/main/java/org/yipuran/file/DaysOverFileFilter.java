package org.yipuran.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * DaysOverFileFilter  更新日数 Over ファイル名フィルタ.
 * java.io.File#list(filter) で古いファイルリストを求めるフィルタ
 */
public class DaysOverFileFilter implements FilenameFilter{
	private Pattern pattern;
	private Date limitDt;
	/**
	 * コンストラクタ
	 * @param regex 対象ファイル名正規表現
	 * @param days 日数、（現在時刻 - days）> lastModified で抽出
	 */
	public DaysOverFileFilter(String regex,int days){
		this.pattern = Pattern.compile(regex);
		Calendar c = Calendar.getInstance();
		int d = days < 0 ? days : days * -1;
		c.add(Calendar.DAY_OF_MONTH,d);
		this.limitDt = c.getTime();
	}
	/* (非 Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir,String name){
		File f = new File(dir,name);
		if (f.isDirectory()) return false;
		if (this.pattern.matcher(name).matches()){
			return this.limitDt.compareTo(new Date(f.lastModified())) > 0 ? true : false;
		}
		return false;
	}
}
