/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignite.xema.mgenie.xmpp;

import java.util.Vector;

import ignite.xema.mgenie.screens.gn_MainFrame;
//import maven.xema.v1.xmppelements.Buddy;
/*
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;
*/
public class BuddyVector  {
	/*public static final Bitmap offlineIcon = Bitmap.getBitmapResource("img/offline.png");
	public static final Bitmap awayIcon = Bitmap.getBitmapResource("img/away.png");
	public static final Bitmap busyIcon = Bitmap.getBitmapResource("img/busy.png");
	public static final Bitmap onlineIcon = Bitmap.getBitmapResource("img/online.png");
	public static final Bitmap unreadIcon = Bitmap.getBitmapResource("img/unread.png");

	public static final Bitmap[] statusIcon = new Bitmap[]{offlineIcon, awayIcon, busyIcon, onlineIcon};
	*/
	public Vector buddyVector = null;
	//private final MainCanvas btalk;
        public static gn_MainFrame btalk;

	public BuddyVector(gn_MainFrame b) {
            btalk = b;
            buddyVector = new Vector();
	}

	public Buddy getBuddyAt(int index) {
            return (Buddy)buddyVector.elementAt(index);
	}

	public int getBuddyIndex(Buddy b) {
            return buddyVector.indexOf(b);
	}

	public void buddyReposition(Buddy b) {
            int index = buddyVector.indexOf(b);
            buddyReposition(index);
	}

	public void buddyReposition(int oldIndex) {
		Buddy b = (Buddy)buddyVector.elementAt(oldIndex);
		int newIndex = 0;

		if (b.unread) {
			newIndex = 0;
		} else {
                    while (newIndex < buddyVector.size() &&
                        ((b == (Buddy)buddyVector.elementAt(newIndex)) ||
                        ((Buddy)buddyVector.elementAt(newIndex)).unread ||
                        (b.status < ((Buddy)buddyVector.elementAt(newIndex)).status)))
                        ++newIndex;
		}

		newIndex = (oldIndex < newIndex) ? (newIndex-1) : newIndex;

		if (oldIndex != newIndex) {
			buddyVector.removeElementAt(oldIndex);
			buddyVector.insertElementAt(b, newIndex);
		}

//		synchronized (BTalk.getEventLock()) {
//			this.invalidate();
//		}
		//this.invalidate();
	}

	public int findBuddyIndex(String jid) {
		for (int i = buddyVector.size()-1; i >= 0; i--) {
			if (((Buddy)buddyVector.elementAt(i)).jid.equalsIgnoreCase(jid))
				return i;
		}

		return -1;
	}

	public Buddy findBuddy(String jid) {
		for (int i = buddyVector.size()-1; i >= 0; i--) {
			if (((Buddy)buddyVector.elementAt(i)).jid.equalsIgnoreCase(jid))
				return (Buddy)buddyVector.elementAt(i);
		}
		return null;
	}

	public void addBuddy(Buddy b) {
		buddyVector.addElement(b);
		//this.insert(buddyVector.indexOf(b));
	}

	public boolean deleteBuddy(String jid) {
		int idx;
		for (idx = buddyVector.size()-1; idx >= 0; idx--) {
			if (((Buddy)buddyVector.elementAt(idx)).jid.equalsIgnoreCase(jid))
				break;
		}

		/*if (idx >= 0) {
			buddyVector.removeElementAt(idx);
			this.delete(idx);
			this.getScreen().invalidate();
			return true;
		} else {
			return false;
		}*/ return false;
	}
	/*
	public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
		// TODO finish drawListRow
		// NOTICE 14 would be consist the icon size
		Buddy b = (Buddy)buddyVector.elementAt(index);
		if (b.unread) {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), unreadIcon, 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, unreadIcon, 0, 0);
		} else {
			//graphics.drawBitmap(1, y+((this.getRowHeight()-icon.getWidth())>>1), icon.getWidth(), icon.getWidth(), statusIcon[b.status], 0, 0);
			graphics.drawBitmap(1, y+((this.getRowHeight()-14)>>1), 14, 14, statusIcon[b.status], 0, 0);
		}
		//graphics.drawText(b.name, icon.getWidth()+2, y, DrawStyle.ELLIPSIS, width-icon.getWidth()-2);
		graphics.drawText(b.name, 16, y, DrawStyle.ELLIPSIS, width-16);
	}
        */
	public void clearBuddies() {
            // TODO Auto-generated method stub
            buddyVector = null;
            /*if (btalk.buddyList != null) {
                    int i = buddyVector.size();
                    while (i-- > 0)
                            this.delete(0);
                    btalk.buddyscreen.delete(btalk.buddyList);
                    btalk.buddyList = null;
            }*/
	}

	public void invalBuddies() {
            /*if (btalk.buddyList != null) {
                for (int i = buddyVector.size()-1; i >= 0; i--) {
                        Buddy b = (Buddy) buddyVector.elementAt(i);
                        b.status = Buddy.STATUS_OFFLINE;
                }

                this.invalidate();
            }*/
	}
}
