
import java.util.ArrayList;
import java.util.Arrays;

//Given the input of a surface word, and an edge-crossing sequence, this class finds a configuration which has the minimal possible
//self-intersections, and determines if the curve is filling.

public class FillingTester extends SurfaceWord
{	
	int[][] PL;  //PL[i][0] = letter of point i, PL[i][1] = # of point i
	int[][] SL;  //SL[j][0] = index of first point, SL[j][1]=index of 2nd point  (in PL)
	int[] UL;   //determines whether the point index by i has been used.   PL format [ith point][0] = edge label; [ith point][1] = number along that edge
	int[] M;
	ArrayList BL1;
	ArrayList BL2;
	int[][] IntMat;
	String surfw;
	String curvw;
	public int done;
	public int minInt;

		public FillingTester()
		{
		super("");
		}
	
		public FillingTester(String sw, String cw)
		{
			super(sw);
			surfw=new String(sw);
			curvw=new String(cw);
			minInt=0;
			//construct point list according to rules inherent in sw and cw
			SL=new int[cw.length()][2];
			PL=new int[2*cw.length()][2];
			UL=new int[2*cw.length()];
			int[] C = this.toNumber(cw);
			int L=this.getSurfaceLength();
			M =new int[L];
			IntMat=new int[cw.length()][cw.length()];
			
			for(int i=0;i<=5;i++)
			{
				for(int j=0;j<=3;j++)
				{
					int k=i&j;
					// System.out.println(i+" "+j+"  "+k);
				}
			}
			//counts how many of each pair there are
			for( int i = 0; i<L;i++)
			{
				for (int j = 0;j<cw.length();j++)
				{
					if ( C[j] == i)
					 {
						M[i]++;
					 }
				}
			}
			int[] start=new int[L];
			start[0]=0;
			for(int i=1;i<L;i++)
			{
				start[i]=start[i-1]+M[i-1]+M[this.barN.get(i-1)];
			}
			//constructs the list of points
			int k=0;
			for (int i = 0;i<sw.length();i++)
			{
				boolean used;
				int con = this.barN.get(i);
				used=(con<i);
				if (!used)
				{
					for(int j=0;j<M[i]+M[con];j++)
					{	
						PL[k][0]=i;
						PL[k][1]=j;
						k++;
					}
				}
				else 
				{
					for(int j=M[i]+M[con]-1;j>=0;j--)
					{	
						PL[k][0]=i;
						PL[k][1]=j;
						k++;
					}
				}
			}
			//constructs the list of segments
			int LL=cw.length();
			int[] avail=new int[sw.length()];
			int h = this.barN.get(C[LL-1]);
			SL[0][0]=this.findPoint(h,M[C[LL-1]]+M[h]-1);
			SL[0][1]=findPoint(C[0],avail[C[0]]);	
			avail[C[0]]++;
			avail[this.barN.get(C[0])]++;
			for(int kk=1;kk<LL;kk++)
			{
				SL[kk][0]=this.findPoint(this.barN.get(PL[SL[kk-1][1]][0]),avail[PL[SL[kk-1][1]][0]]-1);
				SL[kk][1]=this.findPoint(C[kk],avail[C[kk]]);		
				avail[C[kk]]++;
				avail[this.barN.get(C[kk])]++;
			}
		}
		
		//This method decides whether the input word is trivial OR bounds a puncture, using the given surface word
		public boolean IsTrivial(String w)
		{
			//need to determine which word corresponds to the puncture
			int i=0;
			String puncw="";
			int j=0;
			while(i<surfw.length()){
				puncw=puncw+surfw.charAt(j);
				j=(surfw.indexOf(barL.get(surfw.charAt(j)))+1)%(surfw.length());
				i++;}
			if (Reduce(w).isEmpty()==true){
				return true;
			}
			if (IsEquivalentTo(Reduce(w),puncw))
			{
				return true;
			}
			return false;
		}
	
//decides whether w1 is an inverse or conjugate of w2, this is used to determine if w1 bounds a puncture - WORKING FOR NOW
		public boolean IsEquivalentTo(String w1,String w2)
		{
			if(w1.length()!=w2.length()) return false;
			StringBuilder w1c=new StringBuilder(w1);
			for(int i = 0;i<w2.length();i++){
				if(Reduce(w1c+w2).length()==0) return true;
				if(Reduce(w1c+toString(barN(toNumber(w2)))).length()==0) return true;
				char f=w1c.charAt(0);
				w1c=w1c.insert(0, barL.get(f));
				w1c=w1c.append(f);
				String w11=Reduce(w1c.toString());
				w1c=new StringBuilder(w11);
				
			}
			return false;
		}

