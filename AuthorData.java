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
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.crypto.AEADBadTagException;

public class AuthorData {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;



	public void readDataBase() throws Exception {
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

			int count=0;
			System.out.println(resultSet.getFetchSize());
			while (resultSet.next()) {
				StringBuilder content=new StringBuilder(1024);
				String userID = resultSet.getString("UserId");
				String questionIDs = resultSet.getString("questionIds");
				String answerIDs = resultSet.getString("answerIds");

				/*String questionData[]=questionIDs.split(",");

				for(int i=0;i<questionData.length;i++)
				{
					readQuestionData(questionData[i],content);
				}*/

				File file=new File("E:/stackoverflow/output/"+userID+".txt");
				FileWriter fstream = new FileWriter(file, true); //true tells to append data.
				BufferedWriter out = new BufferedWriter(fstream);

				String answerData[]=answerIDs.split(",");

				/*if(answerData.length>=5)
				{*/
				for(int i=0;i<answerData.length;i++)
				{
					//readAnswerData(answerData[i],content);
					out.write(content.toString());
					out.write(System.getProperty("line.separator"));

				}
				//}

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

				if(content.toString().isEmpty())
				{
					fileDelete(file);
				}
				count++;
			}
			System.out.println(count);

		} catch (Exception e) {
			throw e;
		}

	}

	public void fileDelete(File file)
	{
		//file.delete();
		//File fil=new File("E:/stackoverflow/output/64964.txt");
		file.delete();
	}

	public int readQuestionData(String questionID,StringBuilder content,TreeMap<String, String> voteData) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/stackoverflow","root","mysql");

			String sql="select * from stackoverflow.question_data where questionID=?";
			// Statements allow to issue SQL queries to the database
			preparedStatement=connect.prepareStatement(sql);
			preparedStatement.setString(1, questionID);
			// Result set get the result of the SQL query

			resultSet = preparedStatement
					.executeQuery();

			// writeResultSet(resultSet);

			int status=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			java.sql.Date min=new java.sql.Date(format.parse ("2012-01-01").getTime());
			java.sql.Date max=new java.sql.Date(format.parse ("2012-11-30").getTime());

			while (resultSet.next()) {

				String title = resultSet.getString("Title");
				String description = resultSet.getString("question");
				Date time = resultSet.getDate("time");
				String vote = resultSet.getString("vote");

				if(time.after(min) && time.before(max))
				{
					content.append(title);
					content.append(description);
					status=1;

					voteData.put("questionID", questionID);
					voteData.put("questionVote",vote);
				}

			}

			return status;

		} catch (Exception e) {
			throw e;
		}


	}

	public void readAnswerData(String answerId,StringBuilder content,TreeMap<String, String> voteData,int count ) throws Exception {
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
					
					count++;
					if(count>=5)
					{
						status=readQuestionData(question,content,voteData);
					}
				}

				if(status==1)
				{
					content.append(answer);

					voteData.put("answerId", answerId);
					voteData.put("answerVote",vote);
					voteData.put("accepted",accepted);
				}

			}

			connect.close();
		} catch (Exception e) {
			throw e;
		}


	}
	
	public int checkAnswerData(String answerId,int count) throws Exception {
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
				}

			}
			

			connect.close();
			return status;
		} catch (Exception e) {
			throw e;
		}


	}
}
