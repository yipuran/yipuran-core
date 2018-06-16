package org.yipuran.csv;

import java.util.List;

import org.yipuran.csv4j.CSVLineProcessor;

/**
 * CsvLineProcessor.
 * <PRE>
 * CsvStreamProcessor ＣＳＶ読込みProcessorで実行する処理 org.yipuran.csv4j.CSVLineProcessor の継承で
 * org.yipuran.csv4j.CSVLineProcessor の processDataLine をラムダで実行する為のインターフェース
 * CsvStreamProcessor ヘッダ無し読込みで使用する。
 *
 * CsvStreamProcessor  processor = new CsvStreamProcessor();
 * processor.readNoheader(new InputStreamReader(in, "UTF-8"), (n, l)->{
 *      // n = 行番号
 *      // l = １行の Line<String>
 *      l.stream().forEach(e->{
 *         System.out.print("[" + e + "]");
 *      });
 * });
 *
 * （注意）空列＝空文字で読み込まれる。
 * </PRE>
 */
@FunctionalInterface
public interface CsvLineProcessor extends CSVLineProcessor{
	@Override
	default public boolean continueProcessing(){
		return true;
	}
	@Override
	default public void processHeaderLine(int linenumber, List<String> fieldNames){
		processDataLine(linenumber, fieldNames);
	}
}
