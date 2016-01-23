package com.automation.ui.common.dataStructures;

/**
 * Variables to trigger a Mouse Event in JavaScript<BR>
 * See <B>https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent</B> for more details.<BR>
 */
public class MouseEvent {
	public MouseEventType type;
	public boolean canBubble;
	public boolean cancelable;
	public String view;
	public int detail;
	public int screenX;
	public int screenY;
	public int clientX;
	public int clientY;
	public boolean ctrlKey;
	public boolean altKey;
	public boolean shiftKey;
	public boolean metaKey;
	public int button;
	public String relatedTarget;

	/**
	 * Constructor - Initializes other variables to defaults for use in Framework class
	 * 
	 * @param type - Mouse Event type
	 */
	public MouseEvent(MouseEventType type)
	{
		init(type, true, false, "window", 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	}

	/**
	 * Initialize all variables
	 * 
	 * @param type - the string to set the event's type to. Possible types for mouse events include: click,
	 *            mousedown, mouseup, mouseover, mousemove, mouseout.
	 * @param canBubble - whether or not the event can bubble. Sets the value of event.bubbles.
	 * @param cancelable - whether or not the event's default action can be prevented. Sets the value of
	 *            event.cancelable.
	 * @param view - the Event's AbstractView. You should pass the window object here. Sets the value of
	 *            event.view.
	 * @param detail - the Event's mouse click count. Sets the value of event.detail.
	 * @param screenX - the Event's screen x coordinate. Sets the value of event.screenX.
	 * @param screenY - the Event's screen y coordinate. Sets the value of event.screenY.
	 * @param clientX - the Event's client x coordinate. Sets the value of event.clientX.
	 * @param clientY - the Event's client y coordinate. Sets the value of event.clientY.
	 * @param ctrlKey - whether or not control key was depressed during the Event. Sets the value of
	 *            event.ctrlKey.
	 * @param altKey - whether or not alt key was depressed during the Event. Sets the value of event.altKey.
	 * @param shiftKey - whether or not shift key was depressed during the Event. Sets the value of
	 *            event.shiftKey.
	 * @param metaKey - whether or not meta key was depressed during the Event. Sets the value of
	 *            event.metaKey.
	 * @param button - the Event's mouse event.button.
	 * @param relatedTarget - the Event's related EventTarget. Only used with some event types (e.g. mouseover
	 *            and mouseout). In other cases, pass null.
	 */
	protected void init(MouseEventType type, boolean canBubble, boolean cancelable, String view, int detail,
			int screenX, int screenY, int clientX, int clientY, boolean ctrlKey, boolean altKey,
			boolean shiftKey, boolean metaKey, int button, String relatedTarget)
	{
		this.type = type;
		this.canBubble = canBubble;
		this.cancelable = cancelable;
		this.view = view;
		this.detail = detail;
		this.screenX = screenX;
		this.screenY = screenY;
		this.clientX = clientX;
		this.clientY = clientY;
		this.ctrlKey = ctrlKey;
		this.altKey = altKey;
		this.shiftKey = shiftKey;
		this.metaKey = metaKey;
		this.button = button;
		this.relatedTarget = relatedTarget;
	}

	/**
	 * String that represents the parameters to method <B>initMouseEvent</B> on object
	 * document.createEvent("MouseEvents")<BR>
	 * <BR>
	 * <B>Example:</B><BR>
	 * if ('createEvent' in document) {<BR>
	 * &nbsp;&nbsp;var evt = document.createEvent('MouseEvents');<BR>
	 * &nbsp;&nbsp;evt.initMouseEvent( <B> toString() </B> );<BR>
	 * &nbsp;&nbsp;document.getElementById('red').dispatchEvent(evt);<BR>
	 * }
	 */
	public String toString()
	{
		String comma = ", ";
		return "'" + type.toString() + "'" + comma + canBubble + comma + cancelable + comma + view + comma
				+ detail + comma + screenX + comma + screenY + comma + clientX + comma + clientY + comma
				+ ctrlKey + comma + altKey + comma + shiftKey + comma + metaKey + comma + button + comma
				+ relatedTarget;
	}
}
