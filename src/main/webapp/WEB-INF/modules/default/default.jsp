<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
#resizable {
	width: 100%;
	height: 500px;
	max-width: 100%;
	min-width: 100%;
	border: 3px solid black
}

.container {
	margin-bottom: 40px;
}

#resizable h3 {
	text-align: center;
	margin: 0;
}

#resizable {
	position: relative
}

#sidebar {
	background-color: grey;
}

#editor {
	position: absolute;
	top: 15px;
	left: 0;
	right: 0;
	bottom: 0;
}

input {
	color: #000000;
}
</style>
<div class="row">
	<div class="col-md-8 editor">
		<div id="resizable">
			<h3 class="ui-widget-header">FCL Editor</h3>
			<div id="editor">
				<jsp:include page='example.fcl' />
			</div>

		</div>
		<div id="varDiv"></div>
	</div>
	<div id="sidebar" class="col-md-4 sidebar">
		<div id="variables" class="well">zmienne</div>
		<div id="tree" class="well">
		</div>
	</div>
</div>
<div class="row">
	<div id="log" class="col-md-8"></div>
</div>

<link rel="stylesheet"
	href="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.css">
<link rel="stylesheet"
	href="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.theme.css">
<link rel="stylesheet"
	href="scripts/lib/dist/themes/default/style.min.css" />
<script src="scripts/lib/dist/jstree.min.js"></script>
<script src="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
<script src="scripts/lib/ace/src-noconflict/ace.js"
	type="text/javascript" charset="utf-8"></script>

<script>
	//	var mainData;
	var env;
	var refreshEnviromentData = function(callback) {
		$.post("App?action=getEnviroment", null, function(data) {
			env = $.parseJSON(data);
			refreshVariableDiv();
			if (callback)
				callback();
		});
	};
	var refreshVariableDiv = function() {
		$("#varDiv").text("");
		var text = "Variables :<br>";
		$
				.each(
						env,
						function(key, value) {
							text += value.name;
							text += "<input title='Wprowadź liczbę' pattern='-?[0-9]*\.?[0-9]+' class='variable-input' id='var-"
									+ key
									+ "' name='"
									+ value.name
									+ "' value='" + value.value + "'><br>";
						});
		var changingVariable;
		$("#varDiv").append(text);
		$(".variable-input").keyup(function() {
			if (changingVariable != null)
				return;
			changingVariable = this;
			window.setTimeout(function() {
				var id = $(changingVariable).attr('id');
				changingVariable = null;
				id = /-.*/.exec(id);
				id = ("" + id).substring(1);
				var val = $("#var-" + id).val();
				val = parseFloat(val);
				if (val === env[id].value)
					return;
				var name = env[id].name;
				$.post("App?action=setVariable", {
					"name" : name,
					"value" : val
				}, function(data) {
					if (data == "null") {
						reloadEditor();
					} else {
						refreshVariables();
					}
				});

			}, 1500);
		});
	};
	var refreshVariables = function() {
		refreshEnviromentData(function() {
			$.each(env, function(key, value) {
				if (value.value !== $("#var-" + key).val())
					$("#var-" + key).val(value.value);
			});
		});
	};

	var refreshTree = function() {
		$.post("App?action=getTreeData", null, function(data) {
			$('#tree').jstree("destroy");
			var treeData = $.parseJSON(data);
			$('#tree').jstree({ 'core' : {
			    'data' : treeData
			       } });
			$('#tree').jstree().redraw(true);
		});
	}

	var refreshIDE = function() {
		refreshEnviromentData();
		refreshTree();
		getErrors();
	}

	var reloadEditor = function() {
		$.post("Gateway", {
			data : editor.getSession().getValue()
		}, function(value) {
			refreshIDE();
			editNotification = false;
		});
	};

	var errors = null;
	var errorMsg = null;
	function getErrors(callback) {
		$.post("App?action=getErrorLog", null, function(data) {
			errors = $.parseJSON(data);
			highlightError();
			if (callback)
				callback();
		});
	}
	function highlightError() {
		errorMsg = errors[0];
		if (errorMsg == undefined || errorMsg == null)
			errorMsg = {
				line : -1,
				entry : "",
				linepos : -1
			};
		editor.getSession().setAnnotations([ {
			row : (errorMsg.line - 1),
			column : errorMsg.linepos,
			text : errorMsg.entry,
			type : "error" // also warning and information
		} ]);
	};
	var editNotification = false;
	var editor = null;
	$(document).ready(function() {
		editNotification = false;
		editor = ace.edit("editor");
		editor.setTheme("ace/theme/monokai");
		editor.getSession().on('change', function() {
			if (!editNotification) {
				editNotification = true;
				window.setTimeout(function() {
					reloadEditor();
				}, 1000);
			}
		});
		$("#resizable").resizable({
			resize : function(event, ui) {
				editor.resize();
			}
		});
		reloadEditor();
	});
</script>

