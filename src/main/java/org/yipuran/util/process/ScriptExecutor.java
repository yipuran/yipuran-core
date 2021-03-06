package org.yipuran.util.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * ラムダ・スクリプト実行.
 * <PRE>
 * プロセスとして実行するスクリプトをラムダ式、Supplier によるスクリプト実行から
 *  BiConsumer で結果標準出力と標準エラー出力を処理する。
 * 例外捕捉は全て標準エラー出力として受け取る。
 * java.lang.Process の exitValue() 結果をメソッドの戻り値とする。
 *
 * BiConsumer は、標準出力、標準エラー出力を String で受け取る実行方法と
 * InputStream(byte単位のストリーム）で受け取る実行方法の２通りのメソッドがある。
 *
 * BiConsumer のラムダが実行された後に、Process 実行結果を得る＝すなわちメソッドが返ってくる。
 *
 * （Linux での Pythonスクリプト 実行例）
 *      int sts = ScriptExecutor.run(()-&gt;"python /usr/local/app/myapp.py", t->{
 *         System.out.println("stdout : " + t );
 *      }, (t, x)-&gt;{
 *         System.out.println("stderr : " + t );
 *         x.printStackTrace();
 *      });
 *
 * （Windows 環境での Pythonスクリプト 実行例）
 *      int sts = ScriptExecutor.run(()-&gt;"cmd.exe /C python c:/User/app/myapp.py", t->{
 *         System.out.println("stdout : " + t );
 *      }, (t, x)-&gt;{
 *         System.out.println("stderr : " + t );
 *         x.printStackTrace();
 *      });
 *
 *   スクリプト起動後、入力を必要とするスクリプトを実行する場合は、全ての入力テキストを与える Supplier&lt;Collection&lt;String&gt;&gt;
 *   か、入力テキストを読み取る InputStream を指定して実行する。
 *   （Windows 例）
 *       int sts = ScriptExecutor.run(()-&gt;"cmd.exe /C python"
 *       , ()->Arrays.asList("print(\"Hello Python\")\n", "exit()\n"), t->{
 *            System.out.println("stdout : " + t );
 *       }, (t, x)-&gt;{
 *            System.out.println("stderr : " + t );
 *            x.printStackTrace();
 *       });
 *   （Linux 例）
 *       int sts = ScriptExecutor.run(()-&gt;"python"
 *       , ()->Arrays.asList("print(\"Hello Python\")\n", "exit()\n"), t->{
 *       	 System.out.println("stdout : " + t );
 *       }, (t, x)-&gt;{
 *           System.out.println("stderr : " + t );
 *           x.printStackTrace();
 *       });
 *   入力を必要とするスクリプトを実行する場合、スクリプトの言語によっては、各処理行の終了、改行コードが必要など、
 *   スクリプトがきちんと終了すること、注意しなければならない。
 *
 *   Python 実行、Python スクリプトが標準入力を求めている場合、
 *   　起動後入力有り、ラムダ・スクリプト実行（標準出力結果→String）
 *   　public static int run(Supplier&lt;String&gt; , Supplier&lt;Collection&lt;String&gt;&gt; , Consumer&lt;String&gt; , BiConsumer&lt;String, Throwable&gt; )
 *   　
 *   　起動後入力有り、ラムダ・スクリプト実行（標準出力結果→InputStream）
 *   　public static int runStream(Supplier&lt;String&gt; , Supplier&lt;Collection&lt;String&gt;&gt; , Consumer&lt;InputStream&gt; , BiConsumer&lt;String, Throwable&gt; )
 *
 *   を使用するが、起動後入力する文字列が２バイト文字を含む場合、Unicode 変換した文字列を渡して
 *   Pythonスクリプト側で、encode().decode('unicode-escape') で受け取る必要がある。
 *   Unicode 変換は、org.yipuran.util.SJutil の toUnicode(String) メソッドを使用すると便利
 *
 * Windowsで "cmd.exe /C " を先頭に付与しないのが通常だが、
 * *.EXEのコマンドとして認識されない、*.bat 等は、先頭に "cmd.exe /c "等、cmd.exe を付与した文字列を返す必要がある。
 * </PRE>
 */
public final class ScriptExecutor{

	private ScriptExecutor(){}

	/**
	 * ラムダ・スクリプト実行（標準出力結果→String）
	 * @param scriptSupplier 実行するスクリプトを返すSupplier
	 * @param consumer 正常終了時の Consumer → 標準出力
	 * @param error 異常終了時の BiConsumer → 標準エラー出力とThrowable
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常。
	 */
	public static int run(Supplier<String> scriptSupplier, Consumer<String> consumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String stdout;
		String stderr;
		try{
			Process p = Runtime.getRuntime().exec(scriptSupplier.get());
			//Process p = new ProcessBuilder(scriptSupplier.get()).start();
			_processStreamReader p_stderr = new _processStreamReader(p.getErrorStream());
			_processStreamReader p_stdout = new _processStreamReader(p.getInputStream());
			p_stderr.start();
			p_stdout.start();
			p_stderr.join();
			p_stdout.join();
			p.waitFor();
			rtn = p.exitValue();
			stdout = p_stdout.getString();
			stderr = p_stderr.getString();
			if (stderr==null || stderr.isEmpty()){
				consumer.accept(stdout);
			}else{
				error.accept(stderr, new Exception(stderr));
			}
		}catch(Exception ex){
			rtn = 1;
			stdout = "";
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
			stderr = sb.toString();
			error.accept(stderr + ex.getMessage(), ex);
		}
		return rtn;
	}
	/**
	 * ラムダ・スクリプト実行（標準出力結果→InputStream）
	 * @param scriptSupplier 実行するスクリプト
	 * @param consumer 正常終了時の Consumer → InputStream
	 * @param error 異常終了時の BiConsumer → 標準エラー出力 StringとThrowable
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常。
	 */
	public static int runStream(Supplier<String> scriptSupplier, Consumer<InputStream> consumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String stderr;
		try{
			Process p = Runtime.getRuntime().exec(scriptSupplier.get());
			//Process p = new ProcessBuilder(scriptSupplier.get()).start();
			_processStreamReader p_stderr = new _processStreamReader(p.getErrorStream());
			consumer.accept(p.getInputStream());
			p_stderr.start();
			p_stderr.join();
			p.waitFor();

			rtn = p.exitValue();
			stderr = p_stderr.getString();
			if (stderr !=null && !stderr.isEmpty()){
				error.accept(stderr, new Exception(stderr));
			}
		}catch(Exception ex){
			rtn = 1;
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
			stderr = sb.toString();
			error.accept(stderr + ex.getMessage(), ex);
		}
		return rtn;
	}

	/**
	 * 起動後入力有り、ラムダ・スクリプト実行（標準出力結果→String）.
	 * @param scriptSupplier 実行するスクリプト
	 * @param inputSupplier 起動後、スクリプトが求める入力テキストを Collection&lt;String&gt; で提供するSupplier
	 * @param consumer 正常終了時の Consumer → 標準出力
	 * @param error 異常終了時の BiConsumer → 標準エラー出力とThrowable
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常
	 */
	public static int run(Supplier<String> scriptSupplier, Supplier<Collection<String>> inputSupplier, Consumer<String> consumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String stdout;
		String stderr;
		try{
			Process p = Runtime.getRuntime().exec(scriptSupplier.get());
			//Process p = new ProcessBuilder(scriptSupplier.get()).start();
			_processStreamReader p_stderr = new _processStreamReader(p.getErrorStream());
			_processStreamReader p_stdout = new _processStreamReader(p.getInputStream());
			try(PrintWriter pw = new PrintWriter(p.getOutputStream())){
				inputSupplier.get().stream().forEach(s->{
					pw.print(s);
					pw.flush();
				});
			}
			p_stderr.start();
			p_stdout.start();
			p_stderr.join();
			p_stdout.join();
			p.waitFor();
			rtn = p.exitValue();
			stdout = p_stdout.getString();
			stderr = p_stderr.getString();
			if (stderr==null || stderr.isEmpty()){
				consumer.accept(stdout);
			}else{
				error.accept(stderr, new Exception(stderr));
			}
		}catch(Exception ex){
			rtn = 1;
			stdout = "";
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
			stderr = sb.toString();
			error.accept(stderr + ex.getMessage(), ex);
		}
		return rtn;
	}
	/**
	 * 起動後入力有り、ラムダ・スクリプト実行（標準出力結果→InputStream）.
	 * @param scriptSupplier 実行するスクリプト
	 * @param consumer 正常終了時の Consumer → InputStream
	 * @param error 異常終了時の BiConsumer → 標準エラー出力 StringとThrowable
	 * @return java.lang.Process の exitValue() 結果、例外発生時は 1 を返す。
	 */
	public static int runStream(Supplier<String> scriptSupplier, Supplier<Collection<String>> inputSupplier , Consumer<InputStream> consumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String stderr;
		try{
			Process p = Runtime.getRuntime().exec(scriptSupplier.get());
			//Process p = new ProcessBuilder(scriptSupplier.get()).start();
			_processStreamReader p_stderr = new _processStreamReader(p.getErrorStream());
			try(PrintWriter pw = new PrintWriter(p.getOutputStream())){
				inputSupplier.get().stream().forEach(s->{
					pw.print(s);
					pw.flush();
				});
			}
			consumer.accept(p.getInputStream());
			p_stderr.start();
			p_stderr.join();
			p.waitFor();

			rtn = p.exitValue();
			stderr = p_stderr.getString();
			if (stderr !=null && !stderr.isEmpty()){
				error.accept(stderr, new Exception(stderr));
			}
		}catch(Exception ex){
			rtn = 1;
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
			stderr = sb.toString();
			error.accept(stderr + ex.getMessage(), ex);
		}
		return rtn;
	}
	//---------------
	static class _processStreamReader extends Thread{
		StringBuffer        sb;
		InputStreamReader   inredaer;
		public _processStreamReader(InputStream in){
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
