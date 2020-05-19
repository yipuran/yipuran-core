package org.yipuran.csv.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.yipuran.csv.CsvCreator;
import org.yipuran.csv.CsvUtil;
import org.yipuran.csv.Csvprocess;

/**
 * CsvTest2.java
 */
public class CsvTest2{

	/**
	 * @param args
	 */
	public static void main(String[] args){

		List<String[]> list = new ArrayList<>();

		CsvCreator c = ()->()->list;

		list.add(new String[]{ "Ａ", "Ｂ", "Ｃ"  } );
		list.add(new String[]{ ",a", "b", "c"} );
		list.add(new String[]{ "10", "20",  "30"  } );
		list.add(new String[]{ "11", null, "31" } );
		list.add(new String[]{ ",12", "21", "32"  } );
		list.add(new String[]{ "あ", "い,", "う"  } );

		try(OutputStream out = new FileOutputStream("c:/work/sample8bom.csv")){
			c.createBomUTF8WithDblQuot(out);
//			c.createBomUTF8(out);
		}catch(Exception e){
			e.printStackTrace();
		}
		Csvprocess  proces = new Csvprocess();
		//Csvprocess  proces = new Csvprocess(true);

		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample8bom.csv")){
			proces.readNoheader(new InputStreamReader(in, StandardCharsets.UTF_8), (i, p)->{
				System.out.print(i + ": " + p.size() + " : ");
				p.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.print("\n");
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample8bom.csv")){
			proces.read(new InputStreamReader(in, StandardCharsets.UTF_8), h->{
				System.out.println("===== header =======");
				h.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.println("\n====================");
			}, (i, p)->{
				System.out.print(i + ": " + p.size() + " : ");
				p.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.print("\n");
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("---------- Map ----------------");
		try(InputStream in = new FileInputStream("c:/work/sample8bom.csv")){
			proces.read(new InputStreamReader(in, StandardCharsets.UTF_8), (i, m)->{
				System.out.println("Line:" + i + " : " +
					m.entrySet().stream().map(e->"{" + e.getKey() + ":" + e.getValue() + "}").collect(Collectors.joining())
				);
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample81.csv")){
			proces.readNoheader(new InputStreamReader(in, StandardCharsets.UTF_8), (i, p)->{
				System.out.print(i + ": " + p.size() + " : ");
				p.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.print("\n");
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("--------------------------");
		System.out.println("sample8bom = "+ CsvUtil.isBOMutf8(new File("c:/work/sample8bom.csv")) );


	}

}
