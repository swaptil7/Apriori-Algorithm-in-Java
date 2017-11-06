import java.util.*;
import java.io.*;

class Ap{

	ArrayList<HashSet<String>> items;
	ArrayList<HashSet<String>> resultlist;
	ArrayList<HashSet<String>> tempresult;
	int n;
	double support;
	int flag;
	HashMap<String,Integer> table;
	HashMap<HashSet<String>,Integer> tab;

	Ap(int n,int support)
	{
		items = new ArrayList<HashSet<String>>();
		tab = new HashMap<HashSet<String>,Integer>();
		table = new HashMap<String,Integer>();
		resultlist = new ArrayList<HashSet<String>>();
		tempresult = new ArrayList<HashSet<String>>();
		this.n = n;
		this.support = (double)support*n/100;
		flag = 0;
	}

	void createDatabase()
	{
		try
		{
			FileReader filereader = new FileReader("input.txt");
			BufferedReader buf = new BufferedReader(filereader);
			Scanner inp = new Scanner(buf);
			for(int i=0;i<n;i++)
			{
					HashSet<String> tempItems = new HashSet<String>();
					String line = inp.nextLine();
					Scanner delim = new Scanner(line).useDelimiter("\\s+");
					while(delim.hasNext())
					{
						tempItems.add(delim.next());
					}
					items.add(tempItems);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not Found"+e.getMessage());
		}
	}

	void displayDB()
	{
		System.out.println("Displaying Database........");
		for(int i=0;i<n;i++)
		{
			System.out.println(items.get(i));
		}
	}

	void applyAlg()
	{
		System.out.println("Hello");
		while(true)
		{
			if(flag == 0)
			{
				table = new HashMap<String,Integer>();
				for(int i=0;i<n;i++)
				{
					for(String s : items.get(i))
					{
						if(!table.containsKey(s))
						{
							table.put(s,1);
						}
						else
						{
							Integer temp = table.get(s);
							temp++;
							table.put(s,temp);
						}
					}
				}
				displayTab(1);
				System.out.println();
				remove(1);
				HashSet<String> result;
				tempresult = new ArrayList<HashSet<String>>();
				for(Iterator<Map.Entry<String,Integer>> it = table.entrySet().iterator();it.hasNext();)
				{
					Map.Entry<String,Integer> entry = it.next();
					System.out.println(entry.getKey()+":"+entry.getValue());
					result = new HashSet<String>();
					result.add(entry.getKey());
					tempresult.add(result);
					resultlist.add(result);
				}

			}
			else
			{
				ArrayList<HashSet<String>> hs = findcandidates(tempresult);
				if(hs.size() == 0)
					break;
				tab = new HashMap<HashSet<String>,Integer>();
				for(HashSet<String> h : hs)
				{
					if(h.size() == flag + 1)
						tab.put(h,0);
				}

				for(Iterator<Map.Entry<HashSet<String>,Integer>> it = tab.entrySet().iterator();it.hasNext();)
				{
					Map.Entry<HashSet<String>,Integer> entry = it.next();
					for(int i=0;i<n;i++)
					{
						if(items.get(i).containsAll(entry.getKey()))
						{
							//System.out.println("key:"+tempset1+tempset2);
							Integer temp = tab.get(entry.getKey());
							temp++;
							tab.put(entry.getKey(),temp);
						}
					}
				}

				displayTab(2);
				remove(2);
				System.out.println("----------------------------------------------");
				HashSet<String> result;
				tempresult = new ArrayList<HashSet<String>>();
				for(Iterator<Map.Entry<HashSet<String>,Integer>> it = tab.entrySet().iterator();it.hasNext();)
				{
					Map.Entry<HashSet<String>,Integer> entry = it.next();
					System.out.println(entry.getKey()+":"+entry.getValue());
					result = new HashSet<String>();
					result.addAll(entry.getKey());
					tempresult.add(result);
					resultlist.add(result);
				}		
				tab.clear();
			}
			flag++;
		}
	}
			

	void displayTab(int j)
	{
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
		if(j == 1)
		{
			for(Iterator<Map.Entry<String,Integer>> it = table.entrySet().iterator();it.hasNext();)
			{
				Map.Entry<String,Integer> entry = it.next();
				System.out.println(entry.getKey()+":"+entry.getValue());
			}
		}
		else
		{
			for(Iterator<Map.Entry<HashSet<String>,Integer>> it = tab.entrySet().iterator();it.hasNext();)
			{
				Map.Entry<HashSet<String>,Integer> entry = it.next();
				System.out.println(entry.getKey()+":"+entry.getValue());
			}
		}
	}

	ArrayList<HashSet<String>> findcandidates(ArrayList<HashSet<String>> li)
	{
		ArrayList<HashSet<String>> lis = new ArrayList<HashSet<String>>();
		for(int i=0;i<li.size();i++)
		{
			for(int j=i+1;j<li.size();j++)
			{
				//System.out.println(li.get(i)+":"+li.get(j));
				lis.add(unite(li.get(i),li.get(j)));
			}
		}
		return lis;
	}

	HashSet<String> unite(HashSet<String> a,HashSet<String> b)
	{
		HashSet<String> d = new HashSet<String>();
		for(String s:a)	
		{
			d.add(s);
		}
		for(String s:b)	
		{
			d.add(s);
		}
		return d;
	}

	void remove(int j)
	{
		if(j == 1)
		{
			for(Iterator<Map.Entry<String,Integer>> it = table.entrySet().iterator();it.hasNext();)
			{
				Map.Entry<String,Integer> entry = it.next();
				if(entry.getValue() < support)
					it.remove();
			}
		}
		else
		{
			for(Iterator<Map.Entry<HashSet<String>,Integer>> it = tab.entrySet().iterator();it.hasNext();)
			{
				Map.Entry<HashSet<String>,Integer> entry = it.next();
				if(entry.getValue() < support)
					it.remove();
			}
		}
	}

	public static void main(String[] args)
	{
		Ap alg = new Ap(6,50);
		alg.createDatabase();
		alg.displayDB();
		alg.applyAlg();
	}
}
