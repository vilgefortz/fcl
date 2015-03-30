package research.fcl.library.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import research.fcl.library.variables.BaseFunctionVariable;

import com.google.gson.annotations.Expose;

public class Rule implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8303989672422512125L;
	@Expose
	private String text;
	@Expose
	private String name;
	@Expose
	Cause cause;
	@Expose
	List<Effect> effect = new ArrayList<Effect> ();
	private List<BaseFunctionVariable> dependend = new ArrayList<BaseFunctionVariable>();
	private List<BaseFunctionVariable> affected = new ArrayList<BaseFunctionVariable>();
	private String id;
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Rule)obj).id);
	}
	public void setText(String text) {
		this.text = text;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Rule(String name) {
		this.name = name;
		Random r  = new Random();
		this.id = name + r.nextLong();
	}

	public Rule(String name, String text) {
		this(name);
		this.text = text;
	}

	public void addDepenedency(BaseFunctionVariable v) {
		if (!this.dependend.contains(new BaseFunctionVariable(v.getName())))
			this.dependend.add(v);
	}

	public void addAffected(BaseFunctionVariable v) {
		if (!this.affected.contains(new BaseFunctionVariable(v.getName())))
			this.affected.add(v);
	}

	public boolean affects(BaseFunctionVariable var) {
		return this.affected.contains(new BaseFunctionVariable(var.getName()));
	}
	public boolean dependsOn(BaseFunctionVariable var) {
		return this.dependend.contains(new BaseFunctionVariable(var.getName()));
	}

	public Cause getCause() {
		return cause;
	}

	public List<Effect> getEffects() {
		return effect;
	}

	public List<BaseFunctionVariable> getAffected() {
		return this.affected;
	}

	
}
