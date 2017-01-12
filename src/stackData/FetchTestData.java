package stackData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class FetchTestData {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	public static TreeMap<String, String> acceptedAnswer = new TreeMap<String, String>();

	public void readQuestionData() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.test_data";
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sql);

			// writeResultSet(resultSet);

			int status=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());
            
			List<String> list = new ArrayList<String>();
			String title="",question="";
			TreeMap<String, String> questionTitle = new TreeMap<String, String>();
			TreeMap<String, String> questionDesc = new TreeMap<String, String>();
			
			while (resultSet.next()) {

				String questionId=resultSet.getString("questionID");
				title = resultSet.getString("Title");
				question = resultSet.getString("question");
				Date time = resultSet.getDate("time");
				String vote = resultSet.getString("vote");

				//readAnswerData(questionId);
				list.add(questionId);
				questionTitle.put(questionId,title);
				questionDesc.put(questionId, question);

			}
			
			for(String qString:list)
			{
				status=check(qString);
				
				if(status==1)
				{
					StringBuilder content=new StringBuilder(1024);
					content.append(questionTitle.get(qString));
					content.append(questionDesc.get(qString));

					File file=new File("E:/stackoverflow/test_data/"+qString+".txt");
					FileWriter fstream = new FileWriter(file, true); //true tells to append data.
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(content.toString());
					out.close();
				}
			}

			File file=new File("E:/stackoverflow/test_data/acceptedAnswer.txt");
			FileWriter fstream = new FileWriter(file, true); //true tells to append data.
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(acceptedAnswer.toString());
			out.close();

		} catch (Exception e) {
			throw e;
		}


	}

	public int check(String questionID) throws Exception
	{
		try {
			// This will load the MySQL driver, each DB has its own driver
			//Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			//connect = DriverManager	.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.answer_data where questionId=?";
			preparedStatement=connect.prepareStatement(sql);
			preparedStatement.setString(1, questionID);
			// Result set get the result of the SQL query

			
			resultSet = preparedStatement
					.executeQuery();

			// writeResultSet(resultSet);

			int status=0;
			
			
			while (resultSet.next()) {

				String answerId=resultSet.getString("answerID");
				String accepted = resultSet.getString("accepted");
				String userId = resultSet.getString("userId");

				System.out.println(questionID);
				System.out.println(answerId);
				System.out.println(accepted);
				System.out.println("\n");
				if(accepted.equals("1"))
				{
					acceptedAnswer.put(questionID, userId);
					status=1;
				}


			}

			return status;

		} catch (Exception e) {
			throw e;
		}
	}
	
	public void readAnswerData(String questionID) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			//Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			//connect = DriverManager	.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.answer_data where questionId=?";
			preparedStatement=connect.prepareStatement(sql);
			preparedStatement.setString(1, questionID);
			// Result set get the result of the SQL query

			resultSet = preparedStatement
					.executeQuery();

			// writeResultSet(resultSet);

			int status=0;
			System.out.println("answer");
			while (resultSet.next()) {


				String answerId=resultSet.getString("answerID");
				String accepted = resultSet.getString("accepted");
				String userId = resultSet.getString("userId");

				
				
				if(accepted.equals("1"))
				{
					acceptedAnswer.put(questionID, userId);
					status=1;
				}


			}

			

		} catch (Exception e) {
			throw e;
		}


	}
}
