package research.fcl.library.rules;

import research.fcl.library.rules.modifiers.FairlyModifier;
import research.fcl.library.rules.modifiers.ModifierFactory;
import research.fcl.library.rules.modifiers.NotModifier;
import research.fcl.library.rules.modifiers.VeryModifier;

public class DefaultModifierFactory extends ModifierFactory {
	public DefaultModifierFactory() {
		super();
		this.put("not", new NotModifier());
		this.put("very", new VeryModifier());
		this.put("fairly", new FairlyModifier());
	}
}	
