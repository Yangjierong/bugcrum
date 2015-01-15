import groovy.sql.Sql;

//@GrabConfig(systemClassLoader=true)
//@Grab('mysql:mysql-connector-java:5.1.31')


import java.sql.Connection
import java.sql.DriverManager
import java.sql.DatabaseMetaData
import java.sql.ResultSet

//@Grab('org.apache.commons:commons-email:1.3.3')
import org.apache.commons.mail.*

//@Grab('javax.mail:mail:1.4.1')
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import groovy.transform.Field


def url = 'jdbc:mysql://192.168.130.161/bugzilla?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useTimezone=true&serverTimezone=CTT'
def user = 'root'
def password = 'password'
def driver = 'com.mysql.jdbc.Driver'


Sql.loadDriver(driver)
Connection connection = DriverManager.getConnection(url, user, password)

Sql sql = new Sql(connection)

//def productName = 'Asset Management'

def product = sql.firstRow('SELECT * FROM products WHERE name =?', [productName])

def reportDir = 'target'

generateHTML(sql, new Date(), reportDir, productName, product.id)

sendMail(sql, productName, reportDir, mailHost, mailProtocol, mailUser, mailPassword)

//-------------------------class & method-----------------------------------------------------------



def generateHTML(sql, generateDate, reportDir, productName, productId){
    def sw = new StringWriter()
    def html = new groovy.xml.MarkupBuilder(sw)
    html.html{
        head{ 
			title("${productName} Task Board") 
			style(new File(cssFile).text)
		}
        body{            
            h3("Generated Date: ${generateDate.format('yyyy-MM-dd HH:mm:ss')}")
            h3("${productName} Task Board")			
			
			br()
			br()			
						
			sql.eachRow('SELECT * FROM bug_status WHERE value != ? order by sortkey', ['CLOSED']){ status ->
				
				def bugs = sql.rows('SELECT * FROM bugs WHERE product_id=? and bug_status = ? order by resolution, bug_severity', [productId, status.value])
                
				if(bugs.size()>0){            
					h3("Task Board of ${status.value}:")
					
					table(border:1, cellpadding:"0", cellspacing:"0", class:"table"){
						tr{
							th(class:"head", align:'center', "ID")
							th(class:"head", align:'center', "Owner")
							th(class:"head", align:'center', "Severity")
							th(class:"head", align:'center', "Resolution")
							th(class:"head", align:'center', "Desc")
						}
						
						bugs.each{ bug ->
							def user = sql.firstRow('SELECT * FROM profiles WHERE userid = ?', [bug.assigned_to])
							println "bug_id: ${bug.bug_id}, assigned_to: ${bug.assigned_to}, bug_severity: ${bug.bug_severity}, bug_status: ${bug.bug_status}"
							println "short_desc: ${bug.short_desc}"
							tr{
								td(class:'td', align:'center', bug.bug_id)
								td(class:'td', align:'center', user.realname)
								td(class:'td', align:'center', bug.bug_severity)
								td(class:'td', align:'center', bug.resolution)
								td(class:'td', align:'left', bug.short_desc)
							}
						}
					}
				}
					
			}            
        }
    }
    
	new File(reportDir).mkdirs()
    def f = new File("${reportDir}/index.html")
    f.write(sw.toString())
}

def sendMail(sql, productName, reportDir, mailHost, mailProtocol, mailUser, mailPassword){

	println("Sending mail...");
	println('NOTE: need to copy mail.jar to $GROOVY_HOME/lib/');

	Properties props = new Properties();
	props.setProperty("mail.transport.protocol", mailProtocol);
	props.setProperty("mail.host", mailHost);
	props.setProperty("mail.user", mailUser);
	props.setProperty("mail.password", mailPassword);
    
	props.setProperty("mail.mime.charset", "UTF-8");

	Session mailSession = Session.getDefaultInstance(props, null);
	mailSession.setDebug(true);
	Transport transport = mailSession.getTransport();

	MimeMessage message = new MimeMessage(mailSession);
	message.setSubject("${productName} Task Board");
	message.setFrom(new InternetAddress(mailUser));
	message.setSentDate(new Date());
    
    sendTos.split(',').each{email ->
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email))
    }
    	
		
	//
	// This HTML mail have to 2 part, the BODY and the embedded image
	//
	MimeMultipart multipart = new MimeMultipart("related");

	// first part (the html)
	BodyPart messageBodyPart = new MimeBodyPart();
	String htmlText = new File("${reportDir}/index.html").getText("UTF-8");
	//htmlText = htmlText.replace("burnDownChart.png", "cid:image");
	messageBodyPart.setContent(htmlText, "text/html; charset=UTF-8");

	// add it
	multipart.addBodyPart(messageBodyPart);

	// second part (the image)
	//messageBodyPart = new MimeBodyPart();
	//DataSource fds = new FileDataSource("${reportDir}/burnDownChart.png");
	//messageBodyPart.setDataHandler(new DataHandler(fds));
	//messageBodyPart.setHeader("Content-ID", "<image>");

	// add it
	//multipart.addBodyPart(messageBodyPart);

	// put everything together
	message.setContent(multipart);

	transport.connect();
	transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
	transport.close();
}

