/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caprica.ava.db.bgl;

import java.util.ArrayList;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author moji
 */
public class XDict {

    private ArrayList<XDictHeader> headers = new ArrayList<XDictHeader>();

    public ArrayList<XDictHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<XDictHeader> headers) {
        this.headers = headers;
    }
    
    /// <summary>
    /// Entries for each word.
    /// </summary>
    /// <remarks>Do not access directly, use AddEntry instead.</remarks>
    private ArrayList<XDictEntry> entries = new ArrayList<XDictEntry>();

    public ArrayList<XDictEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<XDictEntry> entries) {
        this.entries = entries;
    }
    
    //private MultiDictionary<String, XDictEntry> _index = new MultiDictionary<String, XDictEntry>();

    public XDict() {
    } // XML

    /// <summary>
    /// Load from an XML file
    /// </summary>
    /// <param name="file"></param>
    /// <returns></returns>
       /* no need to load data from xml 
     public static XDict Load(String file)
     {
     XDict d = Utils.LoadXml<XDict>(file);
     d.Reindex();
     return d;
     }*/
    /// <summary>
    /// Add an entry to the dictionary and index
    /// </summary>
    /// <param name="entry"></param>
    public void AddEntry(XDictEntry entry) {
        entries.add(entry);
      //  _index.add(entry.getWord().getText(), entry);
    }
    /// <summary>
    /// Get the entry for the given word
    /// </summary>
    /// <param name="wordText"></param>
    /// <returns></returns>
    

    //void Reindex() {
  //      _index.clear();
//        for(XDictEntry e : entries)
     //       {
             //   _index.add(e.getWord().getText(), e);
   //     }
 //   }

    /// <summary>
    /// Save to an XML file
    /// </summary>
    /// <param name="file"></param>
/*    public void Save(String file) {
        Entries.Sort();
        Utils.SaveXml<XDict>
        (file
        , this
    

    );
        }
*/
        /// <summary>
        /// Get unique words in the given language. Pass null to get words in any language.
        /// </summary>
        /// <param name="lang"></param>
        /// <returns></returns>
        public Set<String> GetUniqueWords(String lang) {
        Set<String> uniqueWords = new TreeSet<String>() ;
        for(XDictEntry entry : this.entries)
            {
                uniqueWords.add(entry.getWord().getText());
        }

        return uniqueWords;
    }
}
