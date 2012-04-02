/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignite.xema.mgenie.xmpp;

import java.util.Calendar;
import ignite.xema.mgenie.screens.gn_MainFrame;
import ignite.xema.mgenie.listeners.MessageListener;
import javax.microedition.lcdui.Image;
//import net.rim.device.api.i18n.DateFormat;

public class Buddy implements MessageListener {
	private static final long MAX_TIME_INTERVAL = 180000;

	public static gn_MainFrame btalk;
        Calendar c = Calendar.getInstance();
	//private static DateFormat dateGen = DateFormat.getInstance(DateFormat.TIME_DEFAULT);
        private String time;
	public String jid;
	public String name;
	public String group;
	public int status;
	public Image jidPic;
	public int unreadCount =0;
	public String custom_str;
	public boolean unread;

        public MessageListener ml = null;

	// is the last message from this buddy?
	public boolean lastFrom;
	public long lastTimeStampMe;
	public long lastTimeStampBuddy;

	//public MessageListField msgList;
	//public MessageRichTextField msgField;

	//private MessageScreen msgScreen;

	public Buddy(String id, String n, int s, String g) {
            if (n == null)
                this.name = id;
            else
                this.name = n;
            this.jid = id;
            this.status = s;
            this.jidPic=null;
            try  {
                this.jidPic = Image.createImage("/user-icon.png");
            } catch (Exception e ) {}
            this.group = g;
            this.lastFrom = false;
            this.lastTimeStampBuddy = 0;
            this.lastTimeStampMe = 0;
	}

	// TODO message list
	/*public MessageListField getMsgList() {
		if (msgList == null)
			msgList = new MessageListField();
		return msgList;
	}*/

	public void sendMessage(String msg) {
             time = c.get(Calendar.HOUR_OF_DAY) + ":" +c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                if (lastFrom) {
                        this.lastTimeStampMe = System.currentTimeMillis();
////                        msgScreen.sendMessage(msg, true, time);
                } else {
                    long curtime = System.currentTimeMillis();
                    if ((curtime - this.lastTimeStampMe) > MAX_TIME_INTERVAL)
////                        msgScreen.sendMessage(msg, true, time);
////                    else
////                        msgScreen.sendMessage(msg, false, null);
                    this.lastTimeStampMe = curtime;
                }
                lastFrom = false;
	}

	public void receiveMessage(String msg, boolean current) {

             time = c.get(Calendar.HOUR_OF_DAY) + ":" +c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
		if (!lastFrom) {
			this.lastTimeStampBuddy = System.currentTimeMillis();
////			this.getMsgScreen().receiveMessage(msg, current, true, time);
		} else {
                    long curTime = System.currentTimeMillis();
                    if ((curTime - this.lastTimeStampBuddy) > MAX_TIME_INTERVAL)
////                        msgScreen.receiveMessage(msg, current, true, time);
////                    else
////                        msgScreen.receiveMessage(msg, current, false, null);
                    this.lastTimeStampBuddy = curTime;
		}

		lastFrom = true;
	}

////	public MessageScreen getMsgScreen() {
////            if (msgScreen == null)
////                msgScreen = new MessageScreen(this);
////            return msgScreen;
////	}

        public void registerMessageListener(MessageListener ml) {
            this.ml = ml;
        }

        public void onMessage(String msgFrom, String msgTo, String msgBody){
            if (ml != null) {
                ml.onMessage(msgFrom,msgTo,msgBody);
            }
        }

	// status value
	public static final int STATUS_OFFLINE 	= 0x00000000;
	public static final int STATUS_AWAY 	= 0x00000001;
	public static final int STATUS_BUSY 	= 0x00000002;
	public static final int STATUS_ONLINE 	= 0x00000003;
}