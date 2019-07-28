/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caprica.ava.db.bgl;

/**
 *
 * @author moji
 */
  public class XDictHeader
    {
        public String Source;
        public String Url;
        public String Comment;

        public XDictHeader() { }

        public XDictHeader(String source, String url, String comment)
        {
            Source = source;
            Url = url;
            Comment = comment;
        }
    }
