/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caprica.ava.db.bgl;

/**
 * 
 * @author moji
 */
class BabylonConsts {
	public static String[] Bgl_language = { "English", "French", "Italian",
			"Spanish", "Dutch", "Portuguese", "German", "Russian", "Japanese",
			"Traditional Chinese", "Simplified Chinese", "Greek", "Korean",
			"Turkish", "Hebrew", "Arabic", "Thai", "Other",
			"Other Simplified Chinese dialects",
			"Other Traditional Chinese dialects",
			"Other Eastern-European languages",
			"Other Western-European languages", "Other Russian languages",
			"Other Japanese languages", "Other Baltic languages",
			"Other Greek languages", "Other Korean dialects",
			"Other Turkish dialects", "Other Thai dialects", "Polish",
			"Hungarian", "Czech", "Lithuanian", "Latvian", "Catalan",
			"Croatian", "Serbian", "Slovak", "Albanian", "Urdu", "Slovenian",
			"Estonian", "Bulgarian", "Danish", "Finnish", "Icelandic",
			"Norwegian", "Romanian", "Swedish", "Ukrainian", "Belarusian",
			"Farsi", "Basque", "Macedonian", "Afrikaans", "Faeroese", "Latin",
			"Esperanto", "Tamazight", "Armenian" };

	public static String[] Bgl_charsetname = { "Default", "Latin",
			"Eastern European", "Cyrillic", "Japanese", "Traditional Chinese",
			"Simplified Chinese", "Baltic", "Greek", "Korean", "Turkish",
			"Hebrew", "Arabic", "Thai" };
	static byte[] GIF = { 71, 73, 70, 56 };
    static   byte[] BMP = {66, 77};
	static byte[] JPG = { (byte) 255, (byte) 216, (byte) 255 };
	static byte[] PNG = { (byte) 137, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13,
			73, 72, 68, 82 };
	public static String[] Bgl_charset = { "Windows-1252", /* Default */
	"Windows-1252", /* Latin */
	"ISO-8859-2", /* Eastern European */
	"windows-1251", /* Cyriilic */
	"Windows-932", /* Japanese */
	"Windows-950", /* Traditional Chinese */
	"Windows-936", /* Simplified Chinese */
	"Windows-1257", /* Baltic */
	"Windows-1253", /* Greek */
	"Windows-949", /* Korean */
	"windows-1254", /* Turkish */
	"Windows-1255", /* Hebrew */
	"Windows-1256", /* Arabic */
	"Windows-874" /* Thai */
	};
}