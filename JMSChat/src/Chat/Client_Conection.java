package Chat;


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
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Erstellt Connection für den Client und ist ein Tread um automailbox ein bzw aus zu schalten
 * @author Backhausen, Rieppel
 * @version 0.5
 */
public class Client_Conection extends Thread{
	private String usr, pwd, url, raum;
	private Session s = null;
	private Connection c = null;
	private MessageProducer pro = null;
	private MessageConsumer con = null;
	private Destination d = null;
	private Client_Controller cc;
	private Queue q;
	private boolean bool = true;
	/**
	 * Konstruktor
	 * @param serverName Server addresse
	 * @param serverPort Server Port
	 * @param cc Controller um mit ihm comonizieren zu können
	 */
	public Client_Conection(String serverName, int serverPort,String usr, String pwd, String raum, Client_Controller cc){
		this.usr = usr;
		this.pwd = pwd;
		this.raum = raum;
		this.url = "tcp://"+serverName + ":" + serverPort;
		this.cc = cc;
		this.startCon();
		
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
			//q = s.createQueue(raum);
			d = s.createTopic(raum);
			pro = s.createProducer(d);
			//pro = s.createProducer(q);
			pro.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			con = s.createConsumer(d);
			//con = s.createConsumer(q);
			
			this.start();
		} catch (JMSException e) {
			System.err.println("ERROR: " + e.getMessage());
		} 
	}
	
	/**
	 * Trennt die Verbindung
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
			Date d = new Date();
			String ti = sdf.format(d);
			TextMessage m = s.createTextMessage(ti + " " + this.usr + ": " + msg );
			
			pro.send(m);
		} catch (JMSException e) {
			System.err.println("sendM_ERROR: \n" + e.getMessage());
		}
	}
	/**
	 * Ruft die Nächste Nachricht ab
	 */
	public void getMassage(){
		//boolean b = true;
		//while(b){
			try {
				TextMessage message = (TextMessage) con.receive();
				if ( message != null ) {
					cc.handle(message.getText());
					message.acknowledge();
				}
			} catch (JMSException e) {
				System.err.println("ERROR: " + e.getMessage());
			}
		//}
	}
	/**
	 * Setzt bool für automailbrox zu aktivieren/deaktivieren
	 * @param r neuer wert
	 */
	public void setbool(boolean r){
		this.bool = r;
	}
	/**
	 * GIbt bool zurück um automailbox zu überprüfen
	 * @return der aktuelle automailbox zustand
	 */
	public boolean getbool(){
		return this.bool;
	}
	@Override
	public void run(){
		while(!this.isInterrupted()){
			while(bool){
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
}