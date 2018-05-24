package org.yipuran.file;

import java.io.File;

/**
 * ファイル有無ロック.
 * <pre>
 * 排他スコープを JVM 内に限定させす、同一プラットフォームを排他のスコープとする
 * 排他制御機能を提供する。同一プラットフォームで動作する異なるJVM の処理で排他制御を行う為、
 * プラットフォームに、ロック用のファイルを生成しファイルの存在有無で排他制御する。
 *
 * begin 呼び出しと end 呼び出しで排他をかけたい処理を囲む。
 * new 演算子でインスタンス生成して、begin 呼び出しと end 呼び出しを行う！
 * （使用例）
 *     FilebaseLock s = new FilebaseLock(filepath);
 *     try{
 *     boolean b = s.begin(60);  // 最大 60 秒 待つ。 引数なしの begin(); 実行なら、永久に待つ！！
 *          // true=ロック成功、false=ロック失敗
 *       :  //対象処理
 *     }finally{
 *        s.end();
 *     }
 * </pre>
 */
public final class FilebaseLock{
	private File lockFile;
	/**
	 * コンストラクタ.
	 * @param filepath ロックファイル生成path を指定する
	 */
	public FilebaseLock(String filepath){
		this.lockFile = new File(filepath);
	}
	/**
	 * 排他開始.
	 * <br/> ロックファイル生成できるまで待たされる。begin(0) 実行と同じ効果があります。
	 * @return true ロック成功
	 */
	public boolean begin(){
		return this.begin(0);
	}
	/**
	 * 排他開始.
	 * <pre>
	 * 引数に排他用ロックファイル生成するまでの最大待ち合わせ時間（秒）指定します。
	 * 待ち合わせ時間を経過しても排他用ロックファイルを生成できない時、
	 * 既に他の処理が排他を開始して終了していない時は、false を返します。
	 * 待ち合わせ時間に、0 以下の値を指定すると排他用ロックファイルを生成できるまで待たされます。
	 * </pre>
	 * @param maxSec 排他最大待ち合わせ時間を秒数で指定する
	 * @return true＝ロック成功、false＝ロック失敗
	 */
	public synchronized boolean begin(int maxSec){
		try{
			for(int n=maxSec;0 < n || maxSec < 1;n--){
			   if (this.lockFile.createNewFile()) return true;
			   try{Thread.sleep(1000);}catch(InterruptedException e){}
			}
		}catch(Exception e){
		}finally{
		}
return false;
	}
	/**
	 * 排他終了.
	 * <pre>
	 * begin(),begin(int) を実行したインスタンスで呼び出さなくてはなりません。
	 * </pre>
	 */
	public void end(){
		if (this.lockFile.exists()){
			this.lockFile.delete();
		}
	}
}
