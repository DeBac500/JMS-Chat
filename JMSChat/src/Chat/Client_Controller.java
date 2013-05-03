package Chat;



/**
 * Client Controller ermögtlich komunikation zwischen GUI und Conection
 * @author Backhausen, Rieppel
 * @version 0.5
 */
public class Client_Controller{
	private Client_GUI cg;
	private Client_Conection cc;
	private String addr;
	private int port;
	/**
	 * Konstruktor mit Server
	 */
	public Client_Controller(){
		cg = new Client_GUI(this);
		MyFrame mf = new MyFrame(cg,null,"Chat",100,100,500,500,true);
	}
	/**
	 * Handle entscheidet was gmit eingehenden Nachrichten gemacht werden soll
	 * @param msg die eingehende Nachricht
	 */
	public void handle(String msg){
		cg.setChat(msg);
	}
	/**
	 * Gibt den Hilfetext aus
	 */
	public void helpman(){
		cg.setChat("/help , /? , ?	Zeigt Alle verFügbaren Befehle");
		cg.setChat("/vsdbchat <ip_message_broker> <benutzername> <chatroom>");
		//cg.setChat("/MAIL <ip_des_benutzers> <nachricht>     Sendet Mails");
		cg.setChat("/MAILBOX    Ruft Mails ab");
	}
	/**
	 * Überprüft die eingaben des Users entscheidet aufgrund von befehlen bzw sendet nachrichten
	 * @param msg zu bearbeitende Usereingabe
	 */
	public void sendMessage(String msg){
		String[] arr = msg.split(" ");
		if(arr.length > 0){
			if(arr[0].equals("/help") || arr[0].equals("/?") || arr[0].equals("?"))
				this.helpman();
			if(arr[0].equals("/vsdbchat")){
				if(arr.length>=4){
					cc = new Client_Conection(arr[1], 61616, arr[2], "" ,arr[3], this);
					cg.setChat("Connected!");
					cc.sendMessage("ist nun Verbunden!");
				}
			}
			if(cc != null){
				if(arr[0].equals("/mailbox")){
					cc.getMassage();
				}
				if(arr[0].charAt(0) != '/')
					cc.sendMessage(msg);
			}
		}
	}
	/**
	 * Schaltet automailbox ein
	 */
	public void automailboxon(){
		if(cc != null)
			cc.setbool(true);
	}
	/**
	 * Schaltet automailbox aus
	 */
	public void automailboxoff(){
		if(cc != null)
			cc.setbool(false);
	}
	/**
	 * Startet das Pogramm
	 */
	public static void main(String[] args){
		new Client_Controller();
	}
}