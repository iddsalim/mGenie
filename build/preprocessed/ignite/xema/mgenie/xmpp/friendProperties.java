/*
 * This file is part of org.kalmeo.demo.kuix.
 *
 * org.kalmeo.demo.kuix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * org.kalmeo.demo.kuix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.demo.kuix.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Creation date : 10 mar. 2008
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 */

package ignite.xema.mgenie.xmpp;

import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.util.LinkedListItem;

/**
 * @author omarino
 */
public class friendProperties extends DataProvider {

	public final static int NAMES_FLAG = 0;
	public final static int STATE_FLAG = 1;

	private final static String NAMES_PROPERTY = "cNames";
	private final static String STATE_PROPERTY = "cState";

	public int state;
	public String fullnames;

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.model.DataProvider#getUserDefinedValue(java.lang.String)
	 */
	protected Object getUserDefinedValue(String property) {
		if (NAMES_PROPERTY.equals(property)) {
			return fullnames;
		}
		if (STATE_PROPERTY.equals(property)) {
			return Integer.toString(state);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.model.DataProvider#compareTo(org.kalmeo.util.LinkedListItem, int)
	 */
	public int compareTo(LinkedListItem item, int flag) {
		if (item instanceof friendProperties) {
			friendProperties friend = (friendProperties) item;
			switch (flag) {
                            case NAMES_FLAG :
                                    return fullnames.compareTo(friend.fullnames);
                            case STATE_FLAG :
                                    return friend.state - state;
			}
		}
		return 0;
	}
}
