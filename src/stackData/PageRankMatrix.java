package stackData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class PageRankMatrix {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static ArrayList<String> fileNames1= new ArrayList<String>();
	public static ArrayList<String> fileNames2= new ArrayList<String>();
	public static Integer test[][]=new Integer[5765][5765];

	public static void main(String args[]) throws Exception 
	{
		final File folder = new File("E:/stackoverflow/output");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		String fileName="";

		for(File file:fileList)
		{
			String name=file.getName();
			//System.out.println(name.replaceAll(".txt", ""));
			fileNames1.add(name.replaceAll(".txt", ""));
			//System.out.println(fileNames);
		}

		fileNames2=fileNames1;
		/*System.out.println(fileNames1);
		System.out.println(fileNames1.indexOf("1000939"));
		test[1][1]=1;
		System.out.println(test[1][1]);*/
		for(int i=0;i<test.length;i++)
		{
			for(int j=0;j<test[i].length;j++)
			{
				test[i][j]=0;
			}
		}

		File file=new File("userId.txt");
		FileWriter fstream = new FileWriter(file, true); //true tells to append data.
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(fileNames1.toString());
		out.close();
		
		readTestData();
		//readDataBase();
	}

	public static void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String query="Select * from user_data";
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(query);
			// writeResultSet(resultSet);

			TreeMap<String, String> userData = new TreeMap<String, String>();
			TreeMap<String, String> newUserData = new TreeMap<String, String>();

			while (resultSet.next()) {

				List<Date> averageTime = new ArrayList<Date>();
				String userID = resultSet.getString("UserId");
				String questionIDs = resultSet.getString("questionIds");
				String answerIDs = resultSet.getString("answerIds");

				StringBuilder check=new StringBuilder(1024);
				String check1="";

				String answerData[]=answerIDs.split(",");

				AuthorData authorData=new AuthorData();

				//if(answerData.length>=5)
				//{
				int count=0,status=0;
				for(int i=0;i<answerData.length;i++)
				{
					status=authorData.checkAnswerData(answerData[i], count);
					if(status==1)
					{
						count++;
					}
				}

				if(count>=5)
				{
					userData.put(userID, answerIDs);

				}

			}


			for(Map.Entry<String,String> entry : userData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				TreeMap<String, Date> timeData = new TreeMap<String, Date>();
				ArrayList<String> user= new ArrayList<String>();

				int status=0;
				String answerData[]=value.split(",");
				for(int i=0;i<answerData.length;i++)
				{
					status=readAnswerData(answerData[i],timeData);
					if(status==1)
					{
						user.add(answerData[i]);

					}
				}

				if(fileNames1.contains(key))
				{
					newUserData.put(key,user.toString());	
				}

			}
			
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");
			
			for(Map.Entry<String,String> entry : newUserData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue().substring(1, entry.getValue().length()-1);
			String sql="Insert into stackoverflow.test_answer_data values (?,?) ";
			// Statements allow to issue SQL queries to the database
			preparedStatement=connect.prepareStatement(sql);
			preparedStatement.setString(1, key);
			preparedStatement.setString(2, value);
			// Result set get the result of the SQL query
			preparedStatement
					.executeUpdate();
			}
			//System.out.println(newUserData.size());
			connect.close();
			//pageRank(newUserData);

		} catch (Exception e) {
			throw e;
		}

	}

	public static int readAnswerData(String answerId,TreeMap<String, Date> timeData) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.answer_data where answerID=?";
			// Statements allow to issue SQL queries to the database
			preparedStatement=connect.prepareStatement(sql);
			preparedStatement.setString(1, answerId);
			// Result set get the result of the SQL query
			resultSet = preparedStatement
					.executeQuery();

			// writeResultSet(resultSet);


			int status=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());

			while (resultSet.next()) {

				String answer = resultSet.getString("answer");
				String question = resultSet.getString("questionId");
				Date time = resultSet.getDate("time");
				String vote = resultSet.getString("vote");
				String accepted = resultSet.getString("accepted");


				if(time.after(min) && time.before(max))
				{
					status=1;
					timeData.put(answerId, time);
				}
			}
			connect.close();
			return status;


		} catch (Exception e) {
			throw e;
		}
	}

	public static void readTestData() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.test_answer_data";
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sql);

			TreeMap<String, String> userData = new TreeMap<String, String>();
			// writeResultSet(resultSet);


			while (resultSet.next()) {

				String userId = resultSet.getString("userId");
				String answerIds = resultSet.getString("answerIds");
				
				userData.put(userId, answerIds);
				
			}
			connect.close();
		
            pageRank(userData);

		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void pageRank(TreeMap<String, String> userData) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sqlAnswer="select * from stackoverflow.answer_data where answerID=?";
			String sqlQuestion="select * from stackoverflow.question_data where questionID=?";

			// writeResultSet(resultSet);

			for(Map.Entry<String,String> entry : userData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String answerData[]=value.split(",");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
				java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());

				for(int i=0;i<answerData.length;i++)
				{
					String userId="";
					Date time= new Date();

					// Statements allow to issue SQL queries to the database
					preparedStatement=connect.prepareStatement(sqlAnswer);
					preparedStatement.setString(1, answerData[i]);
					// Result set get the result of the SQL query
					resultSet = preparedStatement
							.executeQuery();

					while (resultSet.next()) {
						String answer = resultSet.getString("answer");
						String question = resultSet.getString("questionId");


						preparedStatement=connect.prepareStatement(sqlQuestion);
						preparedStatement.setString(1, question);
						// Result set get the result of the SQL query
						resultSet = preparedStatement
								.executeQuery();

						while (resultSet.next()) {
							userId = resultSet.getString("userId");
							time= resultSet.getDate("time");
						}

						if(time.after(min) && time.before(max)&&fileNames1.contains(userId))
						{
							test[fileNames1.indexOf(userId)][fileNames2.indexOf(key)]=
									test[fileNames1.indexOf(userId)][fileNames2.indexOf(key)]+1;
						}

					}
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

			}


		} catch (Exception e) {
			throw e;
		}
	}
}
