package stackData;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.TreeMap;

public class StackDB {


	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;


	public ResultSet readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/engine","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from stackoverflow.user_data");
			// writeResultSet(resultSet);
			return resultSet;

			/* // PreparedStatements can use variables and are more efficient
      preparedStatement = connect
          .prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
      // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
      // Parameters start with 1
      preparedStatement.setString(1, "Test");
      preparedStatement.setString(2, "TestEmail");
      preparedStatement.setString(3, "TestWebpage");
      preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
      preparedStatement.setString(5, "TestSummary");
      preparedStatement.setString(6, "TestComment");
      preparedStatement.executeUpdate();

      preparedStatement = connect
          .prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);

      // Remove again the insert comment
      preparedStatement = connect
      .prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
      preparedStatement.setString(1, "Test");
      preparedStatement.executeUpdate();

      resultSet = statement
      .executeQuery("select * from FEEDBACK.COMMENTS");
      writeMetaData(resultSet);*/

		} catch (Exception e) {
			throw e;
		}

	}

	public void writeUserData(TreeMap<String, String> questionData) throws Exception
	{
		int status=0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/engine","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query

			ResultSet resultSet=readDataBase();
			while (resultSet.next()) {

				String userID = resultSet.getString("UserId");
				String user=questionData.get("UserID");

				if(userID.equals(user))
				{
					status=1;

					String questionID = resultSet.getString("questionIds");
					if(questionID.equals(""))
					{
						questionID=questionData.get("QuestionID");
					}
					else
					{
						questionID=questionID+","+questionData.get("QuestionID");
					}
					preparedStatement = connect
							.prepareStatement("UPDATE stackoverflow.user_data SET questionIds = ? WHERE userId = ?");
					preparedStatement.setString(1, questionID);
					preparedStatement.setString(2, userID);
					preparedStatement.executeUpdate();
				}
			}

			// PreparedStatements can use variables and are more efficient
			if(status==0)
			{
				preparedStatement = connect
						.prepareStatement("insert into  stackoverflow.user_data values (?, ?, ?, ?)");
				// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
				// Parameters start with 1
				preparedStatement.setString(1, questionData.get("UserID"));
				preparedStatement.setString(2, questionData.get("UserName"));
				preparedStatement.setString(3, questionData.get("QuestionID"));
				if(questionData.containsKey("AnswerId"))
				{
					preparedStatement.setString(4, questionData.get("AnswerId"));
				}
				else
				{
					preparedStatement.setString(4, "");
				}

				preparedStatement.executeUpdate();
			}

			connect.close();


		} catch (Exception e) {
			throw e;
		}
	}

	public void writeUserAnswerData(TreeMap<String, String> questionData) throws Exception
	{
		int status=0;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/engine","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query

			ResultSet resultSet=readDataBase();
			while (resultSet.next()) {

				String userID = resultSet.getString("userId");

				if(userID.equals(questionData.get("UserID")))
				{
					status=1;
					String answerID = resultSet.getString("answerIds");
					if(answerID.equals(""))
					{
						answerID=questionData.get("AnswerId");
					}
					else
					{
						answerID=answerID+","+questionData.get("AnswerId");
					}
					preparedStatement = connect
							.prepareStatement("UPDATE stackoverflow.user_data SET answerIds = ? WHERE userId = ?");
					preparedStatement.setString(1, answerID);
					preparedStatement.setString(2, userID);
					preparedStatement.executeUpdate();
				}
			}

			// PreparedStatements can use variables and are more efficient
			if(status==0)
			{
				preparedStatement = connect
						.prepareStatement("insert into  stackoverflow.user_data values (?, ?, ?, ?)");
				// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
				// Parameters start with 1
				if(questionData.containsKey("UserID"))
				{
					preparedStatement.setString(1, questionData.get("UserID"));
				}
				else
				{
					preparedStatement.setString(1, "");
				}
				if(questionData.containsKey("UserName"))
				{
					preparedStatement.setString(2, questionData.get("UserName"));
				}
				else
				{
					preparedStatement.setString(2, "");
				}


				preparedStatement.setString(3, "");
				if(questionData.containsKey("AnswerId"))
				{
					preparedStatement.setString(4, questionData.get("AnswerId"));
				}
				else
				{
					preparedStatement.setString(4, "");
				}

				preparedStatement.executeUpdate();
			}
			connect.close();


		} catch (Exception e) {
			throw e;
		}
	}

	public void writeQuestionData(TreeMap<String, String> questionData) throws Exception
	{
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/engine","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  stackoverflow.question_data values (?, ?, ?, ?, ?, ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
			// Parameters start with 1

			preparedStatement.setString(1, questionData.get("QuestionID"));
			preparedStatement.setString(2, questionData.get("UserID"));
			preparedStatement.setString(3, questionData.get("Title"));
			preparedStatement.setString(4, questionData.get("Description"));

			if(questionData.containsKey("UpVote"))
			{

				preparedStatement.setString(5, questionData.get("UpVote"));
			}
			else
			{

				preparedStatement.setString(5, questionData.get("DownVote"));
			}

			preparedStatement.setString(6, questionData.get("Time"));
			preparedStatement.setString(7, questionData.get("Tags"));


			preparedStatement.executeUpdate();

			connect.close();


		} catch (Exception e) {
			throw e;
		} 
	}

	public void writeAnswerData(TreeMap<String, String> questionData) throws Exception
	{
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/engine","root","mysql");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  stackoverflow.answer_data values (?, ?, ?, ?, ?, ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
			// Parameters start with 1

			preparedStatement.setString(1, questionData.get("AnswerId"));
			preparedStatement.setString(2, questionData.get("UserID"));
			preparedStatement.setString(3, questionData.get("QuestionID"));
			preparedStatement.setString(4, questionData.get("Answer"));

			if(questionData.containsKey("UpVote"))
			{

				preparedStatement.setString(5, questionData.get("UpVote"));
			}
			else
			{

				preparedStatement.setString(5, questionData.get("DownVote"));
			}

			preparedStatement.setString(6, questionData.get("Time"));
			preparedStatement.setString(7, questionData.get("Accepted"));


			preparedStatement.executeUpdate();

			connect.close();


		} catch (Exception e) {
			throw e;
		} 
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		//   Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String url = resultSet.getString("url");
			String language = resultSet.getString("language");
			String sloc = resultSet.getString("sloc");
			/* Date date = resultSet.getDate("datum");
      String comment = resultSet.getString("comments");*/
			System.out.println("User: " + url);
			System.out.println("Website: " + language);
			System.out.println("Summery: " + sloc);
			/*  System.out.println("Date: " + date);
      System.out.println("Comment: " + comment);*/
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

} 

