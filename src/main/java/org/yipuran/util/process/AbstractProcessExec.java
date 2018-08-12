package org.yipuran.util.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * プロセス起動抽象クラス.
 * <pre>
 * Java から、コマンド、バッチをプロセスとして起動する為の抽象クラスで、実装するクラスで
 * 実行するスクリプトを返すメソッド実装が約束されている。
 * 実行後の終了コード、標準入力、標準出力、標準エラー出力を取得するメソッドが
 * インターフェース org.yipuran.util.ProcessExecutor で用意されており、
 * AbstractProcessExec は、ProcessExecutor をimplementsしています
 * 【使い方】
 *     実装クラスを用意、sample.bat を起動する場合、
 *
 *     public class Aprocess extends AbstractProcessExec{
 *         ＠Override
 *         public String arrange(){
 *                return "cmd.exe /c sample.bat param1 param2";
 *             }
 *         }
 *     }
 *     呼び出し側
 *         // new もしくは、DI で、具象クラスを生成
 *         ProcessExecutor  a = new Aprocess();
 *         // 実行
 *         int sts = a.exec();
 *         // 標準出力結果
 *         String stdout = a.getStdout();
 *         // 標準エラー出力結果
 *         String stderr = a.getStderr();
 *
 * </pre>
 */
public abstract class AbstractProcessExec implements ProcessExecutor{
	private String stdout;
	private String stderr;

	/**
	 * 起動スクリプトを準備する.
	 * <pre>
	 * 具象化クラスで、プロセス起動のスクリプトを返すようにする。
	 * Windows の場合、*.EXEのコマンドとして認識されない、*.bat 等は、
	 * 先頭に "cmd.exe /c "等、cmd.exe を付与した文字列を返す必要がある。
	 * </pre>
	 * @return 起動スクリプト
	 */
	public abstract String arrange();


	/* (非 Javadoc)
	 * @see org.yipuran.util.ProcessExecutor#exec(java.lang.String[])
	 */
	@Override
	public int exec(String...outs){
		int rtn = 0;
   	try{
			Process p = Runtime.getRuntime().exec(this.arrange());
			_ProcessStreamReader p_stderr = new _ProcessStreamReader(p.getErrorStream());
			_ProcessStreamReader p_stdout = new _ProcessStreamReader(p.getInputStream());
			if (outs.length > 0){
			// 入力がある場合
				try(PrintWriter pw = new PrintWriter(p.getOutputStream())){
					for(int i=0;i < outs.length;i++){
						pw.print(outs[i]);
						pw.flush();
					}
				}
			}
			p_stderr.start();
			p_stdout.start();
			p_stderr.join();
			p_stdout.join();
			p.waitFor();
			rtn = p.exitValue();
			this.stdout = p_stdout.getString();
			this.stderr = p_stderr.getString();
		}catch(Exception ex){
			rtn = 1;
			this.stdout = "";
			StringBuilder sb = new StringBuilder();
			sb.append(ex.getMessage());
			sb.append("\n");
			sb.append(Arrays.stream(ex.getStackTrace()).map(t->t.toString()).collect(Collectors.joining("\n\t")));
			Optional.ofNullable(ex.getCause()).ifPresent(x->{
				sb.append("\n");
				sb.append("Caused by: ");
				sb.append(x.getMessage());
				sb.append("\n");
				sb.append(Arrays.stream(x.getStackTrace()).map(t->t.toString()).collect(Collectors.joining("\n\t")));
			});
			this.stderr = sb.toString();
		}
		return rtn;
	}
	/* (非 Javadoc)
	 * @see org.yipuran.util.ProcessExecutor#getStdout()
	 */
	@Override
	public String getStdout(){
		return this.stdout;
	}
	/* (非 Javadoc)
	 * @see org.yipuran.util.ProcessExecutor#getStderr()
	 */
	@Override
	public String getStderr(){
		return this.stderr;
	}
	//---------------
	class _ProcessStreamReader extends Thread{
		StringBuffer        sb;
		InputStreamReader   inredaer;
		public _ProcessStreamReader(InputStream in){
			super();
			this.inredaer = new InputStreamReader(in);
			this.sb = new StringBuffer();
		}
		@Override
		public void run(){
			try{
				int i;
				int BUFFER_SIZE = 1024;
				char[] c = new char[BUFFER_SIZE];
				while((i = this.inredaer.read(c,0,BUFFER_SIZE - 1)) > 0){
					this.sb.append(c,0,i);
					if (i < BUFFER_SIZE - 1){ break; }
				}
				this.inredaer.close();
			}catch(IOException e){}
		}
		public String getString(){
			return this.sb.toString();
		}
	}
}

