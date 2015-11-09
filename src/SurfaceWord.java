
import java.util.HashMap;
import java.util.Map;

public class SurfaceWord {
	private final String surfaceWord;
	protected final  int slength;
	protected final Map<Character, Integer> toNumber = new HashMap<Character, Integer>();
	public final Map<Integer, Character> toLetter= new HashMap<Integer, Character>();
    protected final Map<Character, Character> barL= new HashMap<Character, Character>();
	public final Map<Integer, Integer> barN= new HashMap<Integer,Integer>();
	//public   Map<Integer[], Integer> sr = new HashMap<Integer[], Integer>(); 


	
	public Map<Integer, Integer> getBarN() {
		return barN;
	}

	public SurfaceWord(String sw){
		surfaceWord = sw;
		slength = surfaceWord.length();
		//store the translations from letter to number and number to letter.
		for(int i = 0 ; i< slength ; i++){
			toLetter.put(i,surfaceWord.charAt(i));
			toNumber.put(surfaceWord.charAt(i),i);
		}
		//store the bar function for letters.
		
		for(int h = 65 ; h < 91 ; h++){
			 barL.put((char) h, (char) (h - 'A' + 'a'));
		}
		for(int h = 97 ; h < 123 ; h++){
			 barL.put((char) h, (char) (h - 'a' + 'A'));
		}
		
		//store the bar function for numbers
		for(int h =0 ; h < slength ; h++){
			barN.put(h, toNumber.get(barL.get(toLetter.get(h))));
		}
	}	
	
	public int remainderModSurfaceLength(int  j){
		j = j % this.slength;
		if (j >= 0)
			return j;
		else
			return this.slength + j;
	}
	
	public String toString(int[] curve)
	{
		String word="";
		for(int i=0;i<curve.length;i++)
		{
			word=word+toLetter.get(curve[i]);
	
		}
		return word;
	}
	
	public String SNumtoSLetters(String nums)
	{	String s="";
		for(int i=0;i<nums.length();i++){
			s=s+this.LtoString(Character.getNumericValue(nums.charAt(i)));
		}return s;
	}
	
	public boolean IsReduced(String w){
		return w.equals(this.Reduce(w));
		}
	
	public String Reduce(String w){
		int done=0;
		StringBuilder sb = new StringBuilder(w);
		//System.out.println("Checking "+w);
		while(done==0){
			done=1;
			int i=0;
			while(i<sb.length()){
			//	System.out.println("*"+sb.length());
			//	System.out.println(i);
				if(sb.charAt(i)==this.barL.get(sb.charAt((i+1)%(sb.length())))){
					if(i==0){sb.delete(0,2);done=0;i=0;}else
					if(i<sb.length()-1){sb.delete(i,i+2);done=0;i=0;}else
					if(i==sb.length()-1){sb.deleteCharAt(i);sb.deleteCharAt(0);done=0;i=0;}}else i++;
				    
			}
		}
		return sb.toString();
		}
	
	public boolean ContainsEveryLetter(String w)
	{
		//surface word must be of form abAB, abABcdCD, abABcdCDefEF etc.
	if(this.ContainsExtraPunctures(w)==false){
		int count=0;
		for(int i=0;i<slength;i=i+4){
			if((w.indexOf(surfaceWord.charAt(i))!=-1)||(w.indexOf(surfaceWord.charAt(i+2))!=-1)){count++;}
			if((w.indexOf(surfaceWord.charAt(i+1))!=-1)||(w.indexOf(surfaceWord.charAt(i+3))!=-1)){count++;}
			
		}
		return count==slength/2;}
	else return true;  //lazy way out...don't bother!!
	
	}
	public boolean ContainsExtraPunctures(String w)
	{
		for(int i=0;i<w.length()-1;i++)
		{
			if(w.charAt(i)==this.barL.get(w.charAt(i+1))) return true;
		}
		return false;
	}
	
	public String LtoString(int i)
	{
		return "" + toLetter.get(i);
	}

	public int[] toNumber(String aWord){
		int[] answer = new int[aWord.length()];
		for(int i=0 ; i < aWord.length() ; i++){
			answer[i]= toNumber.get(aWord.charAt(i));
		}
		return answer;
	}
	
	public int getSurfaceLength() {
		return slength;
	}


	public String getSurfaceWord() {
		return surfaceWord;
	}


	public int[] barN(int[] q){
		int[] w = new int[q.length];
		for (int i = 0; i < q.length; i++) {
			w[i] = this.barN.get(q[q.length - i - 1]);
		}
		return w;
	}
	

}
