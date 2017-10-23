package controller;

import javafx.beans.property.SimpleStringProperty;

public class Row{
	private final SimpleStringProperty plage;
	private final SimpleStringProperty  adresse;
	private final SimpleStringProperty  duree;
	private long id;
	
	public Row(String p, String a,String d){
		this.plage = new SimpleStringProperty(p);
		this.adresse = new SimpleStringProperty(a);
		this.duree = new SimpleStringProperty(d);
	}

	public String getPlage() {
		return plage.get();
	}
    public void setPlage(String p) {
        plage.set(p);
    }

	public String getAdresse() {
		return adresse.get();
	}
	  public void setAdresse(String a) {
	        adresse.set(a);
	    }


	public String getDuree() {
		return duree.get();
	}
	  public void setDuree(String d) {
	        duree.set(d);
	    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	
	
}