package research.fcl.library.rules;

import research.fcl.library.rules.modifiers.ModifierFactory;


public abstract class RuleFactory {
	private Ruleblock rb;
	public RuleFactory (Ruleblock rb) {
		this.rb=rb;
	}
	public abstract Rule fromString (String name, String descr) throws Exception;
	public DefaultActionFactory getActionFactory () {
		return this.rb.getFunctionBlock().getApp().getActionFactory(this.rb);
	}
	public ModifierFactory getModfierFactory () {
		return this.rb.getFunctionBlock().getApp().getModifierFactory();
	}
	public DefaultEffectFactory getEffectFactory() {
		return this.rb.getFunctionBlock().getApp().getEffectFactory(this.rb);
	}
	
}
