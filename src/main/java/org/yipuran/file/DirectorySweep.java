package org.yipuran.file;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ディレクトリ過去ファイル削除.
 * <pre>
 * jcron.xml にスケジュール実行クラスとして登録して使用する
 * </pre>
 * @since 1.0.0
 */
public class DirectorySweep{
	/**
	 * Old log delete
	 * @param args String[]下記のとおり、<br>
	 * [0] = 期間（日数）
	 * [1] = 対象ディレクトリ
	 * [n] = 消さないファイル名: n > 2
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException{
		int keep = Integer.parseInt(args[0]);
		SimpleDateFormat sdateForm = new SimpleDateFormat("yyyyMMdd");
		Date nowDate = sdateForm.parse(sdateForm.format(new Date()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		cal.add(Calendar.HOUR,keep * 24 * -1);
		Date chkDt = cal.getTime();
		File logDir = new File(args[1]);
		Map<String,String> map = new HashMap<String,String>();
		for(int i=2;i < args.length;i++){
			map.put(args[i],args[i]);
		}
		File[] targetFiles = logDir.listFiles();
		for(int i=0;i < targetFiles.length;i++){
			if (!map.containsKey(targetFiles[i].getName())){
				Date ldt = new Date(targetFiles[i].lastModified());
				if (chkDt.compareTo(ldt) > 0){
					targetFiles[i].delete();
				}
			}
		}
	}

}
