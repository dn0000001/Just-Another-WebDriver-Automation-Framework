package com.automation.ui.common.dataStructures;

/**
 * HTML Events that are supported in JavaScript<BR>
 * See <B>http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-eventgroupings-htmlevents</B> for more
 * details.<BR>
 * <BR>
 * <B>Notes:</B><BR>
 * 1) Do not change the enumeration values as they need to match the correct HTML event exactly when
 * converting to a string<BR>
 */
public enum HTML_EventType
{
	load, unload, abort, error, select, change, submit, reset, focus, blur, resize, scroll
}
