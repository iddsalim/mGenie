/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ignite.xema.mgenie.screens;

import com.sun.midp.content.AppMonitor;
import java.io.IOException;

import ignite.xema.mgenie.gnMain;
import ignite.xema.mgenie.data.gn_HttpCaller;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.widget.Gauge;
import org.kalmeo.kuix.widget.PopupBox;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.MathFP;
import org.kalmeo.util.frame.Frame;
import org.kalmeo.util.worker.Worker;
import org.kalmeo.util.worker.WorkerTask;
import org.kalmeo.kuix.widget.Desktop;

import org.xml.sax.SAXException;

import org.kalmeo.kuix.widget.ListItem;

import javax.microedition.lcdui.Image;
import ignite.xema.mgenie.xmpp.jxa.Jxa;
import ignite.xema.mgenie.data.gn_UserData;
import ignite.xema.mgenie.data.gn_publicFunctions;

import ignite.xema.mgenie.xmpp.Buddy;
import ignite.xema.mgenie.xmpp.BuddyListForm;
import ignite.xema.mgenie.xmpp.BuddyVector;
import org.kalmeo.kuix.core.model.DataProvider;

import ignite.xema.mgenie.xmpp.jxa.XmppListener;
import ignite.xema.mgenie.xmpp.friendsList;
import ignite.xema.mgenie.xmpp.friendProperties;
import java.util.Random;

/**
 * @author omarino
 */
public class gn_MainFrame implements Frame, XmppListener {

	// Static frame instance
	public static final gn_MainFrame instance = new gn_MainFrame();

	private final Screen screen = Kuix.loadScreen("/xml/blank_screen.xml", null);
	private Desktop desktop;

        //XMPP
        public static Buddy currentBuddy;
        public static BuddyVector buddyList = null;
        public static BuddyListForm friends;
	private DataProvider dpBuddies =  new friendsList() ;

        public static String currUname = "";
        public static String currFullJID = "";
        public int resID = 0;
        public static String lUser = "";
        public static String lPass = "";
        public static boolean lAutoLog = false;
        private int timSplash = 1000;//5000 ;
        public static String xemaDomainName = "xema.mobi";

        public static Jxa jxa;

        private String rosterString = "";
        private String currentChatService = "";

        private Image offline_img;
        private Image online_img;
        public boolean isLoggedOnToXema = false;

        // status values
        public int state;
        public final static int STATE_STARTUP = 0x0;
        public final static int STATE_LOGINING = 0x1;
        public final static int STATE_ONLINE = 0x2;
        public final static int STATE_FAILED = 0x3;
        public final static int STATE_WAITING = 0x4;
        public final static int STATE_RETRYING = 0x5;
        //boolean login;

        private static int unreadCount;
        private byte[] unreadLock = new byte[0];

        /** the account to edit or create */
        Random generator = new Random();

        /**
	 * Show the main screen of the application
	 */
	public void showScreen() {
            screen.setCurrent();
	}

