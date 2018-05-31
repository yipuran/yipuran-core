package org.yipuran.csv;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.yipuran.csv4j.CSVWriter;
/**
 * ＣＳＶ生成インターフェース.
 * <PRE>
 * List<String[]> list = new ArrayList<>();
 *    // list に格納
 * CsvCreator c = ()->()->list;
 *    // 宣言
 *
 * try(OutputStream out = new FileOutputStream("c:/work/sample.csv")){
 *    c.create(out, "MS932");
 * }catch(Exception e){
 *    e.printStackTrace();
 * }
 * </PRE>
 */
@FunctionalInterface
public interface CsvCreator extends Serializable{

	/**
	 * ＣＳＶ出力行数分の Colleactionを返す Supplier を取得する.
	 * @return Supplier<Collection<String[]>
	 */
	public Supplier<Collection<String[]>> getSupplier();

	/**
	 * ＣＳＶ出力実行.
	 * @param out OutputStream
	 * @param charName 文字セット名
	 */
	default public void create(OutputStream out, String charName){
		try(OutputStreamWriter writer = new OutputStreamWriter(out, charName)){
			CSVWriter csvWriter = new CSVWriter(writer);
			for(String[] s:getSupplier().get()){
				for(int i=0;i < s.length;i++){
					s[i] = s[i]==null ? "" : s[i];
				}
				csvWriter.writeLine(s);
			}
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**
	 * ダブルクォート括りＣＳＶ出力実行.
	 * @param out OutputStream
	 * @param charName 文字セット名
	 */
	default public void createWithDblQuot(OutputStream out, String charName){
		String lineSeparator = System.getProperty("line.separator");
		try(OutputStreamWriter writer = new OutputStreamWriter(out, charName)){
			for(String[] sary:getSupplier().get()){
				for(int i=0;i < sary.length;i++){
					sary[i] = sary[i]==null ? "" : sary[i];
				}
				writer.write(csvline(sary));
				writer.write(lineSeparator);
			}
		}catch(Exception e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	/**
	 * ダブルクォート括り１行作成.
	 * @param ary １行生成する配列
	 * @return ダブルクォート括り１行
	 */
	default public String csvline(String[] ary){
		return "\"" + Arrays.stream(ary).map(s->s.replaceAll("\"","\"\"")).collect(Collectors.joining("\",\"")) + "\"";
	}
}
