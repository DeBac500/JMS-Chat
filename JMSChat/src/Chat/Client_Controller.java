package Chat;



/**
 * Client Controller ermögtlich komunikation zwischen GUI und Conection
 * @author Dominik, Daniel
 *
 */
public class Client_Controller{
	private Client_GUI cg;
	private Client_Conection cc;
	private String addr;
	private int port;
	/**
	 * KOnstruktor mit Server addresse und port
	 * @param addr
	 * @param port
	 */
	public Client_Controller(){
		cg = new Client_GUI(this);
		MyFrame mf = new MyFrame(cg,null,"Chat",100,100,500,500,true);
	}
	/**
	 * Handle entscheidet was gmit eingehenden Nachrichten gemacht werden soll
	 * @param id
	 * @param msg
	 */
	public void handle(String msg){
		
	}
	public void helpman(){
		cg.setChat("/help , /? , ?	Zeigt Alle verFügbaren Befehle");
		cg.setChat("/vsdbchat <ip_message_broker> <benutzername> <chatroom>");
		cg.setChat("/MAIL <ip_des_benutzers> <nachricht>     Sendet Mails");
		cg.setChat("/MAILBOX    Ruft Mails ab");
	}
	/**
	 * Sendet Messages
	 * @param msg zu sendente Massage
	 */
	public void sendMessage(String msg){
		//cc.sendMessage(msg);
		cg.setChat(msg);
		String[] arr = msg.split(" ");
		if(arr.length > 0){
			if(arr[0].equals("/help") || arr[0].equals("/?") || arr[0].equals("?"))
				this.helpman();
			if(arr[0].equals("/vsdbchat")){
				if(arr.length>=4){
					cc = new Client_Conection(arr[1], 61616, arr[2], "" ,arr[3], this);
					System.out.println("Conected!");
				}
			}
			if(arr[0].charAt(0) != '/')
				cc.sendMessage(msg);
		}
	}
	/**
	 * Verbindet mit Server
	 */
	
	public static void main(String[] args){
		new Client_Controller();
	}
}