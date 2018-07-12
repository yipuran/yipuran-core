package org.yipuran.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.yipuran.csv4j.CSVReader;
import org.yipuran.csv4j.CSVStreamProcessor;
import org.yipuran.csv4j.ParseException;
import org.yipuran.csv4j.ProcessingException;
import org.yipuran.util.BOMfunction;
import org.yipuran.util.SimplePair;
/**
 * ＣＳＶ読込みProcessor.
 * ヘッダ有り読込みとヘッダ無し読込みを提供する。
 * <PRE>
 * ヘッダ有り読込み、
 * BiConsumer が実行される。ヘッダの列名をキーで列インデックス（０～ｎ）をのMapと
 * 行番号とＣＳＶ行である List&lt;String&gt;のkey-valueである  org.yipuran.util.SimplePair&lt;Integer, List&lt;String&gt;&gt; が
 * BiConsumer の引数である。
 *
 * CsvStreamProcessor  processor = new CsvStreamProcessor();
 * processor.read(new InputStreamReader(in, "MS932"), (m, p)->{
 *     // SimplePair から取得する行番号：ヘッダの次の行を１としてカウント
 *     int lineno =  p.getKey();
 *     // ヘッダ "a" の列を取得
 *     String value =  p.getValue().get(m.get("a"));
 * });
 *
 *
 * ヘッダ無し読込みは、org.yipuran.csv.CsvLineProcessor を使用する。
 *
 * CsvStreamProcessor  processor = new CsvStreamProcessor();
 * processor.readNoheader(new InputStreamReader(in, "MS932"), (n, l)->{
 *      // n = 行番号（１始まり）
 *      // l = １行の Line&lt;String&gt;
 *      l.stream().forEach(e->{
 *         System.out.print("[" + e + "]");
 *      });
 * });
 *
 * （注意）空列＝空文字で読み込まれる。
 * </PRE>
 */
public class CsvStreamProcessor extends CSVStreamProcessor{
	/**
	 * ヘッダ有りＣＳＶ読込み実行.
	 * @param inReader
	 * @param processor ヘッダMap(key, 列index) と SimplePair(行番号,フィールドリスト）の BiConsumer
	 * @throws IOException
	 * @throws ProcessingException
	 * @throws ParseException
	 */
	public void read(InputStreamReader inReader, BiConsumer<Map<String, Integer>, SimplePair<Integer, List<String>>> processor)
	throws IOException, ProcessingException, ParseException{
		CSVReader reader = new CSVReader(new BufferedReader(inReader), getComment());
		try{
			Map<String, Integer> headerMap = new HashMap<>();
			int lineCount = 0;
			while(true){
				List<String> fields = reader.readLine();
				if (fields.size()==0) break;
				try{
					if (isHasHeader() && lineCount==0){
						int row = 0;
						for(String key:fields){
							headerMap.put(key, row);
							row++;
						}
					}else{
						processor.accept(headerMap, new SimplePair<Integer, List<String>>(lineCount, fields));
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
	 * @param processor CsvLineProcessor
	 * @throws IOException
	 * @throws ProcessingException
	 * @throws ParseException
	 */
	public void readNoheader(InputStreamReader inReader, CsvLineProcessor processor) throws IOException, ProcessingException, ParseException{
		CSVReader reader = new CSVReader(new BufferedReader(inReader), getComment());
		try{
			int lineCount = 0;
			while(processor.continueProcessing()){
				List<String> fields = reader.readLine();
				if (fields.size()==0) break;
				try{
					if (isHasHeader() && lineCount==0){
						processor.processHeaderLine(reader.getLineNumber(), fields);
					}else{
						processor.processDataLine(reader.getLineNumber(), fields);
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
	 * BOM付き、ヘッダ有りＣＳＶ読込み実行.
	 * <PRE>
	 * ヘッダ読込みは、BOM付きとして読み込まれて結果取得する先頭のＣＳＶヘッダキーは BOMが予め除去されたヘッダを受け取ることができる。
	 * 読込みI/O の InputStreamReader は、UTF-8 が指定されてなければ、ParseException をスローする
	 * </PRE>
	 * @param inReader UTF-8 エンコード指定した InputStreamReader
	 * @param processor ヘッダMap(key, 列index) と SimplePair(行番号,フィールドリスト）の BiConsumer
	 * @throws IOException
	 * @throws ProcessingException
	 * @throws ParseException
	 */
	public void readWithUTF8_BOM(InputStreamReader inReader, BiConsumer<Map<String, Integer>, SimplePair<Integer, List<String>>> processor)
	throws IOException, ProcessingException, ParseException{
		if (!inReader.getEncoding().equals("UTF-8")) throw new ParseException("Request encoding Error : " + inReader.getEncoding());
		CSVReader reader = new CSVReader(new BufferedReader(inReader), getComment());
		try{
			Map<String, Integer> headerMap = new HashMap<>();
			int lineCount = 0;
			while(true){
				List<String> fields = reader.readLine();
				if (fields.size()==0) break;
				try{
					if (isHasHeader() && lineCount==0){
						int row = 0;
						for(String key:fields){
							if (row==0){
								headerMap.put(BOMfunction.chop(key), 0);
							}else{
								headerMap.put(key, row);
							}
							row++;
						}
					}else{
						processor.accept(headerMap, new SimplePair<Integer, List<String>>(lineCount, fields));
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

}
