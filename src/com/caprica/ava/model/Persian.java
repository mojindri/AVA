package com.caprica.ava.model;

public class Persian {
	private String word;
	private String meaning;
	private String synonym;

	public Persian(String word, String meaning ,String synonym) {
		this.word = word;
		this.meaning= meaning;
		this.synonym = synonym;
	}

	public String getMeaning() {
		return meaning;
	}

	public String getSynonym() {
		return synonym;
	}

	public String getWord() {
		return word;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
