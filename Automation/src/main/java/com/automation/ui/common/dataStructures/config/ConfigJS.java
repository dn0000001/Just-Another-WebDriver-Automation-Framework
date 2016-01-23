package com.automation.ui.common.dataStructures.config;

/**
 * This class contains configuration JavaScript settings
 */
public class ConfigJS {
	/**
	 * The base folder that contains all JavaScript files<BR>
	 * <BR>
	 * <B>Notes:</B><BR>
	 * 1) The ConfigRun.BaseJavaScriptFolder needs to be set with the desired location before any variable in
	 * this class is accessed because initialization of all the static variables will occur at this time.<BR>
	 */
	public static final String baseFolder = ConfigRun.BaseJavaScriptFolder;

	/*
	 * Available JavaScript files
	 */
	public static String _Click = baseFolder + "Click.js";
	public static String _ClickWithAlert = baseFolder + "ClickWithAlert.js";
	public static String _GetAttribute = baseFolder + "GetAttribute.js";
	public static String _GetChildren = baseFolder + "GetChildren.js";
	public static String _GetParent = baseFolder + "GetParent.js";
	public static String _GetSiblings = baseFolder + "GetSiblings.js";
	public static String _GetText = baseFolder + "GetText.js";
	public static String _GetValue = baseFolder + "GetValue.js";
	public static String _HTML_Event = baseFolder + "HTML_Event.js";
	public static String _MaximizeWindow = baseFolder + "MaximizeWindow.js";
	public static String _MouseEvent = baseFolder + "MouseEvent.js";
	public static String _MoveWindow = baseFolder + "MoveWindow.js";
	public static String _ResizeWindow = baseFolder + "ResizeWindow.js";
	public static String _ScrollIntoView = baseFolder + "ScrollIntoView.js";
	public static String _XpathPosition = baseFolder + "XpathPosition.js";
	public static String _GenericIsReadyKO = baseFolder + "GenericIsReadyKO.js";
	public static String _GetNonWatermarkInputValue = baseFolder + "GetNonWatermarkInputValue.js";
	public static String _IsVisible = baseFolder + "IsVisible.js";
	public static String _ScrollToBottom = baseFolder + "ScrollToBottom.js";
	public static String _GetChecked = baseFolder + "GetChecked.js";
	public static String _GetTable = baseFolder + "GetTable.js";
	public static String _GetDropDownOptions = baseFolder + "GetDropDownOptions.js";
	public static String _SetDropDownByIndex = baseFolder + "SetDropDownByIndex.js";
}
