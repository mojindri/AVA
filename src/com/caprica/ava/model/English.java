package com.caprica.ava.model;

public class English {
	private String word;
	private String meaning;
	public English(String word, String meaning) {
	 
		this.meaning = meaning;
		this.word = word;
	}
	public String getMeaning() {
		return meaning;
	}
	public String getWord() {
		return word;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
}
