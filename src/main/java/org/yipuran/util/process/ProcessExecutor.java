package org.yipuran.util.process;

import java.io.IOException;

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
    * </pre>
    * @param outs 省略可能パラメータ、プロセス側が求める標準入力に入力する文字列、順に入力される。
    * @return プロセス終了コード、Process.exitValue() のコードを返す。
    * @throws IOException
    * @throws InterruptedException
    */
   public int exec(String...outs) throws IOException,InterruptedException;
   
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
}
