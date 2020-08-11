package trie;
import java.util.ArrayList;
public class Test {
	private static String getPrefix(String x,String y) {
		if(x.length() == 0 || y.length() == 0 || x.charAt(0) != y.charAt(0))
			return "";
		return x.charAt(0) +  getPrefix(x.substring(1),y.substring(1));	
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> test = new ArrayList<String>();
		test.add("sdfjk");
		ArrayList<String> t = new ArrayList<String>();
		//t = null;
		//test.add(null);
		String x = "333";
		String y = "2";
		System.out.println(getPrefix("34","fix"));
	}

}
