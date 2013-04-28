package Chat;

import java.net.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.sun.jmx.snmp.Timestamp;
/**
 * Erstellt Connection f�r den Client
 * @author Dominik, Dimitrijevic
 *
 */
public class Client_Conection extends Thread{
	private String usr, pwd, url, raum;
	private Session s = null;
	private Connection c = null;
	private MessageProducer pro = null;
	private MessageConsumer con = null;
	private Destination d = null;
	private Client_Controller cc;
	/**
	 * Konstruktor
	 * @param serverName Server addresse
	 * @param serverPort Server Port
	 * @param cc Controller um mit ihm comonizieren zu k�nnen
	 */
	public Client_Conection(String serverName, int serverPort,String usr, String pwd, String raum, Client_Controller cc){
		this.usr = usr;
		this.pwd = pwd;
		this.raum = raum;
		this.url = "tcp://"+serverName + ":" + serverPort;
		this.cc = cc;
    }
	/**
	 * Startet die Verbindung
	 * @throws IOException
	 */
	public void startCon(){
		ConnectionFactory conF = new ActiveMQConnectionFactory(usr,pwd,url);
		try {
			c = conF.createConnection();
			c.start();
			
			s = c.createSession(false, Session.AUTO_ACKNOWLEDGE);
			d = s.createTopic(raum);
			
			pro = s.createProducer(d);
			pro.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			con = s.createConsumer(d);
			
		} catch (JMSException e) {
			System.err.println("ERROR: " + e.getMessage());
		} finally{
			try {c.close();} catch ( Exception e ) {}
			try {s.close();} catch ( Exception e ) {}
			try {con.close();} catch ( Exception e ) {}
			try {pro.close();} catch ( Exception e ) {}
		}
	}
	
	/**
	 * TRennt die Verbindung
	 */
	public void stopCom(){
		try {c.close();} catch ( Exception e ) {}
		try {s.close();} catch ( Exception e ) {}
		try {con.close();} catch ( Exception e ) {}
		try {pro.close();} catch ( Exception e ) {}
	}
	/**
	 * Sendet Nachrichten
	 * @param msg zu sendende Nachricht
	 */
	public void sendMessage(String msg){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			Timestamp time = new Timestamp(System.currentTimeMillis());
			String ti = sdf.format(time);
			TextMessage m = s.createTextMessage(ti + " " + this.usr + ": " + msg );
			pro.send(m);
		} catch (JMSException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	/**
	 * Thread zum empfangen von Nachrichte
	 */
	@Override
	public void run() {
		while(!this.interrupted()){
			try {
				TextMessage message = (TextMessage) con.receive();
				if ( message != null ) {
					cc.handle(message.getText());
					message.acknowledge();
				}
			} catch (JMSException e) {
				System.err.println("ERROR: " + e.getMessage());
			}
		}
	}
}