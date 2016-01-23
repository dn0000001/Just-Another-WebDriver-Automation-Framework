package com.automation.ui.common.dataStructures;

/**
 * Variables to trigger a HTML Event in JavaScript<BR>
 * See <B>https://developer.mozilla.org/en-US/docs/Web/API/event.initEvent</B> for more details.<BR>
 */
public class HTML_Event {
	public HTML_EventType type;
	public boolean bubbles;
	public boolean cancelable;

	/**
	 * Constructor - Initializes other variables to defaults for use in Framework class
	 * 
	 * @param type - HTML Event type
	 */
	public HTML_Event(HTML_EventType type)
	{
		init(type, false, true);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param type - The type of event.
	 * @param bubbles - A boolean indicating whether the event should bubble up through the event chain or not
	 *            (see bubbles).
	 * @param cancelable - A boolean indicating whether the event can be canceled (see cancelable).
	 */
	protected void init(HTML_EventType type, boolean bubbles, boolean cancelable)
	{
		this.type = type;
		this.bubbles = bubbles;
		this.cancelable = cancelable;
	}

	/**
	 * String that represents the parameters to method <B>initMouseEvent</B> on object
	 * document.createEvent("MouseEvents")<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * if ('createEvent' in document) {<BR>
	 * &nbsp;&nbsp;var evt = document.createEvent('HTMLEvents');<BR>
	 * &nbsp;&nbsp;evt.initEvent( <B> toString() </B> );<BR>
	 * &nbsp;&nbsp;document.getElementById('orig').dispatchEvent(evt);<BR>
	 * }
	 */
	public String toString()
	{
		String comma = ", ";
		return "'" + type.toString() + "'" + comma + bubbles + comma + cancelable;
	}
}
