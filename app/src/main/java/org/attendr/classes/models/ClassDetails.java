package org.attendr.classes.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClassDetails extends RealmObject {

	@PrimaryKey
	private int id;

	private String classTitle;
	private String professor;
	private int percentage;

	public ClassDetails() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