		//Cyclically reduces the word w
		public String Reduce(String w){
			int done=0;
			StringBuilder sb = new StringBuilder(w);
			while(done==0){
				done=1;
				int i=0;
				while(i<sb.length()){
					if(sb.charAt(i)==this.barL.get(sb.charAt((i+1)%(sb.length())))){
						if(i==0){sb.delete(0,2);done=0;i=0;}else
						if(i<sb.length()-1){sb.delete(i,i+2);done=0;i=0;}else
						if(i==sb.length()-1){sb.deleteCharAt(i);sb.deleteCharAt(0);done=0;i=0;}}else i++;			    
				}
			}
			return sb.toString();
		}
		
		//This method starts from a point on the boundary of the polygon (determined by i and j), and builds the loop which starts at that point.
		//It needs a submethod "NextSegment" which, from a starting point, determines the next place the loop would exit the polygon
		public String BuildLoop(int i, int j)
		{ 
			String loop="";
			int ti=i;   //temp indices
			int tj=j;
			int b=this.findPoint(i,j);
			int a=this.findPoint(super.barN.get(i),j);   //the "mirror point" which is identified with the start point is approached from the rightward side
			UL[b]=1; //that location has now been *a launch point for a search*
		    int a1=-1; //placeholder variable
			while(a1!=a){ //from starting point on boundary, advances to exit point, adds the letter of that edge to the word
				a1=rightMost(ti,tj);
				loop=loop+super.LtoString(PL[a1][0]);  //adds the letter
				ti=this.barN.get(PL[a1][0]);          //updates the indices for the next step
				tj=PL[a1][1];
				UL[this.findPoint(ti,tj)]=1;    //this "mirror point" is a launch point
			}
			return loop;
		}
		
		//This method returns the "endpoint" of the sequence of arcs that intersects the first segment emanating from point with labels [i][j]
		public int rightMost(int i, int j)
		{	//first figure out whether point(i,j) is the END or beginning of segment.
			int[] startpoint=this.findSegmentContaining(i,j);
			int tempsegment=startpoint[0];
			int counter=-1;
			if(startpoint[1]==0) 			 //(letter i, point j) is the endpoint of a segment, so orientation disagrees
			{   int crm=SL[startpoint[0]][0];		 //current rightmost point - this is an index in PL
				int oldcrm=-1;
				while(oldcrm!=crm){
					oldcrm=crm;
					for(int k=0;k<curvw.length();k++)
					{	
						if(IntMat[tempsegment][k]==1)    //if another segment intersects your starting one, record the "rightmost" point wrt to that segment
						{ if(isclockwiseafter(SL[startpoint[0]][1],crm,SL[k][0])==true){crm=SL[k][0];counter=k;}
						  if(isclockwiseafter(SL[startpoint[0]][1],crm,SL[k][1])==true){crm=SL[k][1];counter=k;}
						}
					} 
					tempsegment=counter;
					}
				return crm;
			}
			if(startpoint[1]==1) 			 //(letter i, point j) is the endpoint of a segment, so orientation agrees
			{   int crm=SL[startpoint[0]][1];		 //current rightmost point - this is an index in PL
				int oldcrm=-1;
				while(oldcrm!=crm){
					oldcrm=crm;
					for(int k=0;k<curvw.length();k++)
					{	if(IntMat[tempsegment][k]==1)    //if another segment intersects your starting one, record the "rightmost" point wrt to that segment
						{ if(isclockwiseafter(SL[startpoint[0]][0],crm,SL[k][0])==true){crm=SL[k][0];counter=k;}
						  if(isclockwiseafter(SL[startpoint[0]][0],crm,SL[k][1])==true){crm=SL[k][1];counter=k;}
						}
					}tempsegment=counter;
				}
				return crm;
			}
		return -1; //if code reaches this point, there has been an error
		}
		
