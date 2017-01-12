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
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class UserFrequency {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static TreeMap<String, Integer> map=new TreeMap<String, Integer>();
	public static TreeMap<Integer, Integer> frequencyMap=new TreeMap<Integer, Integer>();
	public static void main(String args[]) throws Exception 
	{
		readAnswerData();
	}

	public static void readAnswerData() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.answer_data ";
			// Statements allow to issue SQL queries to the database
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sql);

			// writeResultSet(resultSet);


			int status=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-12-31").getTime());

			while (resultSet.next()) {

				String answer = resultSet.getString("answer");
				String question = resultSet.getString("questionId");
				java.sql.Date time = resultSet.getDate("time");
				String vote = resultSet.getString("vote");
				String accepted = resultSet.getString("accepted");
				String userId=resultSet.getString("userId");
				int value=0;

				if(time!=null && time.after(min) && time.before(max))
				{
					if(map.containsKey(userId))
					{
						value=map.get(userId);
						value=value+1;
						map.put(userId, value);
					}
					else
					{
						value=1;
						map.put(userId, value);
						//System.out.println(map);
						//System.exit(1);
					}
				}
			}

			connect.close();
			/*for(Map.Entry<String,Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value1 = entry.getValue();
				System.out.println((value1));
			}*/
			readQuestionData(map);

		} catch (Exception e) {
			throw e;
		}
	}

	public static void readQuestionData(TreeMap<String, Integer> map) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.question_data";
			// Statements allow to issue SQL queries to the database
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sql);

			// writeResultSet(resultSet);

			File file=new File("userFrequency.txt");
			FileWriter fstream = new FileWriter(file, true); //true tells to append data.
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("frequency <- c(");
			
			File fileCount=new File("userCount.txt");
			FileWriter fstream1 = new FileWriter(fileCount, true); //true tells to append data.
			BufferedWriter out1 = new BufferedWriter(fstream1);
			out1.write("Count <- c(");

			int status=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-12-31").getTime());

			while (resultSet.next()) {
				Date time = resultSet.getDate("time");
				String userId=resultSet.getString("userId");

				int value=0;
				if(time.after(min) && time.before(max))
				{
					if(!(map.containsKey(userId)))
					{
						value=0;
						map.put(userId, value);
					}

				}
			}
			connect.close();

			int frequency=0;
			for(Map.Entry<String,Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value1 = entry.getValue();

				frequencyMap.put(value1, 0);

			}
			
			for(Map.Entry<String,Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value1 = entry.getValue();
				
				if(frequencyMap.containsKey(value1))
				{
					frequency=frequencyMap.get(value1);
					frequency=frequency+1;
					frequencyMap.put(value1, frequency);
				}
				else
				{
					frequency=1;
					frequencyMap.put(value1, frequency);
					//System.out.println(map);
					//System.exit(1);
				}
			}

			for(Map.Entry<Integer,Integer> entry : frequencyMap.entrySet()) {
				Integer key = entry.getKey();
				Integer values = entry.getValue();
				out1.write(key+", ");
				out.write(values+", ");
			}

			out.write(")");
			out.close();
			out1.write(")");
			out1.close();

		} catch (Exception e) {
			throw e;
		}
	}

}
