<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
#editor {
	height: 400px;
	position: relative;
}

#log {
	position:relative;	
}
input {
	color:#000000;
}
</style>

<div class="row">
	<div class="col-md-8 editor">
		<div id="fatal"><br></div>
		<div id="editor">		
/* aplikacja 
do 
testów*/
FUNCTION_BLOCK Fuzzy_FB
	VAR_INPUT
		temp: REAL;
		pressure : REAL;
	END_VAR
	VAR_OUTPUT
		test :real;
		valve : REAL;
	END_VAR 
	FUZZIFY temp 
		TERM cold := (3, 1) (27, 0);
		TERM hot := (3, 0) (27, 1);
	END_FUZZIFY 
	FUZZIFY pressure
		TERM low := (55, 1) (95, 0);
		TERM high:= (55, 0) (95, 1);
	END_FUZZIFY
	DEFUZZIFY valve
		TERM drainage := (0,1) (3,0);
		TERM closed := (0,0) (3,1) (5,0); 
		TERM inlet := (3,0) (5,1);
		ACCU : MAX;
		METHOD : COG; 
		DEFAULT := -.2;
	END_DEFUZZIFY 
	DEFUZZIFY test
		TERM drainage := (0,1) (3,0);
		TERM closed := (0,0) (3,1) (5,0); 
		TERM inlet := (3,0) (5,1);ACCU : MAX;
		METHOD : COG;  
		DEFAULT := -.2; 
	END_DEFUZZIFY 

	RULEBLOCK No1  
		AND : MIN;  
		RULE 1 : IF temp IS cold AND pressure IS low THEN valve IS inlet;
		RULE 2 : IF temp IS cold AND pressure IS high THEN valve IS closed WITH 0.8;
		RULE 3 : IF pressure IS low THEN test IS closed;
		RULE 4 : IF temp IS hot AND pressure IS high THEN valve IS drainage;
	END_RULEBLOCK                
END_FUNCTION_BLOCK 
</div>
<div id="varDiv"></div>
	</div>
	<div id="sidebar" class="col=md-4 sidebar">
		<pre id="code">
			
		</pre>
	</div>
</div>
<div class="row">
	<div id="log" class="col-md-8"></div>
</div>

<script src="scripts/lib/ace/src-noconflict/ace.js"
	type="text/javascript" charset="utf-8"></script>
<script>
	
	var mainData;
	var env;
	var refreshVariableInput = function () {
		var env = 
		$("#varDiv").text("");
		var text = "Variables :<br>";
		$.each (env, function(key,value) {
			text += value.name;
			text += "<input title='Wprowadź liczbę' pattern='-?[0-9]*\.?[0-9]+' class='variable-input' id='var-"+key+"' name='"+ value.name +"' value='"+value.value+"'><button class='change-variable' id='varbutton-"+key+"'>Zmień</button><br>";
		});
		var changingVariable;
		$("#varDiv").append(text);

	$(".variable-input").keyup(function() {
			if (changingVariable!=null) return;
			changingVariable = this;
			window.setTimeout(function() {
				var id = $(changingVariable).attr('id');
				changingVariable=null;
				id = /-.*/.exec(id);
				id = ("" + id).substring(1);
				var val = $("#var-" + id).val();
				val = parseFloat(val);
				if (val === env[id].value)
					return;
				var name = env[id].name;
				$.post("Gateway?action=setVariable", {
					"name" : name,
					"value" : val
				}, function(data) {
					if (data=="null") {
						reloadEditor();
					}
					var app = $.parseJSON(data);
					mainData = app;
					var env = app.enviroment;
					$.each(env, function(key, value) {
						if (value.value !== $("#var-" + key).val())
							$("#var-" + key).val(value.value);
					});
				});
			}, 1500);
		});
	};
	
	var refreshIDE = function () {
		refreshVariableInput();	
		highlightError();
	}
	
	var reloadEditor = function () {
		$.post("Gateway", {
			data : editor.getSession().getValue()
		}, function(value) {
			refreshIDE();
			editNotification = false;
		});
	};
	
	var editNotification = false;
	var editor = ace.edit("editor");
	editor.setTheme("ace/theme/monokai");
	editor.getSession().on(
			'change',
			function() {
				if (!editNotification) {
					editNotification = true;
					window.setTimeout(function() {
						reloadEditor();
						window.setTimeout (function () {
						highlightError();
						},200);
						}, 1000);
				}
			});
	var errorMsg=null;

	function highlightError(errorMsg, line){
		 errorMsg = mainData.logger.fatal[0];
		/*$.each(app.logger.fatal, function(key, value) {
			
			$("#fatal").append(
					value.entry + ", at line : "
							+ value.line + " cursor pos : "
							+ value.linepos + "<br>");
		});*/
		if (errorMsg==undefined || errorMsg==null ) errorMsg = {
			line:-1,
			entry:"",
			linepos:-1
		};
		editor.getSession().setAnnotations([{
		    row: (errorMsg.line - 1),
		    column: errorMsg.linepos,
		    text: errorMsg.entry,
		    type: "error" // also warning and information
		}]);
	};
</script>

