/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ignite.xema.mgenie;

import ignite.xema.mgenie.screens.gn_MainFrame;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.widget.Desktop;

/**
 * @author omarino
 */
public class gnMain extends KuixMIDlet {

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopStyles()
	 */

        //public static final String strRegisterScript = "http://lab.xema.mobi/mgenie/xmpphp-0.1rc2-r77/register_user.php";
        public static final String strRegisterScript = "http://xema.mobi/mgenie/bg/register_user.php";

	public void initDesktopStyles() {
		Kuix.loadCss("/css/style.css");
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopContent(org.kalmeo.kuix.widget.Desktop)
	 */
	public void initDesktopContent(Desktop desktop) {
		// Push the MainMenuFrame
		Kuix.getFrameHandler().pushFrame(gn_MainFrame.instance);
	}

}