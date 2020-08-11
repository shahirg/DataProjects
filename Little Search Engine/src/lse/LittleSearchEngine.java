package lse;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		File doc = new File(docFile);
		if(!doc.exists()) {
			throw new FileNotFoundException("File Not Found");
		}
		
		HashMap<String,Occurrence> map = new HashMap<String,Occurrence>(1000,2.0f);
		Scanner sc = new Scanner(doc);
		
		while (sc.hasNext()) {
			String word = getKeyword(sc.next());
			if(word != null && word.length() > 0) {
				if(map.containsKey(word)) {
					Occurrence val = map.get(word);
					val.frequency++;			
				}
				else {
					map.put(word, new Occurrence(docFile, 1));
				}
			}
		}
		sc.close();
	
		
		return map;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Occurrence> list;
		for(String key: kws.keySet()) {
			list = keywordsIndex.containsKey(key) ? keywordsIndex.get(key):new ArrayList<Occurrence>();
			list.add(kws.get(key));
			insertLastOccurrence(list);
			if(list.size() == 1) {
				keywordsIndex.put(key, list);
				continue;
			}
			keywordsIndex.replace(key, list);
		}
	}

	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {

		String out = removePunc(word.toLowerCase());
		for(int i = 0;i < out.length(); i++) {
			if(!Character.isLetter(out.charAt(i))) { 
				return null;
			}			
		}
		out = out.length() == 0 || noiseWords.contains(out) ? null:out;
		return out;
	}
	private String removePunc(String x) {
		String punc = ".,?:;!";
		if(x.length() > 0 && punc.indexOf(x.substring(x.length()-1,x.length())) >= 0 )
			return removePunc(x.substring(0,x.length()-1));
		return x;
	}
	
	

	


	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
	
		if(occs.size() == 1) {
			return null;
		}
		Occurrence target = occs.get(occs.size()-1);
		ArrayList<Integer> x = binSearch(occs, target.frequency, 0,occs.size()-2, new ArrayList<Integer>());
		int ind = x.get(x.size()-1);
		if(target.frequency >= occs.get(ind).frequency) {
			occs.add(ind, occs.get(occs.size() -1));
		}
		else {
			occs.add(ind + 1, occs.get(occs.size() -1));
		}
		occs.remove(occs.size()-1);
		return x;
		

	}

	private static  ArrayList<Integer> binSearch(ArrayList<Occurrence> occs,int target, 
	int lo, int hi,  ArrayList<Integer> list) {
		
		int mid = (lo+hi)/2;
		list.add(mid);		
		if(occs.get(mid).frequency == target || lo == hi) {
			return list;
		}
		else if(occs.get(mid).frequency> target) {
			if(mid + 1 > hi) {
				return list;
			}
			return binSearch(occs, target, mid+1, hi,list);
		}
		else {
			if(mid - 1 < lo) {
				return list;
			}
			return binSearch(occs, target, lo, mid - 1, list);
		}
		
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if(!keywordsIndex.containsKey(kw1)&&!keywordsIndex.containsKey(kw2)) {
			return null;
		}
		ArrayList<String> top = new ArrayList<String>();
		ArrayList<Occurrence> vals1, vals2;
		boolean x = keywordsIndex.containsKey(kw1);
		vals1 = keywordsIndex.containsKey(kw1) ? keywordsIndex.get(kw1) : null;
		vals2 = keywordsIndex.containsKey(kw2) ? keywordsIndex.get(kw2) : null;

		int ind1 = 0, ind2  = 0;
		while(vals1 != null && vals2 != null && ind1 < vals1.size() && ind2 < vals2.size() && top.size()<5) {
			Occurrence o1 = vals1.get(ind1), o2 = vals2.get(ind2);
			if(o1.frequency > o2.frequency) {
				if(!top.contains(o1.document)) {
					top.add(o1.document);
				}
				ind1++;
			}
			else if(o1.frequency < o2.frequency) {
				if(!top.contains(o2.document)) {
					top.add(o2.document);
				}
				ind2++;
			}
			else {//equal
				if(!top.contains(o1.document)) {
					top.add(o1.document);
				}
				ind1++;
				ind2++;
			}
		}
		vals1 = vals1 != null ? vals1:vals2;
		ind1 = vals1 != null ? ind1:ind2;
		while(vals1 != null && ind1 < vals1.size() && top.size() < 5) {
			if(!top.contains(vals1.get(ind1).document)) {
				top.add(vals1.get(ind1).document);
			}
			ind1++;
		}
		return top;
	
	}
}
