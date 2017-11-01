package org.attendr.classes.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClassDetails extends RealmObject {

	@PrimaryKey
	private int id;

	public ClassDetails() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
