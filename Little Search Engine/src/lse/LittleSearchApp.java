package lse;

import java.io.*;
import java.util.*;
//custom runner for my needs
public class LittleSearchApp {
	public static ArrayList<Occurrence> makeFreqs(){
		ArrayList<Occurrence> out = new ArrayList<Occurrence>();
		Occurrence o = new Occurrence("test",0);
		o.frequency = 82;
		out.add(new Occurrence("test",82));
		o.frequency = 76;
		out.add(new Occurrence("test",76));
		o.frequency = 71;
		out.add(new Occurrence("test",71));
		o.frequency = 71;
		out.add(new Occurrence("test",71));
		o.frequency = 70;
		out.add(new Occurrence("test",70));
		o.frequency = 65;
		out.add(new Occurrence("test",65));
		o.frequency = 61;
		out.add(new Occurrence("test",61));
		o.frequency = 56;
		out.add(new Occurrence("test",56));
		o.frequency = 54;
		out.add(new Occurrence("test",54));
		o.frequency = 51;
		out.add(new Occurrence("test",51));
		o.frequency = 48;
		out.add(new Occurrence("test",48));
		o.frequency = 45;
		out.add(new Occurrence("test",45));
		o.frequency = 41;
		out.add(new Occurrence("test",41));
		o.frequency = 36;
		out.add(new Occurrence("test",36));
		o.frequency = 34;
		out.add(new Occurrence("test",34));
		o.frequency = 30;
		out.add(new Occurrence("test",30));
		o.frequency = 25;
		out.add(new Occurrence("test",25));
		o.frequency = 20;
		out.add(new Occurrence("test",20));
		o.frequency = 20;
		out.add(new Occurrence("test",20));
		o.frequency = 18;
		out.add(new Occurrence("test",18));
		o.frequency = 17;
		out.add(new Occurrence("test",17));
		o.frequency = 17;
		out.add(new Occurrence("test",17));
		o.frequency = 14;
		out.add(new Occurrence("test",14));
		o.frequency = 12;
		out.add(new Occurrence("test",14));
		return out;
	}
	public static void print(HashMap<String,Occurrence> map) {
		System.out.println(map.toString());
	}
	public static void usingBufferedWritter(String x) throws IOException
	{
	    String textToAppend = x;

	    BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt", true) );
	    writer.newLine();   //Add new line
	    writer.write(textToAppend);
	    writer.close();
	}
	public static void clearFile() throws FileNotFoundException{

		Formatter f = new Formatter("test.txt");
		Scanner s = new Scanner("test.txt");
	    while(s.hasNext()){
	        f.format(" ");
	    }
	    System.out.println("file cleared");
	}
	public static void main(String[] args) throws IOException {

		Scanner sc  = new Scanner(System.in);
		LittleSearchEngine ls = new LittleSearchEngine();

		//System.out.print("Enter Doc File name: ");
		String file1 = "test2.txt";//sc.nextLine();
		//System.out.print("Enter Noise words File name: ");
		String file2 = "noisewords.txt";
		ls.makeIndex(file1,file2);
		System.out.println(ls.keywordsIndex.toString());

		String in = "";
		while(!in.equals("e")) {
			System.out.print("Enter keyword(enter no to exit): ");
			in = sc.next();
			if(in.equals("e")) {break;}
			System.out.println(ls.getKeyword(in));
		}
		in = "";
		while(!in.equals("e")) {
			System.out.print("Enter File(enter no to exit): ");
			in = sc.next();
			if(in.equals("e")) {break;}
			print(ls.loadKeywordsFromDocument(in));
		}
		in = "";
		while(!in.equals("e")) {
			System.out.print("Enter num to add(enter no to exit): ");
			in = sc.next();
			if(in.equals("e")) {break;}
			ArrayList<Occurrence> out = makeFreqs();
			//System.out.println(out.toString());
			out.add(new Occurrence("test",Integer.parseInt(in)));
			//System.out.println(out.toString());
			System.out.println(ls.insertLastOccurrence(out).toString());
		}
		in = "";
		while(!in.equals("e")) {
			System.out.print("Enter key to get occurances(enter no to exit): ");
			in = sc.next();
			if(in.equals("e")) {break;}
			ArrayList<Occurrence> o = ls.keywordsIndex.get(in);
			System.out.println(o.toString());

		}
		in = "";
		while(!in.equals("e")) {
			System.out.print("Enter top search words(enter no to exit): ");
			in = sc.next();
			if(in.equals("e")) {break;}
			String two = sc.next();
			ArrayList<String> search = ls.top5search(in,two);
			System.out.println(search.toString());

		}
		System.out.println(ls.keywordsIndex.containsKey("service"));
		System.out.println(ls.top5search("service",""));
	}

}