	public void addFriend(int state, String names, DataProvider reference, boolean after) {
            friendProperties media = new friendProperties();

            media.fullnames = names;

            media.state = state;

            if ( !(names.equals("")) ) {
                System.out.println ("Added : " + names);
                dpBuddies.addItem(friendsList.LIST_PROPERTY, media, reference, after);
            }
	}
	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onMessage(java.lang.Object, java.lang.Object[])
	 */
	public boolean onMessage(Object identifier, Object[] arguments) {
		/*if ("demo".equals(identifier)) {
			Kuix.getFrameHandler().pushFrame(KuixDemoDemosFrame.instance);
			if (arguments[0] instanceof String) {
				Kuix.getFrameHandler().processMessage(arguments[0], null);
			}
			return false;
		}*/

		if ("showMainScreen".equals(identifier)) {
                    showScreen();
                    return false;
		}

		if ("setLang".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    Kuix.initI18nSupport((String) arguments[0]);
                    showScreen();
                    return false;
		}

		if ("back".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();

                        if (!isLoggedOnToXema) {
                            showScreen();
                        } else {
                            Kuix.loadScreen("/xml/buddieslist.xml", dpBuddies).setCurrent();
                        }
                    return false;
		}

		if ("goHelp".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    desktop.setCurrentScreen(Kuix.loadScreen("/xml/forms/preloginhelp_popup.xml", null));
                    return false;
		}

		if ("dashBoard".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    desktop.setCurrentScreen(Kuix.loadScreen("/xml/postlogin.xml", null));
                    return false;
		}

		if ("goFriends".equals(identifier)) {
                    Kuix.alert("This demo is meant to show services only. Friends disabled");
                    return true;
		}

		if ("goTopup".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    desktop.setCurrentScreen(Kuix.loadScreen("/xml/forms/prelogintopup_popup.xml", null));
                    return false;
		}

		if ("goServices".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    Kuix.loadScreen("/xml/buddieslist.xml", dpBuddies).setCurrent();
                    return true;
		}

		if ("goSnooze".equals(identifier)) {
                    gnMain.getDefault().getDisplay().setCurrent(null);
                    return false;
		}

		if ("popupChat".equals(identifier)) {
			Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
			if (widget instanceof ListItem) {
                            currentChatService = ((ListItem) widget).getDataProvider().getValue("cNames").toString();
                            System.out.println("Chat with : " +  currentChatService);
                            //Load a form with text area and text for chat. Bas
                            Kuix.showPopupBox("/xml/singlechat.xml", null);

			}
                    return false;
		}

		if ("sendXMPP".equals(identifier)) {
                    String msg = (String) arguments[0];
                    jxa.sendMessage(currentChatService, msg);
                    return false;
		}

		if ("doYesAccept".equals(identifier)) {
                    String jId = (String) arguments[0];
                    //Ask - If YES:
                    jxa.subscribed(jId); // Accept
                    jxa.subscribe(jId);  // Auto-request
                    System.out.println("Accepted::: " +jId);
                    //back to results
                    return false;
		}

		if ("doNoAccept".equals(identifier)) {
                    String jId = (String) arguments[0];
                    //ASK - if NO:
                    jxa.unsubscribed(jId);
                    System.out.println("Rejected::: " +jId);
                    return false;
		}

		if ("doReconnect".equals(identifier)) {
                   try {
                        ignite.xema.mgenie.data.gn_UserData.getUserSettings();
                        System.out.println ("Getting user Settings OK...!!");

                        // 3 - Autologin
                        if (!lUser.equals("") && ! lPass.equals("")) {
                            loginSaaHii(lUser.toString(), lPass.toString());
                        }

                   } catch (Exception e ) {
                        System.out.println ("Error Getting user Settings...!!" + e.toString());
                   }
                    return false;
		}

		if ("goLogin".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    desktop.setCurrentScreen(Kuix.loadScreen("/xml/forms/preloginlogin.xml", null));
                    return false;
		}

		if ("processLogin".equals(identifier)) {
                    //  #uname.text, #pass.text,
			String uname = (String) arguments[0];
			String pass = (String) arguments[1];

			//Check for blanks
                        if (    (uname == null || "".equals(uname))
                             || (pass == null || "".equals(pass))
                            ) {

                            Kuix.alert( "Please fill in both the username and the password.");

			} else {
                            //All details filled

                            // 2 - Save user data
                            gn_UserData.saveUserSettings(uname.toString(), pass.toString(),  "1");

                            // 3 - Autologin
                            loginSaaHii(uname.toString(), pass.toString());

                        }

			return false;
		}

		if ("goJoin".equals(identifier)) {
                    desktop.getCurrentScreen().cleanUp();
                    desktop.setCurrentScreen(Kuix.loadScreen("/xml/forms/preloginregister.xml", null));
                    return false;
		}

		if ("processJoin".equals(identifier)) {
                    // #fname.text, #uname.text, #email.text, #mobile.text, #sex.value, #pass.text, #passc.text,
			String fname = (String) arguments[0];
			String uname = (String) arguments[1];
			String email = (String) arguments[2];
			String mobile = (String) arguments[3];
			String sex = (String) arguments[4];
			String pass = (String) arguments[5];
			String passc = (String) arguments[6];

			//Check for blanks
                        if (    (fname == null || "".equals(fname))
                                || (uname == null || "".equals(uname))
                                || (email == null || "".equals(email))
                                || (mobile == null || "".equals(mobile))
                                || (sex == null || "".equals(sex))
                                || (pass == null || "".equals(pass))
                                || (passc == null || "".equals(passc))
                            ) {

                            Kuix.alert( "Not all details were filled. Please rectify.");

			} else if ( !pass.equals(passc) ) {
                            Kuix.alert( "The password and confirmation do not match.");
			} else {
                        //All details filled

                            //1 - Create account
                            String resp = gn_HttpCaller.callServerSync(gnMain.strRegisterScript + "?u="+
                                    gn_HttpCaller.urlEncode(uname)+"&p="+
                                    gn_HttpCaller.urlEncode(pass)+
                                    "&fn="+ gn_HttpCaller.urlEncode( fname) +
                                    "&em="+ gn_HttpCaller.urlEncode(email) +
                                    "&mo="+ mobile +"&gn="+ sex +"");

                            System.out.println("Response: " +resp);

                            Kuix.alert("Account for  " + fname + " created successfully. Press OK to login" );

                            // 2 - Save user data
                            gn_UserData.saveUserSettings(uname.toString(), pass.toString(),  "1");

                            // 3 - Autologin
                            loginSaaHii(uname.toString(), pass.toString());

                        }

			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onAdded()
	 */
	public void onAdded() {
            desktop = Kuix.getCanvas().getDesktop();
            Widget splash = Kuix.loadWidget("/xml/splash.xml", null);

               try {
                    ignite.xema.mgenie.data.gn_UserData.getUserSettings();
                    System.out.println ("Getting user Settings OK...!!");

                    // 3 - Autologin
                    if (!lUser.equals("") && ! lPass.equals("")) {
                        loginSaaHii(lUser.toString(), lPass.toString());
                    }

               } catch (Exception e ) {
                    System.out.println ("Error Getting user Settings...!!" + e.toString());
               }

            Kuix.splash(2000, splash, "showMainScreen");
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onRemoved()
	 */
	public void onRemoved() {
            desktop.getCurrentScreen().cleanUp();
	}



    // # XMPP FUNCTIONS

   private void loginSaaHii(String uname, String upass) {
        //LOGIN
        resID = generatePin();
        currUname = uname;
        currFullJID = uname +"@"+xemaDomainName + "/"+"xema"+ resID;
        connectXmpp(uname +"@"+xemaDomainName, upass, "xema"+ resID, xemaDomainName, false);
    }

    private Image loadImage(String name) {
        Image image = null;
        try {
          image = Image.createImage(name);
        } catch (IOException ioe) {
          System.out.println(ioe);
        }
        return image;
    }

    private int generatePin() {  return 100000 + generator.nextInt(900000);    }

    public void onDebug(final String jid) {       java.lang.System.out.println("*DEBUG* onDebug: " + jid );     }

    public void sendMessage(final String msg) {
            (new Thread() {
                    public void run() {
                        jxa.sendMessage(currentBuddy.jid, msg);
                    }
            }).start();
//		jxa.sendMessage(currentBuddy.jid, msg);
//		currentBuddy.getMsgList().sendMsg(msg);
            currentBuddy.sendMessage(msg);
    }

    public void openBuddy(Buddy b) {
            if (currentBuddy != null){// && currentBuddy.getMsgScreen().isDisplayed()) {
                //this.popScreen(currentBuddy.getMsgScreen());
                //Open new message window if none exists...
            }

            if (b.unread) {
                    synchronized (unreadLock) {
                            --unreadCount;
                            if (unreadCount == 0) {
                                    //Notification.clearNotification();
                            }
                    }
                    b.unread = false;
            }
            this.currentBuddy = b;
            buddyList.buddyReposition(b);
            //this.pushScreen(b.getMsgScreen());
            //Show chat window
    }

    public void switchBuddy(Buddy b) {
            if (currentBuddy == b) {
                    return;
            } else if (currentBuddy != null) {// && currentBuddy.getMsgScreen().isVisible()) {
                //this.popScreen(currentBuddy.getMsgScreen());
                //Show chat window
                currentBuddy = null;
            }

            this.openBuddy(b);
    }

    //  N E W  J X A
    public void connectXmpp(final String JID, final String Password, final String jResource, final String Host, boolean ssl_5223) {
        //home.show(); - Login Indicator

        if (jxa != null) {            jxa.logoff();   jxa=null;     }

        String port;
        boolean use_ssl;
        if (ssl_5223) {
            port = "5223";
            use_ssl = true;
        } else {
            port = "5222";
            use_ssl = false;
        }
        //JID mJID = new JID(JID);
        //mJID.Resource = jResource;//"JuickME";
        jxa = new Jxa(JID, Password, jResource ,1, xemaDomainName,   port, use_ssl);
        jxa.addListener(this);
        jxa.start();
    }

    public void onConnFailed(String msg) {

        System.err.println("ConnFailed: " + msg);

        try {
            jxa.removeListener(this);
            jxa = null;
        } catch (NullPointerException e) { e.printStackTrace();}

        //Kuix.alert("Could not connect to eGenie service. Do you wish to reconnect?");
        Kuix.alert("Connection to mGenie lost. Do you wish to reconnect?", KuixConstants.ALERT_YES | KuixConstants.ALERT_NO | KuixConstants.ALERT_QUESTION, "doReconnect()", "back()");

        //showLoginForm(false);
        //this.onConnFailed(msg);
    }

    public void onAuth(final String resource) {
        System.err.println("Auth: " + resource);

 /*       // Initialize Internal Forms
        home = new MainMenuForm(this);
        home.setListener(this);

        friends = new BuddyForm("Friends" );                    //1 - Poss : Loaded after roster
        content = new EmptyFormSkeleton("Content Channels");    //2
        //marikiti = new EmptyFormSkeleton("Xema Market");      //3
        profile = new EmptyFormSkeleton("My Profile");          //4
        credo = new EmptyFormSkeleton("Account Credit");        //5
        settings = new EmptyFormSkeleton("My Settings");        //6
        //games = new EmptyFormSkeleton("Play Games");            //7
        helpfaq = new EmptyFormSkeleton("Help and FAQ");        //8

        //SubForms
        inviteform = new frmInvite();
        searchform = new frmSearchFriends();
*/
        //buddyList = new BuddyVector(this);
        //Buddy.btalk = this;
        //BuddyVector.btalk = this;

        //acc.storageWrite();
        try {
            jxa.getRoster();
            jxa.setStatus("available", "Ready to chat...", 2);
            //jxa.sendPresence(null, null, null, null, 1);
        } catch (final IOException e) {
            System.err.println(e);
        }
        //friends = new FriendsScreen("Friends" );                 // ^-^ 1

        isLoggedOnToXema = true;

        desktop.getCurrentScreen().cleanUp();
        desktop.setCurrentScreen(Kuix.loadScreen("/xml/postlogin.xml", null));

        //showMainForm("Xema - " + this.currUname);
    }

    public void onAuthFailed(final String message) {
        System.err.println("AuthFailed: " + message);
        isLoggedOnToXema = false;
        Kuix.alert("Could not login with the details provided. Try again.");
        //showDialog(intlIze("txtWrongLoginTitle"), intlIze("txtWrongLoginBody"), Dialog.TYPE_WARNING, 5000);
        //showLoginForm(false);
        //this.onAuthFailed(message);
    }

    public void subscribe(final String jid) {
            (new Thread() {
                    public void run() { jxa.subscribe(jid); }
            }).start();
    }

    public void unsubscribe(final String jid) {
            (new Thread() {
                    public void run() { jxa.unsubscribe(jid);}
            }).start();
    }

    public void logoffJxa() {
            (new Thread() {
                    public void run() { jxa.logoff();   }
            }).start();
    }

    public void onContactOverEvent() {
        System.out.println("Contacts parsing over...");
        // TODO Auto-generated method s
    }

    public void onContactEvent(final String jid, final String name, final String group, final String subscription) {
            Image imgJid = null;
            System.out.println("[debug] contact: "+jid+", Name: "+name+",GRP: "+group+ ", SUBS: "+subscription );
            // TODO Auto-generated method stub

            contactBtalkHandler(jid, name, group, subscription);
                /*if (subscription.equals("both") && buddyList.findBuddyIndex(jid) == -1) {
                    (new Thread() {
                        public void run() {
                            //    jxa.logoff();
                            buddyList.addBuddy(new Buddy(jid, name, Buddy.STATUS_OFFLINE, group));
                            System.out.println("[BUDDY] :> "+jid);
                        }
                    }).start();
                }*/
        }

    public void onSubscribeEvent(final String jid) { //Friend Request
        // TODO Auto-generated method stub
        System.out.println("[DEBUG] subscribe from: "+jid);

        //Kuix.showPopupBox(null, 0, "", "Friend request from " + gn_publicFunctions.nameFromEmail(jid) + ". \nAccept?".toString(), "YES", "NO", null);
        Kuix.alert("Accept buddy request from "+ gn_publicFunctions.nameFromEmail(jid) +" ?", KuixConstants.ALERT_YES | KuixConstants.ALERT_NO | KuixConstants.ALERT_QUESTION, "doYesAccept("+jid+")", "doNoAccept("+jid+")");
        /*final Dialog dialog = new Dialog();
            dialog.setTitle("Friend request");
            dialog.addComponent(new Label("From " + publicFunctions.nameFromEmail(jid) + ". \nAccept?"));
            dialog.addCommand(new Command("Yes"));  dialog.addCommand(new Command("No"));
                dialog.addCommandListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        Command cmd = evt.getCommand();
                        System.out.println("You selected " + cmd.getCommandName() + " " );
                            if (cmd.getCommandName().equals("Yes")) {
                                //Accept friend request:
                                //Ask - If YES:
                                jxa.subscribed(jid); // Accept
                                jxa.subscribe(jid);  // Auto-request
                                //back to results
                            } else {
                                //ASK - if NO:
                                jxa.unsubscribed(jid);
                            }
                        dialog.dispose();
                    }
                });
            dialog.show();*/
    }

    public void onUnsubscribeEvent(final String jid) { // Deleted by Friend
        // TODO Auto-generated method stub
        System.out.println("[DEBUG] unsubscribe from: " + jid);
        buddyList.deleteBuddy(jid);
        final int idx = buddyList.findBuddyIndex(jid);
        jxa.unsubscribe(jid);
///        friends.removeFriend(idx);
        Kuix.alert( jid + " has deleted you from their buddy list");
    }

    public void onMessageEvent(final String from, final String body) {
            //System.out.println("[debug] message: "+from+" "+ body);
            if (body.length() == 0){
                return;
            } else {
                // TODO add notify function
                messageBtalkHandler(from, body);
            }
    }

    public void onStatusEvent(final String jid, final String show, final String status) {
            //System.out.println("[debug] status:"+jid+" "+show+ " "+ status);
            // TODO Auto-generated method stub
            int idx = jid.indexOf('/');
            final String id;
            // in some instance, jid contains no '/', fix this
            if (idx == -1) {
                    id = new String(jid);
            } else {
                    id = jid.substring(0, jid.indexOf('/'));
            }
            statusBtalkHandler(id, show, status);
    }

    // H A N D L E R S
    private void statusBtalkHandler(final String jid, final String show, final String status) {
        int idx = buddyList.findBuddyIndex(jid);
        Buddy b;
        System.out.println("[BTALK] Status Handler... Got ["+ show +"] - ("+ status +") from ID : " + idx +" - " + jid);
        if (idx != -1) {
                b = (Buddy)buddyList.buddyVector.elementAt(idx);
                int state = 0;

                if (show.equals("")){ state = Buddy.STATUS_ONLINE;}
                    else if (show.equals("chat")) { state = Buddy.STATUS_ONLINE;}
                    else if (show.equals("away")) { state = Buddy.STATUS_AWAY;}
                    else if (show.equals("xa")){ state = Buddy.STATUS_AWAY;}
                    else if (show.equals("dnd")){ state = Buddy.STATUS_BUSY;}
                    else if (show.equals("na")){ state = Buddy.STATUS_OFFLINE;}
                else { System.out.println("[BTALK] Unhandled status: "+jid+" " + show+ " "+status); }

                b.custom_str = status;
                if (b.status == state)
                        return;
                else {
                        b.status = state;
                        buddyList.buddyReposition(idx);
                }
////                friends.onPresence(state);
        } else {
                System.out.println("[BTALK] No buddy matches: "+jid+" " + show+ " "+status);
        }
    }

    private void messageBtalkHandler(final String from, final String body) {
            boolean isCurrentBuddy;
            if (currentBuddy != null && currentBuddy.jid.equalsIgnoreCase(from)) {
                    isCurrentBuddy = true;
//			synchronized (BTalk.getEventLock()) {
//				currentBuddy.receiveMessage(body, true);
//			}

                    currentBuddy.receiveMessage(body, true);

                    //Notification.newMessage(currentBuddy, body, isCurrentBuddy);
                    System.out.println("[BTALK] New Msg: "+currentBuddy.jid+", " + body+ ", "+isCurrentBuddy);

            // from other buddy
            } else {
                    isCurrentBuddy = false;
                    final int idx = buddyList.findBuddyIndex(from);
                    if (idx != -1) {
                            final Buddy b = buddyList.getBuddyAt(idx);

                            if (!b.unread) {
                                    synchronized (unreadLock) {
                                            ++unreadCount;
                                    }
                                    b.unread = true;
                            }
                            b.receiveMessage(body, false);
                            buddyList.buddyReposition(idx);

                            System.out.println("[BTALK] New Msg: "+b.jid+", " + body);//+ ", "+ (isCurrentBuddy=true ?"YES" : "NO"));
                            Kuix.alert("From : " + b.jid + "\n" + body);
                            //Notification.newMessage(b, body, isCurrentBuddy);
                    } else {
                            System.out.println("[warning] Message from unknown buddy");
                    }
            }
//		Notification.newMessage(from, isCurrentBuddy);
            //Add message to queue
            //BuddyForm.onMessage(from, this.currUname, body);
    }

    private void contactBtalkHandler(final String jid, final String name, final String group, final String subscription) {
        if (subscription.equals("both") && buddyList.findBuddyIndex(jid) == -1) {
            (new Thread() {
                public void run() {
                    Widget widget;

                    Buddy bd = new Buddy(jid, name, Buddy.STATUS_OFFLINE, group);
                    buddyList.addBuddy(bd);

                    widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();

                    if (widget != null) {
                        addFriend(Buddy.STATUS_ONLINE, jid, widget.getDataProvider() , true);
                    } else {
                        addFriend(Buddy.STATUS_ONLINE, jid, null , false);
                    }

                    //System.out.println("FRIENDS: " + buddyList.buddyVector.size() + "...");
////                    friends.onRosterItem(bd);
                }
            }).start();
        }
        //System.out.println("++++>> Friends Count: " + buddyList.buddyVector.size());
    }

}