		//for a segment from PL[a] to PL[b], is the point c in between a & b in the clockwise direction?
		public boolean isclockwiseafter(int a,int b,int c)
		{
			int i=2*curvw.length();
			return (((c-a)%i)+i)%i > (((b-a)%i)+i)%i;
		}

		//returns a new starting location to check for the next disk, or returns -1 if none available
		public int nextAvailLoc()
		{
			for(int i=0;i<2*curvw.length();i++)
			{
				if(UL[i]==0){ return i;}
			}
			return -1;
		}

		//This method puts everything together
		public boolean DoesItFill(String w)
		{		int sp=nextAvailLoc();
			while(sp!=-1)
			{ String s=BuildLoop(PL[sp][0],PL[sp][1]);
			if(IsTrivial(s)==false) return false;
			sp=nextAvailLoc();
			}
			return true;
		}

		public int[][] getPL()
		{
			return PL;
		}
		
		public int[][] getSL()
		{
			return SL;
		}
		
		//returns row of PL containing letter a, index b;
		public int findPoint(int a, int b)
		{
		//	System.out.println(curvw);
			for(int j=0;j<(2*curvw.length());j++)
			{
				if (PL[j][0]==a && PL[j][1]==b) return j;
			}
		    return -1;
		}
		
		//returns the starting point (a1,b1) and ending point (a2,b2) of segment a
		public int[][] startEndPts(int a){
			int[][] data=new int[2][2];
			data[1][0]=SL[a][0];
			data[1][1]=SL[a][1];
			data[0][0]=SL[(a-1)%curvw.length()][0];
			data[0][1]=SL[(a-1)%curvw.length()][1];
			return data;
		}
		
		//returns segment containing letter a, index b; - first entry is the index of the segment, 2nd entry is whether it agrees or disagrees with the orientation; 0 = disagree
		public int[] findSegmentContaining(int a, int b)
		{
			int[] data=new int[2];
			for(int j=0;j<curvw.length();j++)
			{ if (PL[SL[j][0]][0]==a && PL[SL[j][0]][1]==b){ data[0]=j;data[1]=1;}  //segment ends with (a,b) and disagrees
			  if (PL[SL[j][1]][0]==a && PL[SL[j][1]][1]==b){data[0]=j;data[1]=0;} //segment starts with (a,b) and agrees
			}
			return data;
		}
		
		//returns true if segments i and j split in direction d, false if they don't
		public boolean split(int i, int j, int d)
		{
			if (d==1)
			{
				return !(PL[SL[i][1]][0]== PL[SL[j][1]][0]);	
			}
			if (d==2)
			{
				return !(PL[SL[i][1]][0]== PL[SL[j][0]][0]);
			}
			if (d==3)
			{
				return !(PL[SL[i][0]][0]== PL[SL[j][1]][0]);
			}
			if (d==4)
			{
				return !(PL[SL[i][0]][0]== PL[SL[j][0]][0]);
			}
			else return false;
		}
		
		//determines if two "segment words" SL[a] and SL[b] intersect
		public boolean intersect(int a,int b)
		{
			//first figure out which is closer to the beginning of PL
			int c;
			int[] rows=new int[4];	
			rows[0]=SL[a][0];
			rows[1]=SL[a][1];
			rows[2]=SL[b][0];
			rows[3]=SL[b][1];
			int l = 0;
			for(int i =0;i<4; i++)
			{	
				if(rows[l] > rows[i])
				{
					l = i;
				}
			}
			if (l==0 || l==1)c=a;
			else c=b;
			int d=a;
			if (c==a)d=b; 
			int i1=SL[c][0];
			int i2=SL[c][1];
			int j1=SL[d][0];
			int j2=SL[d][1];
			if(i1<j1 && j1<i2 && i2<j2) return true;
			if(i1<j2 && j2<i2 && i2<j1) return true;
			if(i2<j1 && j1<i1 && i1<j2) return true;
			if(i2<j2 && j2<i1 && i1<j1) return true;
			else return false;
		}
		
		//This method looks through the combinatorics to find two segments that cross
		public void findIntersection()
		{
			int count=0;		
			for (int i=0;i<curvw.length();i++)
			{
				for(int j=i+1;j<curvw.length();j++)
				{
					if (this.intersect(i,j)) 
					{
						count++;	
						if (this.potentialBigon(i,j)==1)
							{
								return;
							}
					}
				}
			}
			this.easierList();
			done=1;
			minInt=count;
		}
		
