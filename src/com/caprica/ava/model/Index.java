package com.caprica.ava.model;

public class Index {
	private String id;
	private String word;
	private String size;

	public Index(String word, String size) {
		this.word = word;
		this.size = size;
	}

	public Index() {
	}

	@Override
	public boolean equals(Object o) {

		return this.word.equals(o.toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
