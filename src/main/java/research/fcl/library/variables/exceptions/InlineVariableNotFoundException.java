package research.fcl.library.variables.exceptions;

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