		public int getMinInt()
		{
			return minInt;
		}
		
		//returns 1 if the potential bigon is a proper bigon, 0 otherwise;  	
		public int potentialBigon(int i, int j)
		{
			int direction=0;
			int founddir=0;
			int check=0;
			BL1=new ArrayList();
			BL2=new ArrayList();
			BL1.add(i);
			BL2.add(j);
			//check what directions are valid;	
			if(founddir==0)
			{
				if (!this.split(i,j,1)) {direction=1; founddir=1;}
			}
			if(founddir==0)
			{
				if (!this.split(i,j,2)) {direction=2; founddir=1;}
			}
			if(founddir==0)
			{
				if (!this.split(i,j,3)) {direction=3; founddir=1;}
			}
			if(founddir==0)
			{
				if (!this.split(i,j,4)){ direction=4; founddir=1;}
			}
			//build a bigon in the direction specified above
			if(direction==1)
			{
				int k=1;
				for(int fk=1;fk<curvw.length();)
				{
					BL1.add(mod(i+k));
					BL2.add(mod(j+k));
					if(!this.split(mod(i+k), mod(j+k),direction) && !(this.intersect(mod(i+k), mod(j+k))))
					{
						fk++;
						k++;
					}
					else fk=fk+curvw.length();
				}
				if (this.intersect(mod(i+k), mod(j+k)))
				{
					check=1;
					int permute = this.isProper(BL1,BL2,direction);
					if(permute==1) this.permutePoints(BL1,BL2,PL,direction);
					return permute;
				}		
				if (this.split(mod(i+k),mod(j+k),direction) && check==0)
				{
					BL1=new ArrayList();
					BL1.add(i);
					BL2=new ArrayList();
					BL2.add(j);
					if (this.split(i, j,4)==false){ direction=4;}
			//		oppdir=1;
				}
			}
			if(direction==4)
			{
				int k=1;
					for(int fk=1;fk<curvw.length();)
					{
						BL1.add(mod(i-k));
						BL2.add(mod(j-k));
						if(!this.split(mod(i-k), mod(j-k),direction) && !(this.intersect(mod(i-k), mod(j-k))))
						{
							fk++;
							k++;
						}
						else fk=fk+curvw.length();
					}			
				if (this.intersect(mod(i-k), mod(j-k)))
				{
					check=1;
					int permute = this.isProper(BL1,BL2,direction);
					if(permute==1) this.permutePoints(BL1,BL2, PL,direction);
					return permute;
						
				}
				if (this.split(mod(i-k), mod(j-k),direction)==true && check==0)
				{
					BL1=new ArrayList();
					BL1.add(i);
					BL2=new ArrayList();
					BL2.add(j);	
					return 0;
				}
			}
			if(direction==2)
			{
				int k=1;
				for(int fk=1;fk<curvw.length();)
				{
					BL1.add(mod(i+k));
					BL2.add(mod(j-k));
					if(!this.split(mod(i+k), mod(j-k),direction) && !(this.intersect(mod(i+k),mod(j-k))))
					{
						fk++;
						k++;
					}
					else fk=fk+curvw.length();
				}
				if (this.intersect(mod(i+k),mod(j-k)))
				{
					check=1;
					int permute = this.isProper(BL1,BL2,direction);
					if(permute==1) this.permutePoints(BL1,BL2, PL,direction);
					return permute;
				}	
				if (split(mod(i+k),mod(j-k),direction)==true && check==0)
				{
					BL1=new ArrayList();
					BL1.add(i);
					BL2=new ArrayList();
					BL2.add(j);	
					if (split(i,j,3)==false){ 
						direction=3;}
			//		oppdir=1;
				}
			
			}
			if(direction==3)
			{
				int k=1;			
				for(int fk=1;fk<curvw.length();)
				{
					BL1.add(mod(i-k));
					BL2.add(mod(j+k));
					if(!this.split(mod(i-k),mod(j+k),direction) && !(this.intersect(mod(i-k),mod(j+k))))
					{
						fk++;
						k++;
					}
					else fk=fk+curvw.length();
				}

				if (this.intersect(mod(i-k),mod(j+k)))
				{
					check=1;
					int permute = this.isProper(BL1,BL2,direction);
					if(permute==1) this.permutePoints(BL1,BL2, PL,direction);
					return permute;
				}
				if (!this.split(mod(i-k),mod(j+k),direction) && check==0)
				{
					BL1=new ArrayList();
					BL1.add(i);
					BL2=new ArrayList();
					BL2.add(j);		
					return 0;
				}
			}
			return 0;
		}
		public int isProper(ArrayList a, ArrayList b, int d)
		{
			int count=0;
			for (int i=0;i<a.size();i++)
			{
				for (int j=0; j<b.size();j++)
				{
					if (a.get(i)==b.get(j)) count++;
				}
			}
			if (count>=2) return 0;
			else if (count==1)
			{
				//this is to make the leg with the common segment automatically the first one considered
				ArrayList a1=a;
				ArrayList b1=b;
				if (b.get(0)==a.get(b.size()-1))
				{ 	b1=a;
					a1=b;
				}
					/*stores the locations of all 8 points to reduce clutter in this section
					(p1,p2) (p3,p4)
					  .		.
					  .		.
					  .		.
					(p5,p6) (p7,p8)		
					*/		
				
				//define an array of int
				  Integer[] ia1  = new Integer[a1.size()];
				  Integer[] ib1  = new Integer[b1.size()];
				   //convert a List to Array
				   a1.toArray(ia1);
				   b1.toArray(ib1);
				   
					int p1=SL[ia1[0].intValue()][0];
					int p2=SL[ia1[0].intValue()][1];
					int p3=SL[ib1[0].intValue()][0];
					int p4=SL[ib1[0].intValue()][1];
					int p5=SL[ia1[a1.size()-1].intValue()][0];
					int p6=SL[ia1[a1.size()-1].intValue()][1];
				if (d==1)
				{		
						if((p2<p6 && p6<p4 && p1<p3 && p3<p5)||(p2>p6 && p6>p4 && p1>p3 && p3>p5))
						{
							return 0;	
						}
				}
				else return 1;
				if (d==4)
				{
					if (a1.get(0)==b1.get(b1.size()-1))
					{
						if((p2<p4 && p4<p6 && p1<p5 && p5<p3)||(p2>p4 && p4>p6 && p1>p5 && p5>p3))
						{
							return 0;	
						}	
					}
				}
				else return 1;			
			}
			return 1;
		}	
		
