package org.yipuran.csv.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yipuran.csv.CsvCreator;
import org.yipuran.csv.CsvStreamProcessor;

/**
 * CsvTest.
 */
public class CsvTest {
	public static void main(String[] args){

		List<String[]> list = new ArrayList<>();

		CsvCreator c = ()->()->list;

		list.add(new String[]{ "a", "b", "c"} );
		list.add(new String[]{ "10", "20",  "30"  } );
		list.add(new String[]{ "11", null, "31" } );
		list.add(new String[]{ "12", "21", "32"  } );
		list.add(new String[]{ "あ", "い", "う"  } );


		try(OutputStream out = new FileOutputStream("c:/work/sample.csv")){
			//c.create(out, "MS932");
			c.createWithDblQuot(out, "MS932");
		}catch(Exception e){
			e.printStackTrace();
		}

		CsvStreamProcessor  processor = new CsvStreamProcessor();
		try(InputStream in = new FileInputStream("c:/work/sample.csv")){
			processor.readNoheader(new InputStreamReader(in, "MS932"), (lno, linelist)->{
				System.out.print(lno + ": " + linelist.size() + " : ");
				System.out.print(linelist.get(0) + " : ");
				System.out.print(linelist.get(1) + " : ");
				System.out.println(linelist.get(2) + " : ");
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample.csv")){
			processor.read(new InputStreamReader(in, "MS932"), (m, p)->{
				System.out.print(p.getKey() + ": " + p.getValue().get(m.get("a")) + " : ");
				System.out.print(p.getValue().get(m.get("b")) + " : ");
				System.out.println(p.getValue().get(m.get("c")) + " : ");
			});
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
