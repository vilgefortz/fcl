package main.application.enviroment;

import java.util.Observable;
import java.util.logging.Logger;

import com.google.gson.annotations.Expose;

public class Variable extends Observable{
	private static final Logger log = Logger.getLogger( Variable.class.getName() );
	@Expose
	private String name = "";
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.notifyObservers(this);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		this.notifyObservers(this);
	}
	@Expose
	private double value = 0;
	
	@Override
	public boolean equals(Object obj) {
		return this.name.equalsIgnoreCase(((Variable)obj).name);
	}
	
	public Variable (String name) {
		log.fine((new StringBuilder("Creating variable ")).append(name).append(".").toString());
		this.name=name;
	}
	public Variable (String name, double value) {
		log.fine((new StringBuilder("Creating variable ")).append(name).append(" with value ").append(this.value).toString());
		this.name = name;
		this.value= value;
	}
}

