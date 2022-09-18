package org.yipuran.util.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * ラムダ・スクリプト実行.
 */
public class ScriptExecutor{
	private Map<String, String> envMap;
	public ScriptExecutor(){}

	public ScriptExecutor(Map<String, String> envMap){
		this.envMap = envMap;
	}

	public void setEnvMap(Map<String, String> envMap){
		this.envMap = envMap;
	}

	/**
	 * ラムダ・スクリプト実行（標準出力結果→String）
	 * @param command 実行するスクリプトコマンド
	 * @param consumer 正常終了時の Consumer → 標準出力
	 * @param error 異常終了時の BiConsumer → 標準エラー出力とThrowable
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常。
	 */
	public static int run(String command, Consumer<String> consumer, Consumer<String> errconsumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String stdout;
		String stderr;
		try{
			ProcessBuilder builder = new ProcessBuilder(command);
			Process p = builder.start();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                while((stdout = br.readLine()) != null) {
                    consumer.accept(stdout);
                }
            }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                while((stderr = br.readLine()) != null) {
                	errconsumer.accept(stderr);
                }
            }
			p.waitFor();
			rtn = p.exitValue();
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
	 * ラムダ・スクリプト実行（標準出力＆標準エラー出力結果Consumer）
	 * @param command 実行するスクリプトコマンド
	 * @param consumer 標準出力＆標準エラー出力結果Consumer
	 * @param error プロセス生成におけるエラーBiConsumer
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常。
	 */
	public int run(String command, Consumer<String> consumer, BiConsumer<String, Throwable> error){
        int rtn = 0;
        String str = null;
        try{
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            if (envMap != null && envMap.size() > 0) {
                Map<String, String> environments = builder.environment();
                envMap.entrySet().stream().forEach(e->environments.put(e.getKey(), e.getValue()));
            }
            Process p = builder.start();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                while((str = br.readLine()) != null) {
                    consumer.accept(str);
                }
            }
            p.waitFor();
            rtn = p.exitValue();
        }catch(Exception ex){
            String stderr;
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
	 * @param command 実行するスクリプトコマンド
	 * @param inputSupplier 起動後、スクリプトが求める入力テキストを Collection&lt;String&gt; で提供するSupplier
	 * @param consumer 正常終了時の Consumer → 標準出力
	 * @param error 異常終了時の BiConsumer → 標準エラー出力とThrowable
	 * @return java.lang.Process の exitValue() 結果、0 = 起動が正常
	 */
	public  int run(String command, Supplier<Collection<String>> inputSupplier, Consumer<String> consumer, BiConsumer<String, Throwable> error){
		int rtn = 0;
		String str = null;
		try{
			ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            if (envMap != null && envMap.size() > 0) {
                Map<String, String> environments = builder.environment();
                envMap.entrySet().stream().forEach(e->environments.put(e.getKey(), e.getValue()));
            }
            Process p = builder.start();
			try(PrintWriter pw = new PrintWriter(p.getOutputStream())){
				inputSupplier.get().stream().forEach(s->{
					pw.print(s);
					pw.flush();
				});
			}
			try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                while((str = br.readLine()) != null) {
                    consumer.accept(str);
                }
            }
			p.waitFor();
			rtn = p.exitValue();

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
			error.accept(sb.toString() + ex.getMessage(), ex);
		}
		return rtn;
	}
}
