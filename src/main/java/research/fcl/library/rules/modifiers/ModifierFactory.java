package research.fcl.library.rules.modifiers;

import java.util.HashMap;

public class ModifierFactory extends HashMap <String,Modifier>{

	public boolean isModifier(String word) {
		return get(word)!=null;
	}

}
