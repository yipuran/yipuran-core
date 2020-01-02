package org.yipuran.csv.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.yipuran.csv.CsvCreator;
import org.yipuran.csv.Csvprocess;

/**
 * CsvTest.
 */
public class CsvTest {
	public static void main(String[] args){

		List<String[]> list = new ArrayList<>();

		CsvCreator c = ()->()->list;

		list.add(new String[]{ ",a", "b", "c"} );
		list.add(new String[]{ "10", "20",  "30"  } );
		list.add(new String[]{ "11", null, "31" } );
		list.add(new String[]{ ",12", "21", "32"  } );
		list.add(new String[]{ "あ", "い,", "う"  } );


		try(OutputStream out = new FileOutputStream("c:/work/sample.csv")){
			//c.create(out, "MS932");
			c.createWithDblQuot(out, "MS932");
		}catch(Exception e){
			e.printStackTrace();
		}
		try(OutputStream out = new FileOutputStream("c:/work/sample8.csv")){
			c.createBomUTF8(out);
		}catch(Exception e){
			e.printStackTrace();
		}

		Csvprocess  proces = new Csvprocess();
		try(InputStream in = new FileInputStream("c:/work/sample.csv")){
			proces.readNoheader(new InputStreamReader(in, "MS932"), (i, p)->{
				System.out.print(i + ": " + p.size() + " : ");
				p.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.print("\n");
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		try(InputStream in = new FileInputStream("c:/work/sample.csv")){
			proces.read(new InputStreamReader(in, "MS932"), h->{
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
		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample8.csv")){
			proces.readNoheader(new InputStreamReader(in, StandardCharsets.UTF_8), (i, p)->{
				System.out.print(i + ": " + p.size() + " : ");
				p.stream().forEach(s->System.out.print("["+s+"]"));
				System.out.print("\n");
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("--------------------------");
		try(InputStream in = new FileInputStream("c:/work/sample8.csv")){
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

	}

}
