/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caprica.ava.db.bgl;

/**
 *
 * @author moji
 */
public class XWordInfo {

    private String text;
    private String partOfSpeech;
   // private boolean partOfSpeechSpeficied;

    public boolean isPartOfSpeechSpeficied() {
        return partOfSpeech != null || partOfSpeech == "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public XWordInfo() {
    } // XML

    public XWordInfo(String text) {
        this(text, null);
    }

    public XWordInfo(String text, String partOfSpeech) {
        this.text = text;
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public String toString() {
        return (this.text
                + ((this.partOfSpeech != null || "".equals(this.partOfSpeech)) ? "" : "/" + partOfSpeech));

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XWordInfo){
            return true;
        }else
            return false;
    }

  
}