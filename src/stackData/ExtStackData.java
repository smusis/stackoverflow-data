package stackData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;



public class ExtStackData {

	static int ansfirst;
	static int anslast;
	static int userF;
	static int userL;
	static int voteUp;
	static int voteDown;
	static int answerId;




	public static void main(String args[]) throws Exception 
	{
		final File folder = new File("E:/stackoverflow/tag_1");
		final List<File> fileList = Arrays.asList(folder.listFiles());


		for(int i=0;i<fileList.size();i++)
		{
			final BufferedReader br = new BufferedReader(new FileReader(fileList.get(i))); 
			System.out.println(fileList.get(i));
			questions(br);
		}
	}

	public static void questions(BufferedReader br) throws Exception
	{
		/*StackDB stackDB=new StackDB();
		stackDB.readDataBase();
		System.exit(1);*/
		TreeMap<String, String> questionData = new TreeMap<String, String>();

		int continueLoop=0;
		String line; 
		String st=new String();

		StringBuilder content=new StringBuilder(1024);
		while((line=br.readLine())!=null)
		{
			content.append(line);
		} 

		if(content.toString().contains("<title>"))
		{
			String title[]=content.subSequence(content.indexOf("<title>"),
					content.indexOf("</title>",content.indexOf("<title>"))).toString().split("-");
			System.out.println(title[1]);
			questionData.put("Title", title[1]);
		}

		if((content.toString().contains("post-signature owner")) && (!(content.toString().contains("community-wiki"))) &&
				(content.subSequence(content.indexOf("<td class=\"post-signature owner\""),content.indexOf("</div>", content.indexOf("<td class=\"post-signature owner\""))).
				toString().contains("asked")))
		{

			
			if(content.toString().contains("data-questionid"))
			{
				int first = content.indexOf("data-questionid");
				int last = content.indexOf(">",first);


				String questionId=content.subSequence(first,last).toString();

				String[] split =questionId.subSequence(questionId.indexOf("\""),questionId.lastIndexOf("\"")).
						toString().split("\"");
				questionId=split[1]; 
				System.out.println("Question ID "+questionId+"\n");
				questionData.put("QuestionID", questionId);

			}

			if(content.toString().contains("itemprop=\"description\""))
			{
				int first = content.indexOf("itemprop=\"description\"");
				int last = content.indexOf("div class=\"post-taglist\">");

				int userFirst = content.indexOf("post-signature owner",last);
				int userDetails = content.indexOf("<div class=\"user-details\">",userFirst);

				String userData =content.subSequence(userDetails,content.indexOf("<br />",userDetails)).toString();
				String splitData[]=userData.split("/");

				if(splitData.length>1)
				{

					String question=content.subSequence(first,last).toString();
					//System.out.println(question);
					String split ="";
					if(question.contains("<p>"))
					{
						split =question.subSequence(question.indexOf("<p>"),question.indexOf("</div>")).toString();
					}
					else if(question.contains("<code>"))
					{
						split =question.subSequence(question.indexOf("<code>"),question.indexOf("</div>")).toString();
					}
					/*toString().split("\"");
			questionId=split[1];*/

					String noHTMLString = question.replaceAll("\\<.*?>","");
					noHTMLString = noHTMLString.replaceAll("itemprop=\"description\">","");
					noHTMLString = noHTMLString.replaceAll(";","");
					noHTMLString = noHTMLString.replaceAll("&lt","<");
					noHTMLString = noHTMLString.replaceAll("&gt",">");
					noHTMLString = noHTMLString.replaceAll("&quot","\"");
					System.out.println("Description "+noHTMLString+"\n");
					questionData.put("Description", noHTMLString);
					//final StringTokenizer ps = new StringTokenizer(line, " <p></p>"); 

					voteUp = content.indexOf("vote-up-off");
					voteDown = content.indexOf("vote-down-off");

					String voteData=content.subSequence(voteUp,voteDown).toString();
					if(voteData.contains("vote-count-post"))
					{
						String vote[]=voteData.split("vote-count-post ");
						String upVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("UpVote "+upVote);
						questionData.put("UpVote", upVote);
					}
					else
					{
						content.subSequence(voteDown,content.indexOf("</span>")).toString();
						String vote[]=voteData.split("vote-count-post ");
						String downVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("UpVote "+downVote);
						questionData.put("DownVote", downVote);

					}




					String userTime=content.subSequence(userFirst,userDetails).toString();
					
					String dataTime[] =userTime.subSequence(userTime.indexOf("asked"),userTime.indexOf("class",userTime.indexOf("asked"))).toString().
						split("\"");

					Date result; 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					result = formatter.parse (dataTime[1].substring(0,dataTime[1].length()-1 )); 
					java.sql.Date sqlDate = new java.sql.Date(result.getTime()); 
					java.sql.Time sqlTime = new java.sql.Time(result.getTime()); 
					System.out.println("SQL date " + sqlDate + " " + sqlTime);
					questionData.put("Time", sqlDate.toString());



					System.out.println("UserID "+splitData[2]);
					questionData.put("UserID", splitData[2]);

					String userName=splitData[3].subSequence(splitData[3].indexOf(">"), splitData[3].indexOf("<")).
							toString().replaceAll(">","");
					System.out.println("UserName "+userName);
					questionData.put("UserName", userName);

					if(content.toString().contains("_gaq.push(['_setCustomVar"))
					{
						String tags=content.subSequence(content.indexOf("_gaq.push(['_setCustomVar"),content.indexOf("_gaq.push(['_trackPageview",content.indexOf("_gaq.push(['_setCustomVar"))).
								toString();
						String tagsp[]=(tags.subSequence(tags.indexOf("', '"), tags.indexOf("'])")).toString()).split("', '");

						System.out.println("Tags "+tagsp[1].replace('|', ',').substring(1,tagsp[1].length()-1));
						questionData.put("Tags", tagsp[1].replace('|', ',').substring(1,tagsp[1].length()-1));

					}
					
				}
				else
				{
					continueLoop=1;
				}
			}


			if(!(continueLoop==1))
			{
				if(content.toString().contains("\"subheader answers-subheader\""))
				{
					int first = content.indexOf("\"subheader answers-subheader\"");
					int last = content.indexOf("<div id=\"tabs\"",first);


					String question=content.subSequence(first,last).toString();
					//System.out.println(question);
					String split =question.subSequence(question.indexOf("<h2>"),question.indexOf("</h2>",question.indexOf("<h2>"))).toString();
					/*toString().split("\"");
			questionId=split[1];*/
					split=split.replaceAll("<h2>","");
					System.out.println("spl"+split.trim().equals(""));


					if((!(split.trim().equals(""))) && (!(split.isEmpty())))
					{
						String noHTMLString = split.replaceAll("\\<.*?>","");
						noHTMLString = noHTMLString.replaceAll(";","");
						noHTMLString = noHTMLString.replaceAll("&lt","<");
						noHTMLString = noHTMLString.replaceAll("&gt",">");
						noHTMLString = noHTMLString.replaceAll("&quot","\"");
						noHTMLString = noHTMLString.replaceAll("[^\\d.]", "");
						System.out.println("\nNo of answers"+noHTMLString+"\n");

						int noOfAnswers = Integer.parseInt(noHTMLString);
						//System.out.println(noOfAnswers);
						//final StringTokenizer ps = new StringTokenizer(line, " <p></p>"); 

						if(!(noOfAnswers==0))
						{
							answers(content, noOfAnswers,questionData);
						}
						else
						{
							StackDB stackDB=new StackDB();
							stackDB.writeUserData(questionData);
							stackDB.writeQuestionData(questionData);
						}
					}
				}
			}

		}
	}

