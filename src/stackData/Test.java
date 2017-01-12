package stackData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static Integer test[][]=new Integer[3][3];
	public static ArrayList<String> fileNames1= new ArrayList<String>();
	public static void main(String args[]) throws Exception 
	{
		for(int i=0;i<test.length;i++)
		{
			for(int j=0;j<test[i].length;j++)
			{
				test[i][j]=0;
				
			}
			
		}
		/*ArrayList<String> fileNames1= new ArrayList<String>();
		fileNames1.add("kdfjglfd");
		String str=fileNames1.toString();
		System.out.println(str.substring(1,str.length()-1));
		File file=new File("matrix.txt");
		FileWriter fstream = new FileWriter(file, true); //true tells to append data.
		BufferedWriter out = new BufferedWriter(fstream);
		for(int i=0;i<test.length;i++)
		{
			for(int j=0;j<test[i].length;j++)
			{
				test[i][j]=1;
				
			}
			
		}
		
		for(int i=0;i<test.length;i++)
		{
			for(int j=0;j<test[i].length;j++)
			{
				System.out.println(test[i][j]+",");
				out.write(test[i][j].toString());
				out.write("  ");
			}
			out.write("---------");
			out.write("  ");
			System.out.print("\n");
		}
		
		//out.write(test.toString());
		out.close();*/
		//String line="";
	/*	final BufferedReader br = new BufferedReader(new FileReader("C:/Users/kochharps.2012/Documents/Applications/stackOverflow/log")); 
		StringBuilder content=new StringBuilder(4096);
		while((line=br.readLine())!=null)
		{
			content.append(line);
		} 
		
		//System.out.println(content.toString().trim());
		String str[]=content.toString().trim().split("------");
		System.out.println(content.toString().indexOf("------"));
		
		File file=new File("final.txt");
		FileWriter fstream = new FileWriter(file, true); //true tells to append data.
		BufferedWriter out = new BufferedWriter(fstream);
		
		StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        String str[]=sb.toString().trim().split("------");
        
        
        for(int i=0;i<str.length;i++)
        {
        	 String trim=str[i].trim();
        	 String str1[]=trim.substring(2,trim.length()-1).split(",");
        	 for(int j=0;j<str1.length;j++)
             {
        		 content.append(str1[j]+",");
             }
        	 out.write(content.toString().substring(0, content.length()-1));
        	 out.newLine();
        }*/
       
        //System.out.println(str1.length);
		//System.out.println(str[0].substring(0, str[0].length()-1));
		
	/*	final File folder = new File("E:/stackoverflow/output");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		String fileName="";

		for(File file:fileList)
		{
			String name=file.getName();
			//System.out.println(name.replaceAll(".txt", ""));
			fileNames1.add(name.replaceAll(".txt", ""));
			//System.out.println(fileNames);
		}
		if(fileNames1.contains("1291744"))
		{
			test[fileNames1.indexOf("1000939")][fileNames1.indexOf("1291744")]=	test[fileNames1.indexOf("1000939")][fileNames1.indexOf("1291744")]+1;
		System.out.println(test[fileNames1.indexOf("1000939")][fileNames1.indexOf("1291744")]);
		}
		//test[fileNames1.indexOf("1000939")][fileNames1.indexOf("1291744")]=	test[fileNames1.indexOf("1000939")][fileNames1.indexOf("1291744")]+1;
		System.out.println(fileNames1.indexOf("1291744"));*/
		final BufferedReader br = new BufferedReader(new FileReader("E:/Research Projects/ICSE project/JavaProjects.txt")); 
		File file=new File("E:/Research Projects/ICSE project/JavaProjects2.txt");
		FileWriter fstream = new FileWriter(file, true); //true tells to append data.
		BufferedWriter out = new BufferedWriter(fstream);
		
		StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        String line1="";
        while (line != null) {
            String str[]=line.split("/");
            line1=str[0]+"_"+str[1]+"/"+str[1];
            //System.out.println(line1);
            out.write(line1);
            out.newLine();
            line = br.readLine();
        }
        out.close();
	}
}
