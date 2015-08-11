package research.fcl.library.rules.modifiers;


public class DefaultModifierFactory extends ModifierFactory {
	public DefaultModifierFactory() {
		super();
		this.put("not", new NotModifier());
		this.put("very", new VeryModifier());
		this.put("fairly", new FairlyModifier());
	}
}	