	public static void answers(StringBuilder content, int noOfAnswers,TreeMap<String, String> questionData) throws Exception
	{



		if(noOfAnswers==1)
		{
			TreeMap<String, String> answerData = new TreeMap<String, String>();

			if(content.toString().contains("<td class=\"answercell\""))
			{
				int accepted=0;
				int first = content.indexOf("<td class=\"answercell\"");
				int last = content.indexOf("<table class=\"fw\"> ", first);

				int userFirst = content.indexOf("answered",last);
				int userDetails = content.indexOf("<div class=\"user-details\">",userFirst);

				String userData =content.subSequence(userDetails,content.indexOf("<br />",userDetails)).toString();
				String splitData[]=userData.split("/");

				if(splitData.length>1)
				{
					//System.out.println("\n"+first);
					//System.out.println("\n"+last);
					String [] aId=content.subSequence(content.indexOf("data-answerid"),content.indexOf(">",content.indexOf("data-answerid"))).toString().
							split("\"");
					System.out.println("AnswerId "+aId[1].toString().replaceAll("\"", ""));
					answerData.put("AnswerId", aId[1].toString().replaceAll("\"", ""));

					String question=content.subSequence(first,last).toString();
					//System.out.println(question);
					String split="" ;
					if(question.contains("<p>"))
					{
						split =question.subSequence(question.indexOf("<p>"),question.indexOf("</div>")).toString();
					}
					else if(question.contains("<code>"))
					{
						split =question.subSequence(question.indexOf("<code>"),question.indexOf("</div>")).toString();
					}
					/*toString().split("\"");
        			questionId=split[1];*/

					String noHTMLString = question.replaceAll("\\<.*?>","");
					noHTMLString = noHTMLString.replaceAll(";","");
					noHTMLString = noHTMLString.replaceAll("&lt","<");
					noHTMLString = noHTMLString.replaceAll("&gt",">");
					noHTMLString = noHTMLString.replaceAll("&quot","\"");
					System.out.println("Answer "+ noHTMLString+"\n");
					answerData.put("Answer", noHTMLString);

					voteUp = content.indexOf("vote-up-off",voteDown);
					voteDown = content.indexOf("vote-down-off",voteUp);
					String voteData=content.subSequence(voteUp,voteDown).toString();
					if(voteData.contains("vote-count-post"))
					{
						String vote[]=voteData.split("vote-count-post ");
						String upVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("UpVote "+upVote);
						answerData.put("UpVote", upVote);
					}
					else
					{
						content.subSequence(voteDown,content.indexOf("</span>")).toString();
						String vote[]=voteData.split("vote-count-post ");
						String downVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("DownVote "+downVote);
						answerData.put("DownVote", downVote);
					}

					if((content.subSequence(voteDown, content.indexOf("<td class=\"answercell\">",voteDown))).toString().contains("accepted"))
					{
						accepted=1;
					}
					System.out.println("Accepted "+accepted);
					answerData.put("Accepted", Integer.toString(accepted));



					String dataTime[] =content.subSequence(userFirst,content.indexOf("class",userFirst)).toString().split("\"");
					System.out.println("Time "+dataTime[1]);
					Date result; 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					result = formatter.parse (dataTime[1].substring(0,dataTime[1].length()-1 )); 
					java.sql.Date sqlDate = new java.sql.Date(result.getTime()); 
					java.sql.Time sqlTime = new java.sql.Time(result.getTime()); 
					System.out.println("SQL date " + sqlDate + " " + sqlTime);
					answerData.put("Time", sqlDate.toString());



					System.out.println("UserID "+splitData[2]);
					answerData.put("UserID", splitData[2]);
					String userName=splitData[3].subSequence(splitData[3].indexOf(">"), splitData[3].indexOf("<")).
							toString().replaceAll(">","");
					System.out.println("UserName "+userName);
					answerData.put("UserName", userName);
					
					answerData.put("QuestionID", questionData.get("QuestionID"));
				}
				
			}
			StackDB stackDB=new StackDB();
			stackDB.writeUserData(questionData);
			stackDB.writeQuestionData(questionData);
			stackDB.writeUserAnswerData(answerData);
			stackDB.writeAnswerData(answerData);
		}

		else
		{

			if(content.toString().contains("<td class=\"answercell\""))
			{
				TreeMap<String, String> answerData = new TreeMap<String, String>();
				int accepted=0;

				int first = content.indexOf("<td class=\"answercell\"");
				int last = content.indexOf("<table class=\"fw\"> ", first);

				int userFirst = content.indexOf("answered",last);
				int userDetails = content.indexOf("<div class=\"user-details\">",userFirst);

				String userData =content.subSequence(userDetails,content.indexOf("<br />",userDetails)).toString();
				String splitData[]=userData.split("/");

				if(splitData.length>1)
				{
					answerId=content.indexOf("data-answerid");
					answerId=content.indexOf(">",answerId);
					String [] aId=content.subSequence(content.indexOf("data-answerid"),content.indexOf(">",content.indexOf("data-answerid"))).toString().
							split("\"");
					System.out.println("Answer ID "+aId[1].toString().replaceAll("\"", "").toString());
					answerData.put("AnswerId", aId[1].toString().replaceAll("\"", "").toString());

					//System.out.println("\n"+first);
					//System.out.println("\n"+last);

					String question=content.subSequence(first,last).toString();
					//System.out.println(question);
					String split ="";

					if(question.contains("<p>"))
					{
						split =question.subSequence(question.indexOf("<p>"),question.indexOf("</div>")).toString();
					}
					else if(question.contains("<code>"))
					{
						split =question.subSequence(question.indexOf("<code>"),question.indexOf("</div>")).toString();
					}
					/*toString().split("\"");
			questionId=split[1];*/

					String noHTMLString = question.replaceAll("\\<.*?>","");
					noHTMLString = noHTMLString.replaceAll(";","");
					noHTMLString = noHTMLString.replaceAll("&lt","<");
					noHTMLString = noHTMLString.replaceAll("&gt",">");
					noHTMLString = noHTMLString.replaceAll("&quot","\"");
					System.out.println("Answer "+noHTMLString+"\n");
					answerData.put("Answer", noHTMLString);

					voteUp = content.indexOf("vote-up-off",voteDown);
					voteDown = content.indexOf("vote-down-off",voteUp);
					String voteData=content.subSequence(voteUp,voteDown).toString();
					if(voteData.contains("vote-count-post"))
					{
						String vote[]=voteData.split("vote-count-post ");
						String upVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("UpVote "+upVote);
						answerData.put("UpVote", upVote);
					}
					else
					{
						content.subSequence(voteDown,content.indexOf("</span>")).toString();
						String vote[]=voteData.split("vote-count-post ");
						String downVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
						System.out.println("DownVote "+downVote);
						answerData.put("UpVote", downVote);
					}


					if((content.subSequence(voteDown, content.indexOf("<td class=\"answercell\">",voteDown))).toString().contains("accepted"))
					{
						accepted=1;
					}
					System.out.println("Accepted "+accepted);
					answerData.put("Accepted", Integer.toString(accepted));



					String dataTime[] =content.subSequence(userFirst,content.indexOf("class",userFirst)).toString().split("\"");
					System.out.println("Time "+dataTime[1]);
					Date result; 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					result = formatter.parse (dataTime[1].substring(0,dataTime[1].length()-1 )); 
					java.sql.Date sqlDate = new java.sql.Date(result.getTime()); 
					java.sql.Time sqlTime = new java.sql.Time(result.getTime()); 
					System.out.println("SQL date " + sqlDate + " " + sqlTime);
					answerData.put("Time", sqlDate.toString());



					System.out.println("UserID "+splitData[2]);
					answerData.put("UserID", splitData[2]);
					String userName=splitData[3].subSequence(splitData[3].indexOf(">"), splitData[3].indexOf("<")).
							toString().replaceAll(">","");
					System.out.println("UserName "+userName);
					answerData.put("UserName", userName);
					System.out.println("\n");
					//final StringTokenizer ps = new StringTokenizer(line, " <p></p>"); 

					answerData.put("QuestionID", questionData.get("QuestionID"));
					
					StackDB stackDB=new StackDB();
					stackDB.writeUserData(questionData);
					stackDB.writeQuestionData(questionData);
					stackDB.writeUserAnswerData(answerData);
					stackDB.writeAnswerData(answerData);

					anslast=last;

					for(int i=2;i<=noOfAnswers;i++)
					{
						TreeMap<String, String> answerData1 = new TreeMap<String, String>();
						accepted=0;

						ansfirst = content.indexOf("<td class=\"answercell\"",anslast);
						anslast = content.indexOf("<table class=\"fw\"> ", ansfirst);

						userF = content.indexOf("answered",anslast);
						userL= content.indexOf("<div class=\"user-details\">",userF);

						userData =content.subSequence(userL,content.indexOf("<br />",userL)).toString();
						splitData=userData.split("/");

						if(splitData.length>1)
						{

							answerId=content.indexOf("data-answerid",answerId);

							aId=content.subSequence(answerId,content.indexOf(">",answerId)).
									toString().split("\"");
							System.out.println("AnswerId "+aId[1].toString().replaceAll("\"", ""));
							answerData1.put("AnswerId", aId[1].toString().replaceAll("\"", ""));

							answerId=content.indexOf(">",answerId);

							question=content.subSequence(ansfirst,anslast).toString();

							if(question.contains("<p>"))
							{
								split =question.subSequence(question.indexOf("<p>"),question.indexOf("</div>")).toString();
							}
							else if(question.contains("<code>"))
							{
								split =question.subSequence(question.indexOf("<code>"),question.indexOf("</div>")).toString();
							}

							noHTMLString = question.replaceAll("\\<.*?>","");
							noHTMLString = noHTMLString.replaceAll(";","");
							noHTMLString = noHTMLString.replaceAll("&lt","<");
							noHTMLString = noHTMLString.replaceAll("&gt",">");
							noHTMLString = noHTMLString.replaceAll("&quot","\"");
							System.out.println("Answer"+noHTMLString+"\n");
							answerData1.put("Answer", noHTMLString);

							voteUp = content.indexOf("vote-up-off",voteDown);
							voteDown = content.indexOf("vote-down-off",voteUp);
							voteData=content.subSequence(voteUp,voteDown).toString();
							if(voteData.contains("vote-count-post"))
							{
								String vote[]=voteData.split("vote-count-post ");
								String upVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
								System.out.println("UpVote "+upVote);
								answerData1.put("UpVote", upVote);
							}
							else
							{
								content.subSequence(voteDown,content.indexOf("</span>")).toString();
								String vote[]=voteData.split("vote-count-post ");
								String downVote=vote[1].subSequence(vote[1].indexOf(">"), vote[1].indexOf("</span>")).toString().replaceAll(">","");
								System.out.println("DownVote " +downVote);
								answerData1.put("UpVote", downVote);
							}

							if((content.subSequence(voteDown, content.indexOf("<td class=\"answercell\">",voteDown))).toString().contains("accepted"))
							{
								accepted=1;
							}
							System.out.println("Accepted "+accepted);
							answerData1.put("Accepted", Integer.toString(accepted));


							dataTime =content.subSequence(userF,content.indexOf("class",userF)).toString().split("\"");
							formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
							result = formatter.parse (dataTime[1].substring(0,dataTime[1].length()-1 )); 
							sqlDate = new java.sql.Date(result.getTime()); 
							sqlTime = new java.sql.Time(result.getTime()); 
							System.out.println("SQL date " + sqlDate + " " + sqlTime);
							answerData1.put("Time", sqlDate.toString());



							System.out.println("UserID "+splitData[2]);
							answerData1.put("UserID", splitData[2]);

							userName=splitData[3].subSequence(splitData[3].indexOf(">"), splitData[3].indexOf("<")).
									toString().replaceAll(">","");
							System.out.println("UserName "+userName);
							answerData1.put("UserName", userName);
							System.out.println("\n");
							
							answerData1.put("QuestionID", questionData.get("QuestionID"));
							stackDB.writeUserAnswerData(answerData1);
							stackDB.writeAnswerData(answerData1);
						}
					}
				}
			}
		}
	}

	/*public void fetchData()
	{
		int firstWhiteSpace = String.indexOf("data-questionid");
	}*/
}
