package stackData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetSonarData {
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	public static void main(String[] args){  

		try{  
			String line;
			//BufferedReader br = new BufferedReader(new FileReader("E:/Research Projects/FSE project/Data.xml"));
			//FileInputStream fis = new FileInputStream("E:/Research Projects/FSE project/acra.xml");
			//while ((line= br.readLine()) != null) {  
			//	String urlStr = "http://nemo.sonarsource.org/api/resources?resource="+line+"&depth=-1&scopes=PRJ&metrics=ncloc,coverage,line_coverage,branch_coverage";

			/*	String urlPrj = "http://localhost:9000/api/resources?metrics=ncloc,coverage,line_coverage,branch_coverage";

				URL url = new URL(urlPrj);  
				HttpURLConnection connection= (HttpURLConnection)url.openConnection();  
				//connection.connect();  
				connection.setAllowUserInteraction(true);  
				connection.setRequestMethod("GET");  
				connection.setDoOutput(true);  
				connection.setUseCaches(false);  
				//connection.setRequestProperty("Content-Length","application/x-www-form-urlencoded");  

				System.out.println("*************CONNECTED*************"); */ 
			Pattern p = Pattern.compile("<resource>", Pattern.CASE_INSENSITIVE);
			Pattern p2 = Pattern.compile("<resource>(.*)</resource>",Pattern.DOTALL);

			
			
			/*StringBuilder content=new StringBuilder(1024);
			while((line=br.readLine())!=null)
			{
				content.append(line);
			} */

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("E:/Research Projects/APSEC 2014 project/Data.xml");
			Element root = doc.getDocumentElement();
			
			NodeList nodeList = doc.getElementsByTagName("resource");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;

					String id= eElement.getElementsByTagName("id").item(0).getTextContent();
					String key= eElement.getElementsByTagName("key").item(0).getTextContent();
					String name= eElement.getElementsByTagName("name").item(0).getTextContent();

					//System.out.println(name);
					TreeMap<String,String> map=new TreeMap<String,String>();
					String[] array={"lines","ncloc","classes","statements","complexity","class_complexity","function_complexity","file_complexity",
							"tests","test_errors","skipped_tests","test_failures","test_success_density","coverage","line_coverage","branch_coverage"
							//"sqale_index,","sqale_rating,","sqale_effort_to_grade_a,","sqale_effort_to_grade_b,","sqale_rating_file_distribution"
							};
					
					for(String str:array)
					{
						map.put(str, "");
					}
					System.out.println("\n");
					NodeList msr = eElement.getElementsByTagName("msr");
					for (int j = 0; j < msr.getLength(); j++) {
						
						Node node1 = msr.item(j);
						if (node1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement1 = (Element) node1;

							if(eElement1.getElementsByTagName("key").item(0).getTextContent().equalsIgnoreCase("sqale_rating_file_distribution"))
							{
								map.put(eElement1.getElementsByTagName("key").item(0).getTextContent(), eElement1.getElementsByTagName("data").item(0).getTextContent());
							}
							
							else
							{
								System.out.println(eElement1.getElementsByTagName("key").item(0).getTextContent()+"  "+ eElement1.getElementsByTagName("val").item(0).getTextContent());
						    	map.put(eElement1.getElementsByTagName("key").item(0).getTextContent(), eElement1.getElementsByTagName("val").item(0).getTextContent());
							}
						}
					}
					//System.out.println(id);
					
					inputData(id, key, name, map);
				}
				// do your stuff
			}
		}
			catch(Exception e){  
				e.printStackTrace();  
			} 
	}
		
		public static void inputData(String id,String key, String name,TreeMap<String,String> map ) throws Exception{
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost/new_sonar_data","root","mysql");

				System.out.println("In");
				String sql="Insert into new_sonar_data.sonar_github_data values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				//String sql="Insert into sonar_data.sonar_github_data values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				// Statements allow to issue SQL queries to the database
				preparedStatement=connect.prepareStatement(sql);
				preparedStatement.setString(1, id);
				preparedStatement.setString(2, key.trim());
				preparedStatement.setString(3, name.trim());
				preparedStatement.setString(4, map.get("lines"));
				preparedStatement.setString(5, map.get("ncloc"));
				preparedStatement.setString(6, map.get("classes"));
				preparedStatement.setString(7, map.get("statements"));
				preparedStatement.setString(8, map.get("complexity"));
				preparedStatement.setString(9, map.get("class_complexity"));
				preparedStatement.setString(10, map.get("function_complexity"));
				preparedStatement.setString(11, map.get("file_complexity"));
				preparedStatement.setString(12, map.get("tests"));
				preparedStatement.setString(13, map.get("test_errors"));
				preparedStatement.setString(14, map.get("skipped_tests"));
				preparedStatement.setString(15, map.get("test_failures"));
				preparedStatement.setString(16, map.get("test_success_density"));
				preparedStatement.setString(17, map.get("coverage"));
				preparedStatement.setString(18, map.get("line_coverage"));
				preparedStatement.setString(19, map.get("branch_coverage"));
				/*preparedStatement.setString(20, map.get("sqale_index"));
				preparedStatement.setString(21, map.get("sqale_rating"));
				preparedStatement.setString(22, map.get("sqale_effort_to_grade_a"));
				preparedStatement.setString(23, map.get("sqale_effort_to_grade_b"));
				preparedStatement.setString(24, map.get("sqale_rating_file_distribution"));*/
				// Result set get the result of the SQL query
				preparedStatement
						.executeUpdate();

				// writeResultSet(resultSet);
				connect.close();
			
	            

			} catch (Exception e) {
				throw e;
			}

			//}
			//PrintWriter writer = new PrintWriter(new FileWriter(new File("E:/Research Projects/FSE project/output/org.apache.tika:tika"),true));
			//PrintStream out = new PrintStream(new FileOutputStream("E:/Research Projects/FSE project/output/outof"));




			//}
			//connection.disconnect();  
		}
	}