		//takes in the two legs of a proper bigon along with the associated direction, and permutes the points in PL accordingly
		public void permutePoints(ArrayList a,ArrayList b, int[][] p,int direction)
		{
			//define an array of int
			  Integer[] ia  = new Integer[a.size()];
			  Integer[] ib  = new Integer[b.size()];
			   //convert a List to Array
			   a.toArray(ia);
			   b.toArray(ib);				
			for (int i=0;i<a.size()-1;i++)
			{
				//in this implementation, one needs to permute SL as well, since the elements of SL are really just pointers to rows of PL
				if(direction==1)
				{
					this.permute(SL[ia[i].intValue()][1],SL[ib[i].intValue()][1]);
					this.permute(SL[ia[i+1].intValue()][0],SL[ib[i+1].intValue()][0]);
					int h0=SL[ia[i].intValue()][1];
					SL[ia[i].intValue()][1]=SL[ib[i].intValue()][1];
					SL[ib[i].intValue()][1]=h0;
					int h1=SL[ia[i+1].intValue()][0];
					SL[ia[i+1].intValue()][0]=SL[ib[i+1].intValue()][0];
					SL[ib[i+1].intValue()][0]=h1;	
				}
				if(direction==4)
				{
					this.permute(SL[ia[i].intValue()][0],SL[ib[i].intValue()][0]);
					this.permute(SL[ia[i+1].intValue()][1],SL[ib[i+1].intValue()][1]);
					int h0=SL[ia[i].intValue()][0];
					SL[ia[i].intValue()][0]=SL[ib[i].intValue()][0];
					SL[ib[i].intValue()][0]=h0;
					int h1=SL[ia[i+1].intValue()][1];
					SL[ia[i+1].intValue()][1]=SL[ib[i+1].intValue()][1];
					SL[ib[i+1].intValue()][1]=h1;
				}
				if(direction==2)
				{
					this.permute(SL[ia[i].intValue()][1],SL[ib[i].intValue()][0]);
					this.permute(SL[ia[i+1].intValue()][0],SL[ib[i+1].intValue()][1]);			
					int h0=SL[ia[i].intValue()][1];
					SL[ia[i].intValue()][1]=SL[ib[i].intValue()][0];
					SL[ib[i].intValue()][0]=h0;				
					int h1=SL[ia[i+1].intValue()][0];
					SL[ia[i+1].intValue()][0]=SL[ib[i+1].intValue()][1];
					SL[ib[i+1].intValue()][1]=h1;
				}
				if(direction==3)
				{
					this.permute(SL[ia[i].intValue()][0],SL[ib[i].intValue()][1]);
					this.permute(SL[ia[i+1].intValue()][1],SL[ib[i+1].intValue()][0]);					
					int h0=SL[ia[i].intValue()][0];
					SL[ia[i].intValue()][0]=SL[ib[i].intValue()][1];
					SL[ib[i].intValue()][1]=h0;
					int h1=SL[ia[i+1].intValue()][1];
					SL[ia[i+1].intValue()][1]=SL[ib[i+1].intValue()][0];
					SL[ib[i+1].intValue()][0]=h1;
				}
			}
		}
		
