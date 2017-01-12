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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class AverageTimeUser {

	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static TreeMap<String, Integer> userActivity = new TreeMap<String, Integer>();

	public static void main(String args[]) throws Exception 
	{
		readDataBase();
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

				String answerData[]=value.split(",");
				for(int i=0;i<answerData.length;i++)
				{
					readAnswerData(answerData[i],timeData);

				}
				timeData=(TreeMap<String, Date>) sortByValues(timeData);
				DateTime dt = new DateTime(timeData.firstEntry().getValue());
				DateTime dt1 = new DateTime("2012-12-01");
				int days = Days.daysBetween(dt, dt1).getDays();
				userActivity.put(key, days);
			}

			File file=new File("E:/stackoverflow/userActivity.txt");
			FileWriter fstream = new FileWriter(file, true); //true tells to append data.
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(userActivity.toString());
			out.close();
			connect.close();

		} catch (Exception e) {
			throw e;
		}

	}

	public static void readAnswerData(String answerId,TreeMap<String, Date> timeData) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager	.getConnection("jdbc:mysql://localhost:3306/stackoverflow","root","mysql");

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
					timeData.put(answerId, time);

				}

			}

			connect.close();
		} catch (Exception e) {
			throw e;
		}
	}

	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator =  new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k2).compareTo(map.get(k1));
				if (compare == 0) return 1;
				else return compare;
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		//System.out.println(sortedByValues);
		return sortedByValues;
	}
}
