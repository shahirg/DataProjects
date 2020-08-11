package structures;

public class Palindrome {
	public static boolean isPalindrome(String s) {
		s = s.toUpperCase();
		Stack<Character> stk = new Stack<Character>();
		for(int i = 0;i< s.length();i++) {
			Character c = s.charAt(i);
			if(Character.isAlphabetic(s.charAt(i)))
				stk.push(s.charAt(i));

		}

		for(int i = 0;i<s.length();i++) {
			if(Character.isAlphabetic(s.charAt(i))) {
				if(stk.isEmpty())
					return false;
				
				char c = stk.pop();
				if(c != s.charAt(i))
					return false;
			}
		}
		return true;

	}
	public static void main(String[]args) {
		System.out.println(isPalindrome("R.. 3422wac e Ca    r"));
	}
}
