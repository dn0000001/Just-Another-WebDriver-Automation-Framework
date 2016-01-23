package com.automation.ui.common.dataStructures;

/**
 * Mouse Events that are supported in JavaScript<BR>
 * See <B>https://developer.mozilla.org/en-US/docs/Web/API/event.initMouseEvent</B> for more details.<BR>
 * <BR>
 * <B>Notes:</B><BR>
 * 1) Do not change the enumeration values as they need to match the correct mouse event exactly when
 * converting to a string<BR>
 */
public enum MouseEventType
{
	mouseover, mouseout, click, mousedown, mouseup, mousemove
}
