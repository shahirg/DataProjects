package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION



		TrieNode root = new TrieNode(null,null,null);	
		for(int i = 0;  i< allWords.length; i++) {
			//System.out.println(allWords[i]);
			root = insert(root,allWords,i,(short) 0);
			//print(root,allWords);
		}

		return root;
	}
	public static TrieNode insert(TrieNode root, String [] words,int i,short st) {	
		if(root == null) {
			return new TrieNode(new Indexes(i,st,(short)(words[i].length()-1)),null,null);
		}
		else if(root.substr == null) {
			root.firstChild = insert(root.firstChild,words,i,(short) 0);
		}
		else if(simPrefix(words[root.substr.wordIndex].substring(root.substr.startIndex),words[i].substring(root.substr.startIndex))) {
			String curr = words[root.substr.wordIndex].substring(root.substr.startIndex,root.substr.endIndex+1);
			String sim = getPrefix(curr,words[i].substring(root.substr.startIndex));
			if(words[i].contains(words[root.substr.wordIndex].substring(0, root.substr.endIndex+1))) {
				root.firstChild = insert(root.firstChild,words,i,(short) (root.substr.startIndex));
			}
			else if(root.firstChild == null || (!curr.equals(words[i].substring(0, curr.length() ))) ) {
				TrieNode ptr = new TrieNode(new Indexes(root.substr.wordIndex, (short) (sim.length() + root.substr.startIndex),root.substr.endIndex)
						,root.firstChild,insert(null,words,i,(short) (sim.length() + root.substr.startIndex)));
				root.substr.endIndex =  (short) ((sim.length() - 1) + root.substr.startIndex);
				root.firstChild = ptr;
			}
			else {
				root.firstChild = insert(root.firstChild,words,i,(short) (root.substr.startIndex));
			}	
		}
		else {
			root.sibling = insert(root.sibling,words,i,root.substr.startIndex);	
		}	
		return root;
	}
	private static boolean simPrefix(String x,String y) {
		return(x.charAt(0) == y.charAt(0));
	}
	private static String getPrefix(String x,String y) {
		if(x.length() == 0 || y.length() == 0 || x.charAt(0) != y.charAt(0))
			return "";
		return x.charAt(0) + getPrefix(x.substring(1),y.substring(1));	
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		return  compList(root,allWords,prefix, new ArrayList<TrieNode>());
	}

	public static ArrayList<TrieNode> compList(TrieNode root,
			String[] words, String prefix, ArrayList<TrieNode> list) {

		if(root == null) {return list;}
		if(root.substr == null) {//is root
			list = compList(root.firstChild , words,prefix,list);
			if(list.size() == 0) 
				return null;
			return list;
		}
		if(simPrefix(prefix,words[root.substr.wordIndex])) {//on right track

			if(prefix.equals(words[root.substr.wordIndex].substring(0,root.substr.endIndex+1))) {	
				if(words[root.substr.wordIndex].length() == words[root.substr.wordIndex]
						.substring(0, root.substr.endIndex+1).length()) {//word search
					list.add(root);
					return list;
				}
				return compList(root.firstChild , words,prefix,list);	
			}
			else if(words[root.substr.wordIndex].contains(prefix) 
					&& (root.substr.endIndex + 1) == (words[root.substr.wordIndex].length())) {
				list.add(root);
				return compList(root.sibling , words,prefix,list);	
			}
			else if(!(simPrefix(prefix.substring(root.substr.startIndex),words[root.substr.wordIndex].substring(root.substr.startIndex)))) {
				list = compList(root.sibling , words,prefix,list);
				return list;
			}
			list = compList(root.firstChild , words,prefix,list);
			if(list == null) {	
				list = compList(root.sibling , words,prefix,list);
			}
			return list;
		}
		return compList(root.sibling , words,prefix,list);//not on track move over
	}
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
