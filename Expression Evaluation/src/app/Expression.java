package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	StringTokenizer ptr = new StringTokenizer(removeSpaces(expr),delims,true);

        while (ptr.hasMoreTokens()) {
        	String name = ptr.nextToken();
        	Array arr = new Array(name);
			Variable var = new Variable(name);
			if(!(arrays.contains(arr)) && !(vars.contains(var))) {
	        	int c = name.charAt(0);      
	            if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
	            	if(ptr.hasMoreTokens() && ptr.nextToken().equals("[")) {
	            		arrays.add(new Array(name));
	            	}
	            	else {
	            		vars.add(new Variable(name));
	            	}
	            }
			}
        }

    }
    	
    	
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	StringTokenizer ptr = new StringTokenizer(removeSpaces(expr),delims,true);

    	Stack<Float> nums = new Stack<Float>();
    	Stack<String> math = new Stack<String>();

    	while(ptr.hasMoreTokens()) { 		
    		String curr = ptr.nextToken();
    		int c = curr.charAt(0);
    		if(delims.indexOf(c) < 0 || c == '(') {
    			if(delims.indexOf(c) < 0) {
	    			if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {//variable or array
	    				Array arr = new Array(curr);
	    				Variable var = new Variable(curr);
	    				if(vars.contains(var)) {
	    					nums.push((float) (vars.get(vars.indexOf(var)).value));
	    				}
	    				else if(arrays.contains(arr)) {
	    					int x = (int) Math.floor(evaluate(getExpRec(ptr, ptr.nextToken()),vars,arrays));
	    					nums.push((float) arrays.get(arrays.indexOf(arr)).values[x]);	
	    				}
	    			}
	    			else {
	    				nums.push(Float.parseFloat(curr));//pushing just a number
	    			}
    			}
    			else {
    				float x = evaluate(getExpRec(ptr, "("),vars,arrays);
    				nums.push(x);
    			}
    			if(!(math.isEmpty())) {
    				String op = math.pop();
    				switch (op) {
    					case "*": nums.push(nums.pop()*nums.pop());
    						break;
    					case "/": nums.push(1/(nums.pop()/nums.pop())); 
    						break;
    					case "-": nums.push(-1*nums.pop()); math.push("+"); 
    						break;
    					default: math.push(op); 
    				}
    			}
    		}
    		else if(delims.indexOf(c) <= 5) {
    			math.push(curr);
    		}

    	}
    	while(nums.size() > 1) {
    		nums.push(nums.pop()+nums.pop());
    	}
    	return nums.pop();  
    }

    private static String getExpRec(StringTokenizer st, String in) { 	
    	Stack<String> parends = new Stack<String>();
    	parends.push(in);
    	String out = "";
		while(!(parends.isEmpty()) && st.hasMoreTokens()) {
			String next = st.nextToken();
	    	if(next.equals("(") || next.equals("[")) {
	    		parends.push(next);
	    	}
	    	else if(next.equals(")") || next.equals("]")) {
	    		parends.pop();
	    	}
			if(parends.isEmpty()) {
				break;
			}
			out += next;
		}
		return out;
	} 	
    private static String removeSpaces(String x) {
    	if(x.length() == 0) {
    		return "";
    	}
    	else if(x.substring(0,1).equals(" ")) {
    		return removeSpaces(x.substring(1));
    	}
    	else {
    		return x.substring(0,1) + removeSpaces(x.substring(1));
    	}
    }
}
