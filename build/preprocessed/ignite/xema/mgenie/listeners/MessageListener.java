/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignite.xema.mgenie.listeners;

/**
 *
 * @author haxor
 */
public interface MessageListener {

    public void onMessage(String msgFrom, String msgTo, String msgBody);

}