		//switches the locations in PL of rows i and j (recall that a row of PL represents a point)
		public void permute(int i, int j)
		{
			int h0=PL[i][0];
			int h1=PL[i][1];
			int h2=PL[j][0];
			int h3=PL[j][1];
			PL[i][0]=h2;
			PL[i][1]=h3;
			PL[j][0]=h0;
			PL[j][1]=h1;
		}
		
		public int getDone()
		{
			return done;
		}
		
		public void printPoints()
		{
			for (int i=0;i<2*curvw.length();i++)
			{
				System.out.println(this.toLetter.get(PL[i][0])+""+PL[i][1]);
			}	
		}
		
		public void printSegments()
		{
			for (int i=0;i<curvw.length();i++)
			{
				System.out.println(this.toLetter.get(PL[SL[i][0]][0])+""+PL[SL[i][0]][1]+" , "+this.toLetter.get(PL[SL[i][1]][0])+""+PL[SL[i][1]][1]);
			}
			
		}
		
		//gives the "combinatorial length" of the current representative given by PL and SL
		public int combLength()
		{
			int length=0;
			for(int i=0;i<curvw.length();i++)
			{
				int kmax=Math.max(SL[i][0], SL[i][1]);
				int kmin=Math.min(SL[i][0], SL[i][1]);
				length=length+Math.min(kmax-kmin,Math.abs(kmin+2*curvw.length()-kmax));
			}
			return length;
		}
		
		public int mod(int a)
		{
				if (a>curvw.length()-1) return a % curvw.length();
				else if (a<0) return a+curvw.length();
				else return a;
			
		}
		
		//InitIntMat row (i,j)=1 if segments with index i and j intersect
		public void InitIntMat()
		{
			for(int i=0;i<curvw.length();i++)
			{
				for(int j=0;j<curvw.length();j++)
				{
					if(intersect(i,j)==true){IntMat[i][j]=1;} else IntMat[i][j]=0;
				}
				System.out.println(Arrays.toString(IntMat[i]));
			}
		}
		
		//returns the number of points located on a given edge indexed by 'a'
		public int getNumPoints(int a)
		{
			int[] C = this.toNumber(curvw);
			int L=this.getSurfaceLength();
			M =new int[L];			
			//counts how many of each pair there are
			for( int i = 0; i<L;i++)
			{
				for (int j = 0;j<curvw.length();j++)
				{
					if ( C[j] == i)
					 {
						M[i]++;
					 }
				}
			}
			return M[a]+M[this.barN.get(a)];
			
		}
		
		public void easierList()
		{
			int k=0;
			for (int i = 0;i<surfw.length();i++)
			{
				boolean used;
				int con = this.barN.get(i);
				used=(con<i);
				if (!used)
				{
					for(int j=0;j<M[i]+M[con];j++)
					{	
						PL[k][0]=i;
						PL[k][1]=j;
						k++;
					}
				}
				else 
				{
					for(int j=M[i]+M[con]-1;j>=0;j--)
					{	
						PL[k][0]=i;
						PL[k][1]=j;
						k++;
					}
				}	
			}
		}
	}
