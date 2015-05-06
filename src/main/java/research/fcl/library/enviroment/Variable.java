package research.fcl.library.enviroment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import research.fcl.library.variables.BaseFunctionVariable;
import com.google.gson.annotations.Expose;

public class Variable implements Serializable{
	private static final long serialVersionUID = 2893037691069542080L;
	private static final Logger log = Logger.getLogger( Variable.class.getName() );
	private List<Observer> observers = new ArrayList<Observer> ();
	@Expose
	private String name = "";
	@Expose
	private boolean noRange=false;
	@Expose double min = Double.MAX_VALUE;
	@Expose double max = -Double.MAX_VALUE;
	public void calculateRange () {
		log.info ("TEMPORARY LOGGING : " + this.observers.size());
		this.observers.forEach (
			o -> {
				if (o instanceof BaseFunctionVariable) {
					this.min = Math.min (this.min, ((BaseFunctionVariable)o).getMin());
					this.max = Math.max (this.max, ((BaseFunctionVariable)o).getMax());
				}
				this.min = Math.min (this.min, this.getValue() );
				this.max = Math.max (this.max, this.getValue() );
			});
		if (this.min == Double.MAX_VALUE || this.max == -Double.MAX_VALUE) this.noRange = true;
		else this.noRange = false;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.notifyObservers(this);
	}

	private void notifyObservers(Variable variable) {
		observers.forEach(o -> {o.notify (this);});		
	}
	public void addObserver (Observer o) {
		this.observers.add(o);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		if (this.value != value) {
			this.value=value;
			this.notifyObservers(this);
		};
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

	public void forceCalc() {
		this.notifyObservers(this);
	}
}

