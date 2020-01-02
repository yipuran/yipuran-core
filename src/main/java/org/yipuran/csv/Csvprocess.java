package org.yipuran.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.yipuran.csv4j.CSVReader;
import org.yipuran.csv4j.CSVStreamProcessor;
import org.yipuran.csv4j.ParseException;
import org.yipuran.csv4j.ProcessingException;

/**
 * CSV読込みProcessor.
 * ヘッダ有り読込みとヘッダ無し読込みを提供する。
 * <PRE>
 * ヘッダ有り読込み、(for Windows SJIS CSV)
 * Csvprocess process = new Csvprocess();
 * process.read(new InputStreamReader(in, "MS932")
 * , h->{
 *    // ヘッダ１列目
 *    String value = h.get(0);
 * }, (n, p)->{
 *     // n = CSV行カウント（１始まり）、p = CSV １行の Line&lt;String&gt;
 *     // １列目
 *     String value =  p.get(0);
 * });
 *
 *
 * ヘッダ無し読込み、(for Windows SJIS CSV)
 * Csvprocess  process = new Csvprocess();
 * process.readNoheader(new InputStreamReader(in, "MS932"), (i, p)->{
 *      // i = 行index（０始まり）
 *      // p = １行の Line&lt;String&gt;
 *      p.stream().forEach(e->{
 *         System.out.print("[" + e + "]");
 *      });
 * });
 *
 * </PRE>
 * @since version 4.9
 */
public class Csvprocess extends CSVStreamProcessor{
	/**
	 * ヘッダ有りＣＳＶ読込み実行.
	 * @param inReader InputStreamReader
	 * @param header ヘッダ行 Consumer
	 * @param processor コンテンツ行BiConsumer、CSV行読込みカウント（１始まり）とCSV文字列のList
	 * @throws IOException
	 * @throws ProcessingException
	 */
	public void read(InputStreamReader inReader, Consumer<List<String>> header, BiConsumer<Integer, List<String>> processor)
	throws IOException, ProcessingException{
		CSVReader reader = new CSVReader(new BufferedReader(inReader), getComment());
		try{
			int lineCount = 0;
			while(true){
				List<String> fields = reader.readLine();
				if (fields.size()==0) break;
				try{
					if (isHasHeader() && lineCount==0){
						header.accept(fields);
					}else{
						processor.accept(lineCount, fields);
					}
				}catch(Exception e){
					throw new ProcessingException(e, reader.getLineNumber());
				}
				lineCount++;
			}
		}finally{
			reader.close();
		}
	}
	/**
	 * ヘッダ無しＣＳＶ読込み実行.
	 * @param inReader InputStreamReader
	 * @param processor BiConsumer 行のindexとCSV文字列のList
	 * @throws IOException
	 * @throws ProcessingException
	 * @throws ParseException
	 */
	public void readNoheader(InputStreamReader inReader, BiConsumer<Integer, List<String>> processor) throws IOException, ProcessingException, ParseException{
		CSVReader reader = new CSVReader(new BufferedReader(inReader), getComment());
		try{
			int lineIndex = 0;
			while(true){
				List<String> fields = reader.readLine();
				if (fields.size()==0) break;
				try{
					processor.accept(lineIndex, fields);
				}catch(Exception e){
					throw new ProcessingException(e, reader.getLineNumber());
				}
				lineIndex++;
			}
		}finally{
			reader.close();
		}
	}

}
