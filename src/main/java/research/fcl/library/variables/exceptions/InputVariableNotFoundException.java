package research.fcl.library.variables.exceptions;

public class InputVariableNotFoundException extends Exception {
	public InputVariableNotFoundException(String name) {
		this.name = name;
		}	
		protected String name;
		@Override
		public String getMessage() {
		
			return "Input variable not found : '" + name + "'";
		}
}
