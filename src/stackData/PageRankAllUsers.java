package stackData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class PageRankAllUsers {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public static ArrayList<String> allUsers= new ArrayList<String>();
	public static TreeMap<String,Integer> userLink=new TreeMap<String,Integer>();

	public static void main(String args[]) throws Exception 
	{
		readAnswerData();
		readQuestionData();
		
		File file=new File("C:/Users/kochharps.2012/Documents/My Courses/ANM/output/allUsers.txt");
		FileWriter fstream = new FileWriter(file, true); //true tells to append data.
		BufferedWriter out = new BufferedWriter(fstream);
		
		for(String str:allUsers)
		{
			out.write(str+";"+str+";"+"1");
			out.newLine();
		}
		out.close();
	}


	public static void readQuestionData() throws Exception {
		try {

			File file=new File("C:/Users/kochharps.2012/Documents/My Courses/ANM/output/questionData.txt");
			FileWriter fstream = new FileWriter(file, true); //true tells to append data.
			BufferedWriter out = new BufferedWriter(fstream);


			/*File fileCount=new File("C:/Users/kochharps.2012/Documents/My Courses/ANM/output/answerData.txt");
			FileWriter fstream1 = new FileWriter(fileCount, true); //true tells to append data.
			BufferedWriter out1 = new BufferedWriter(fstream1);*/


			// This will load the MySQL driver, each DB has its own driver
			//Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			//connect = DriverManager				.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			TreeMap<String,String> question=new TreeMap<String,String>();
			
			String sql="select * from stackoverflow.question_data";
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sql);

			// writeResultSet(resultSet);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());

			while (resultSet.next()) {

				String userId = resultSet.getString("userId");
				Date time = resultSet.getDate("time");
				String questionId = resultSet.getString("questionID");

				if(time.after(min) && time.before(max))
				{
					if(!(allUsers.contains(userId)))
					{
						allUsers.add(userId);
					}
					//allUsers.add(userId);
				question.put(questionId, userId);

				}
			}
			
				out.write(question.toString());
                out.close();
				
				/*	out1.write(answerData.toString());
			out.close();
			out1.close();*/
				//System.out.println(allUsers.size());

				/*Integer test[][]=new Integer[allUsers.size()][allUsers.size()];
			for(int i=0;i<test.length;i++)
			{
				for(int j=0;j<test[i].length;j++)
				{
					test[i][j]=0;
				}
			}*/

				/*for(Map.Entry<String,String> entry : questionData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				Set<String> keys=getKeysByValue(answerData, value);
				Iterator it=keys.iterator();
				while(it.hasNext())
				{
					test[allUsers.indexOf(key)][allUsers.indexOf(it.next())]=
							test[allUsers.indexOf(key)][allUsers.indexOf(it.next())]+1;
				}

			}
			connect.close();

			for(int i=0;i<test.length;i++)
			{
				for(int j=0;j<test[i].length;j++)
				{
					System.out.println(test[i][j]+",");
				}
				System.out.println("\n");
				System.out.println("------");

			}*/

			} catch (Exception e) {
				throw e;
			}
		}

		public static void readAnswerData() throws Exception {
			try {
				
				File file=new File("C:/Users/kochharps.2012/Documents/My Courses/ANM/output/answerQuestion.txt");
				FileWriter fstream = new FileWriter(file, true); //true tells to append data.
				BufferedWriter out = new BufferedWriter(fstream);


				File fileCount=new File("C:/Users/kochharps.2012/Documents/My Courses/ANM/output/answerUser.txt");
				FileWriter fstream1 = new FileWriter(fileCount, true); //true tells to append data.
				BufferedWriter out1 = new BufferedWriter(fstream1);
				
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
				java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());

				TreeMap<String,String> answerQuestion=new TreeMap<String,String>();
				TreeMap<String,String> answerUser=new TreeMap<String,String>();
				
				String sql="select * from stackoverflow.answer_data";
				// Statements allow to issue SQL queries to the database
				statement = connect.createStatement();
				// Result set get the result of the SQL query
				resultSet = statement
						.executeQuery(sql);
				while (resultSet.next()) {

					String userId = resultSet.getString("userId");
					Date time = resultSet.getDate("time");
					String questionId = resultSet.getString("questionId");
					String answerId = resultSet.getString("answerID");

					if(!(time==null))
					{
						if(time.after(min) && time.before(max))
						{
							if(!(allUsers.contains(userId)))
							{
								allUsers.add(userId);
							}
							answerQuestion.put(answerId, questionId);
							answerUser.put(answerId, userId);

						}
					}

				}

				out.write(answerQuestion.toString());
				out1.write(answerUser.toString());
				out.close();
				out1.close();
				
				
			} catch (Exception e) {
				throw e;
			}
		}

		public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
			Set<T> keys = new HashSet<T>();
			for (Entry<T, E> entry : map.entrySet()) {
				if (value.equals(entry.getValue())) {
					keys.add(entry.getKey());
				}
			}
			return keys;
		}
	}
