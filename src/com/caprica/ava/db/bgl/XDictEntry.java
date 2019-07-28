/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caprica.ava.db.bgl;

import java.util.ArrayList;
 

/**
 *
 * @author moji
 */
public class XDictEntry {

    private XWordInfo word;
    // Index of the source header of the entry
    ///[XmlAttribute("src")]
    private int sourceIndex = 0;
    // Important elements
    // [XmlElement("Translation")]
    private ArrayList<XWordInfo> translations = new ArrayList<XWordInfo>();
    //[XmlElement("Definition")]
    private ArrayList<XWordInfo> definitions = new ArrayList<XWordInfo>();
    // Skip serialization of SourceIndex on default value
    // [XmlIgnore]
 
 
    
  
      private ArrayList<XWordInfo> synonyms = new ArrayList<XWordInfo>();
    //[XmlElement("Antonym")]
    private ArrayList<XWordInfo> antonyms = new ArrayList<XWordInfo>();
    //[XmlElement("Sample")]
    private ArrayList<XWordInfo> samples = new ArrayList<XWordInfo>();
//    [XmlElement("Comment")]
    private ArrayList<XWordInfo> comments = new ArrayList<XWordInfo>();
    //[XmlElement("Classifier")] // Thai classifier (khon, tua, an etc.)
    private String classifier;

    public XDictEntry() {
    }

    public XDictEntry(XWordInfo word) {
        this.word = word;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.word.toString());

        appendWords(sb, "-->", translations);
        appendWords(sb, "def", definitions);
        appendWords(sb, "syn", synonyms);
        appendWords(sb, "ant", antonyms);
        appendWords(sb, "sam", samples);
        appendWords(sb, "com", comments);
        if (!(classifier == null || "".equals(classifier))) {
            sb.append(" num: ").append(classifier);
        }
        return sb.toString();
    }

    public XWordInfo getFirstTranslation() {
        return null;
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(word.getText());
        if (word.isPartOfSpeechSpeficied()) {
            sb.append("/").append(word.getPartOfSpeech());
        }

        XWordInfo wi = getFirstTranslation();
        if (wi != null) {
            sb.append(" -> ").append(wi.getText());
        }

        return sb.toString();
    }

    private void appendWords(StringBuilder sb, String cat, ArrayList<XWordInfo> list) {
        for (XWordInfo s : list) {
            sb.append(" ").append(cat).append(": ").append(s);

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XDictEntry) {
            return true;
        }
        return false;
    }

   
    public boolean isSourceIndexSpecified() {

        return sourceIndex != 0;
    }

    public ArrayList<XWordInfo> getAntonyms() {
        return antonyms;
    }

    public String getClassifier() {
        return classifier;
    }

    public ArrayList<XWordInfo> getComments() {
        return comments;
    }

    public ArrayList<XWordInfo> getDefinitions() {
        return definitions;
    }

    public ArrayList<XWordInfo> getSamples() {
        return samples;
    }

    public int getSourceIndex() {
        return sourceIndex;
    }

    public ArrayList<XWordInfo> getSynonyms() {
        return synonyms;
    }

    public ArrayList<XWordInfo> getTranslations() {
        return translations;
    }

    public XWordInfo getWord() {
        return word;
    }

    public void setAntonyms(ArrayList<XWordInfo> antonyms) {
        this.antonyms = antonyms;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void setComments(ArrayList<XWordInfo> comments) {
        this.comments = comments;
    }

    public void setDefinitions(ArrayList<XWordInfo> definitions) {
        this.definitions = definitions;
    }

    public void setSamples(ArrayList<XWordInfo> samples) {
        this.samples = samples;
    }

    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    

    public void setSynonyms(ArrayList<XWordInfo> synonyms) {
        this.synonyms = synonyms;
    }

    public void setTranslations(ArrayList<XWordInfo> translations) {
        this.translations = translations;
    }

    public void setWord(XWordInfo word) {
        this.word = word;
    }

}
