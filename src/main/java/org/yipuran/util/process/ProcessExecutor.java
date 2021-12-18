package org.yipuran.util.process;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * プロセス起動インターフェース.
 * <pre>
 * Java から、コマンド、バッチをプロセスとして起動する為の抽象クラス AbstractProcessExec が
 * このインターフェースを実装しています。
 * プロセス起動は、起動スクリプトの決定を約束するようにAbstractProcessExecを継承して
 * このインターフェースのプロセスの実行メソッド、execを呼ばなければなりません。
 * </pre>
 */
public interface ProcessExecutor{
   /**
    * プロセスの実行.
    * <pre>
    * AbstractProcessExec が抽象メソッドとして arrange()を約束しており、arrange()メソッドで
    * 返される文字列を起動スクリプトとして実行する。
    * 省略可能パラメータで、プロセス側が求める標準入力に対して任意文字列を与えるように
    * 指定することができる。
    * 例外発生捕捉時はスタックトレースを標準エラー出力するので、getStderr() でスタックトレースを確認する
    * </pre>
    * @param outs 省略可能パラメータ、プロセス側が求める標準入力に入力する文字列、順に入力される。
    * @return プロセス終了コード、Process.exitValue() のコードを返す。
    */
   public int exec(String...outs);

   /**
    * プロセスの実行結果の標準出力を取得
    * @return プロセス完了後の標準出力
    */
   public String getStdout();

   /**
    * 標準エラー出力を取得
    * @return プロセス完了後の標準エラー出力
    */
   public String getStderr();

   /**
    * Python実行エラートレースダンプ Stream変換.
    * <PRE>
    * ScriptExecutor の runメソッド、runStreamメソッドのエラー捕捉処理の BiConsumer＜String, Throwable＞から
    * String ＝ Python実行スクリプトの標準エラー出力文字列が、Python のトレースダンプである時、
    * 本メソッドでトレースダンプ１ステップずつの Stream に変換することができる。
    * </PRE>
    * @param error Pythonスクリプト標準エラー出力結果
    * @return Stream
    * @since 4.30
    */
   public static Stream<String> pythonErrorTrace(String error) {
		String estr = error.replaceAll("\r", "").replaceAll("\n", "");
		estr = estr.substring(2, estr.length()-2);
		String[] ary = estr.split("', '");
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>(){
			int x = -1;
			@Override
			public boolean hasNext(){
				return x < ary.length-1;
			}
			@Override
			public String next(){
				x++;
				return ary[x].replaceFirst("\\\\n$", "");
			}
		}, Spliterator.ORDERED), false);
	}
}
