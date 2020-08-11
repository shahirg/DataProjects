package lse;

import java.util.ArrayList;

public class Test {
	static int test = 7;
	private static String punc = ".,?:;!";
	private static String removePunc(String x) {
		if(punc.indexOf(x.substring(x.length()-1,x.length())) >= 0){
			return removePunc(x.substring(0,x.length()-1));
		}
		return x;
	}
	private static String add(int x) {
		 x+=5;
		return "";
	}
	private static  ArrayList<Integer> binSearch(ArrayList<Integer> occs,int target, 
	int lo, int hi,  ArrayList<Integer> list) {
		
		System.out.println(lo+" : " +hi);
		int mid = (hi+lo)/2;
		if(lo == hi || (hi+lo)/2 == 0) {
			int ind = occs.get(mid) > target ?mid + 1 :mid;
			list.add(ind);
			return list;
		}
		list.add(mid);
		System.out.println("mid: "+ mid);
		if(occs.get(mid)== target) {	
			return list;
		}
		else if(occs.get(mid) > target) {
			return binSearch(occs, target, mid+1 , hi, list);
		}
		else {
			return binSearch(occs, target, lo , mid-1, list);
		}
	}
	private static  ArrayList<Integer> search(ArrayList<Integer> occs,int target, 
	int lo, int hi,  ArrayList<Integer> list) {
		int mid = (lo+hi)/2;


		list.add(mid);		
		if(occs.get(mid) == target || lo == hi) {
			return list;
		}
		else if(occs.get(mid) > target) {
			if(mid + 1 > hi) {
				return list;
			}
			return search(occs, target, mid+1, hi,list);
		}
		else {
			if(mid - 1 < lo) {
				return list;
			}
			return search(occs, target, lo, mid - 1, list);
		}
		
	}
	public static void main(String[]args) {
		ArrayList<Integer> x = new ArrayList<Integer>();


		add(test);
		System.out.println(test);
		x.add(82);
		x.add(76);
		x.add(71);
		x.add(71);
		x.add(70);
		x.add(65);
		x.add(61);
		x.add(56);
		x.add(54);
		x.add(51);
		x.add(48);
		x.add(45);
		x.add(41);
		x.add(36);
		x.add(34);
		x.add(30);
		x.add(25);
		x.add(20);
		x.add(20);
		x.add(18);
		x.add(17);
		x.add(17);
		x.add(14);
		x.add(12);
		x.add(50);
		System.out.println(x.toString());
		int target = x.get(x.size()-1);
		ArrayList<Integer> y = search(x, target, 0, x.size()-2, new ArrayList<Integer>());
		System.out.println(x.get(x.size()-1));


		System.out.println(y.toString());
		x.add(y.get(y.size()-1), x.get(x.size() -1));
		x.remove(x.size()-1);
		System.out.println(x.toString());

	}
}
