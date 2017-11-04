import java.util.*;
import java.io.*;

class Apriori
{
	ArrayList<ArrayList<String>> trans; 
	int num_trans;
	File file;
	int support;
	ArrayList<String> set;
	ArrayList<String> resultset;

	Apriori(String f,int s)
	{
		trans = new ArrayList<ArrayList<String>>();
		num_trans = 0;
		file = new File(f);
		support = s;
		resultset = new ArrayList<String>();
	}

	void createDatabase()
	{
		try
		{
			FileReader filereader = new FileReader(file);
			BufferedReader buf = new BufferedReader(filereader);
			Scanner s = new Scanner(buf);
			String elements;
			Scanner in;

			while(s.hasNext())
			{
				num_trans++;
				elements = s.nextLine();
				in = new Scanner(elements).useDelimiter("\\s+");;
				
				ArrayList<String> ar = new ArrayList<String>();				
				while(in.hasNext())
				{
					ar.add(in.next());
				}

				trans.add(ar);
				in.close();
			}

			System.out.println("---------------------------------------------------");
			System.out.println("Transaction Database:");
			int count = 1;
			for(ArrayList<String> ar : trans)
			{
				System.out.println((count++)+"-->"+ar);
			}
			System.out.println("---------------------------------------------------");

			s.close();

		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");			
		}
		
	}


	void applyalg()
	{
		set = stage1();

		set = stage2();

		stage3();

		System.out.println("---------------Final Output------------------");
		System.out.println("final associated items are::");
		System.out.println(resultset);
	}

	ArrayList<String> stage1()
	{
		HashMap<String,Integer> table = new HashMap<String,Integer>();

		for(ArrayList<String> ar : trans)
		{
			for(String s : ar)
			{
				if(!table.containsKey(s))
				{
					table.put(s,new Integer(1));
				}
				else
				{
					Integer temp = table.get(s);
					temp++;
					table.put(s,temp);
				}
			}
		}

		System.out.println("--------------------Before Support (stage 1)-------------------------------");
		for(Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator(); it.hasNext(); ) 
		{
      		Map.Entry<String, Integer> entry = it.next();
			System.out.println(entry.getKey()+":"+entry.getValue());
      		if(entry.getValue().intValue() <= support)  
        		it.remove();
  		}

		System.out.println("---------------------------------------------------");


  		System.out.println();


  		ArrayList<String> set = new ArrayList<String>();

		System.out.println("--------------------After Support (Stage 1)-------------------------------");

		for(Map.Entry<String, Integer> entry : table.entrySet()) 
		{
		    System.out.println(entry.getKey()+":"+entry.getValue());
		    set.add(entry.getKey());
		}

		resultset.addAll(set);

		System.out.println("---------------------------------------------------");

		return set;

	}

	ArrayList<String> stage2()
	{
		HashMap<String,Integer> table = new HashMap<String,Integer>();
		
		for(int i=0;i<set.size();i++)
		{
			for(int j=i;j<set.size();j++)
			{
				if(i<j)
				{
					String key = set.get(i) + "," + set.get(j);
					for(ArrayList<String> ar : trans)
					{
						if(ar.contains(set.get(i)) && ar.contains(set.get(j)))
						{
							if(!table.containsKey(key))
							{
								table.put(key,new Integer(1));
							}
							else
							{
								Integer temp = table.get(key);
								temp++;
								table.put(key,temp);
							}
						}
					}
				}
			}
		}

		System.out.println();

		System.out.println("------------------Before Support (Stage 2)---------------------------------");

		for(Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator(); it.hasNext(); ) 
		{
      		Map.Entry<String, Integer> entry = it.next();
			System.out.println(entry.getKey()+":"+entry.getValue());
      		if(entry.getValue().intValue() <= support)  
        		it.remove();
  		}


		System.out.println("---------------------------------------------------");

  		System.out.println();

  		ArrayList<String> set = new ArrayList<String>();

		System.out.println("--------------------After Support (Stage 2)-------------------------------");

		for(Map.Entry<String, Integer> entry : table.entrySet()) 
		{
		    System.out.println(entry.getKey()+":"+entry.getValue());
		    resultset.add("("+entry.getKey()+")");
		    Scanner n = new Scanner(entry.getKey()).useDelimiter("\\,+");
		    while(n.hasNext())
		    {
		    	String key = n.next();
		    	if(!set.contains(key))
		    		set.add(key);
		    }
		}
		System.out.println("---------------------------------------------------");

		return set;
	}


	void stage3()
	{
		HashMap<String,Integer> table = new HashMap<String,Integer>();
		
		for(int i=0;i<set.size();i++)
		{
			for(int j=i;j<set.size();j++)
			{
				for(int k=j;k<set.size();k++)
				{
					if(i<j && j<k && i<k)
					{
						String key = set.get(i) + "," + set.get(j) + "," + set.get(k);
						for(ArrayList<String> ar : trans)
						{
							if(ar.contains(set.get(i)) && ar.contains(set.get(j)) && ar.contains(set.get(k)))
							{
								if(!table.containsKey(key))
								{
									table.put(key,new Integer(1));
								}
								else
								{
									Integer temp = table.get(key);
									temp++;
									table.put(key,temp);
								}
							}
						}
					}
				}
			}
		}

		System.out.println();

		System.out.println("--------------------Before Support (Stage 3)-------------------------------");


		for(Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator(); it.hasNext(); ) 
		{
      		Map.Entry<String, Integer> entry = it.next();
			System.out.println(entry.getKey()+":"+entry.getValue());
      		if(entry.getValue().intValue() <= support)  
        		it.remove();
  		}

		System.out.println("---------------------------------------------------");


  		System.out.println();

		System.out.println("---------------------After Support (Stage 3)------------------------------");


		for(Map.Entry<String, Integer> entry : table.entrySet()) 
		{
		    System.out.println(entry.getKey()+":"+entry.getValue());
		    resultset.add("("+entry.getKey()+")");
		}

		System.out.println("---------------------------------------------------");

	}

	public static void main(String args[])
	{
		Apriori alg = new Apriori("input.txt",2);
		alg.createDatabase();
		alg.applyalg();
	}

}
