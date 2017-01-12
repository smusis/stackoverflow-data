package stackData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;



public class DateTime {

	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	public static void main(String args[]) throws Exception 
	{
		String datetimeString = "2010-05-23 23:32:45Z";
		String datetimeString1 = "2012-05-23 23:32:45Z";
		System.out.println(datetimeString.substring(0, datetimeString.length()-1));
		Date result,result1,re; 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		result = formatter.parse (datetimeString); 
		result1 = formatter.parse (datetimeString1); 
		java.sql.Date sqlDate = new java.sql.Date(result.getTime()); 
		java.sql.Time sqlTime = new java.sql.Time(result.getTime()); 
		System.out.println("SQL date " + sqlDate + " " + sqlTime);

		java.sql.Date sqlDate1 = new java.sql.Date(result1.getTime()); 
		java.sql.Time sqlTime1 = new java.sql.Time(result1.getTime()); 
		System.out.println("SQL date " + sqlDate1 + " " + sqlTime1);

		if(sqlDate1.after(sqlDate))
		{
			System.out.println("true");
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
		java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());
		java.sql.Date time=new java.sql.Date(format.parse ("2012-06-30").getTime());

		if(time.after(min) && time.before(max))
		{
			System.out.println("true");
		}
		else
		{
			System.out.println("false");
		}

		readDataBase();
	}

	/*public static void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			String query="Select * from user_data";
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(query);
			// writeResultSet(resultSet);
int count=0;
			System.out.println(resultSet);
			while (resultSet.next()) {
				StringBuilder content=new StringBuilder(1024);
				String userID = resultSet.getString("UserId");
				String questionIDs = resultSet.getString("questionIds");
				String answerIDs = resultSet.getString("answerIds");

				System.out.println(userID);
				//String questionData[]=questionIDs.split(",");

				count++;
			}
System.out.println(count);
		} catch (Exception e) {
			throw e;
		}

	}*/

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




			System.out.println(resultSet.getFetchSize());
			while (resultSet.next()) {

				List<Date> averageTime = new ArrayList<Date>();
				String userID = resultSet.getString("UserId");
				String questionIDs = resultSet.getString("questionIds");
				String answerIDs = resultSet.getString("answerIds");

				StringBuilder check=new StringBuilder(1024);
				String check1="";
				//System.out.println(userID);
				/*String questionData[]=questionIDs.split(",");

				for(int i=0;i<questionData.length;i++)
				{
					readQuestionData(questionData[i],content);
				}*/

				File file=new File("E:/stackoverflow/output/"+userID+".txt");
				FileWriter fstream = new FileWriter(file, true); //true tells to append data.
				BufferedWriter out = new BufferedWriter(fstream);

				File file1=new File("E:/stackoverflow/voteOutput/"+userID+".txt");
				FileWriter fstream1 = new FileWriter(file1, true); //true tells to append data.
				BufferedWriter out1 = new BufferedWriter(fstream1);

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
					for(int i=0;i<answerData.length;i++)
					{
						StringBuilder content=new StringBuilder(1024);
						StringBuilder repContent=new StringBuilder(1024);
						TreeMap<String, String> voteData = new TreeMap<String, String>();


						authorData.readAnswerData(answerData[i],content,voteData,count);
						out.write(content.toString());
						out.write(System.getProperty("line.separator"));
						check=check.append(content);

						out1.write(voteData.toString());
						out1.write(System.getProperty("line.separator"));
						check1=check1+voteData.toString();
					}
				}
				
								//}

				final StringTokenizer ps = new StringTokenizer(check1, " \t\n\r\f.,;:!?'{}#+=~|0"); 

				//file.delete();
				/*String line="";
				final BufferedReader br = new BufferedReader(new FileReader(file));
				if ((line=br.readLine()) != null ) {
					System.out.println("Line"+line);
					System.out.println(file.length());
				}*/


				//br.close();
				/*if(content.toString().isEmpty()&&content.toString()=="")
				{
					file.delete();
				}*/
				//System.out.println(System.getProperty("line.separator"));
				out.close();
				out1.close();

				if(check.toString().isEmpty())
				{
					authorData.fileDelete(file);
				}
				if(ps.countTokens()==0)
				{
					authorData.fileDelete(file1);
				}

			}
			connect.close();

		} catch (Exception e) {
			throw e;
		}

	}
}
