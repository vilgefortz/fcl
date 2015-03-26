package research.fcl.library.variables;

public class InlineVariableNotFoundException extends Exception {
	public InlineVariableNotFoundException(String name) {
		this.name = name;
		}	
		protected String name;
		@Override
		public String getMessage() {
		
			return "Inline variable not found : '" + name + "'";
		}
}
