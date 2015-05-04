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
.window-header {
	border-width: 1px;
    height: 22px;
    border-bottom: solid;
}
.window-resizable {
	padding-top:3px
}
.container {
	margin-bottom: 40px;
}

#resizable h3 {
	text-align: center;
	margin: 0;
	margin: 0;
}

#resizable {
	position: relative
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

.var-send-div {
	width:70%
}

.var-send-input {
	width:30%
}
</style>
<div class="row">
	<div class="col-md-8 editor">
		<div id="resizable">
			<!--3 class="ui-widget-header">FCL Editor</h3-->
			<div id="editor">
				<jsp:include page='example.fcl' />
			</div>

		</div>
		<div id="varDiv"></div>
		<button id="addWindow">add</button>
		<button class="treeWindowButton">add Tree</button>
		<button class="varWindowButton">add Var</button>
	</div>
	<div id="sidebar" class="col-md-4 sidebar">
		<div id="window-rail"></div>
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
	<link rel="stylesheet" href="css/windowmanager/fontello.css" />
	<link rel="stylesheet" href="css/windowmanager/windowsmanager.css" />
	<script src="scripts/lib/windowmanager/windowmanager.js" type="text/javascript"></script>
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

	//~ var refreshTree = function() {
		//~ $.post("App?action=getTreeData", null, function(data) {
			//~ var tree = $("#tree");
			//~ tree.jstree("destroy");
			//~ tree.bind("loaded.jstree", function (event, data) {
		        //~ tree.jstree("open_all");
		    //~ });
			//~ var treeData = $.parseJSON(data);
			//~ tree.jstree({ 'core' : {
			    //~ 'data' : treeData
			       //~ } });
			//~ tree.jstree().redraw(true);
		//~ });
	//~ }

	var refreshIDE = function() {
		refreshEnviromentData();
	//	refreshTree();
		getErrors();
	}

	var reloadEditor = function() {
		$.post("Gateway", {
			data : editor.getSession().getValue()
		}, function(value) {
			if (editor.registeredListeners.length > 0) 
				$.each (editor.registeredListeners, function (key, obj) {
					obj.refresh();
				});
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
	}
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
		$("#resizable").resizable	({
			resize : function(event, ui) {
				editor.resize();
			}
		});
		editor.registeredListeners = [];
		editor.removeListener = function (fun) {
			if (fun.index) this.registeredListeners.splice (fun.index,1);	
		}
		editor.registerListener = function (fun) {
			var index = this.registeredListeners.push(fun);
			fun.index = index
		}
		reloadEditor();
	});
	//VARIABLE WINDOW
	//requires editor
	
	var VariableWindow = function (element) {
		this.element = element;
		this.init ();
	};
	VariableWindow.prototype.init = function () {
		this.variables = [];
		this.reload ();
	}
	VariableWindow.prototype.reload = function () {
		var self = this;
		$.post ('App?action=getEnviroment',null,function (data) {
			if (!data) return;
			self.env = $.parseJSON(data);
			self.redraw ();
		});
	};
	
	VariableWindow.prototype.redraw = function () {
		var self = this;
		var previous = false;
		$.each (this.env, function (key, value) {
			var v = self.getVariable (value.name);
			if (!v) {
				v = new Variable (value,self);
				if (previous) {
					previous.element.after('<div class="var-input var-' + value.name +'"></div>');
					v.element = self.element.find ('.var-' + value.name).first();
					v.updateHtml ();
				}
				else {
					self.element.append('<div class="var-input var-' + value.name +'"></div>');
					v.element = self.element.find ('.var-' + value.name).first();
					v.updateHtml ();
				}
				self.variables.push(v);
			}
			else {
				previous = v;
				v.updateValue(value)
				v.updateHtml ();
			};
		});
		self.clearUnused();
	};
	VariableWindow.prototype.clearUnused = function () {
		for (var i=0; i<this.variables.length; i++) {
			var varExists = false;
			for (var j=0; j<this.env.length; j++) {
				if (this.variables[i].name == this.env[i].name) {
					varExists = true;
					break;
				}
			}
			if (!varExists) {
				this.variables.splice (i,1);
			}
		}
	}
	VariableWindow.prototype.setEnviroment = function (env) { 
		this.env = env;
	}
	VariableWindow.prototype.getVariable = function (name) {
		var self = this;
		for (var i=0; i<self.variables.length; i++) {
			if (self.variables[i].name == name) return self.variables[i];
		}
		return false;
	}
	var Variable = function (value,parent) {
		this.parent = parent;
		this.name = value.name;
		this.value = value.value;
		this.min = value.min;
		this.max = value.max;
		this.lock = false // lock this value changes when sliding
		this.hasChanged = true;
		this.lockReload = false; //this value is setup true to notify that there is change about to be done
		this.lockReloadDelay = 200;
	}
	
	Variable.prototype.updateValue = function (value) {
		this.name = value.name;
		this.min = value.min;
		this.max = value.max;
		if (value.value != this.value) {
			this.hasChanged = true;
			this.value = value.value;
		}
	}
	
	Variable.prototype.setValueAndReload = function (value) {
		var self = this;
		self.tempValue=value;
		if (self.lockReload) return;
		self.lockReload = true;
		setTimeout (function () {
		self.lockReload = false;
		$.post("App?action=setVariable", {
			"name" : self.name,
			"value" : self.tempValue,
		}, function(data) {
		if (data == "null") {
						//reloadEditor();
		} else {
			self.parent.setEnviroment ($.parseJSON (data));
			self.parent.redraw();
			}
		});
		}, self.lockReloadDelay);
		
	}
	Variable.prototype.updateHtml = function () {
		var self = this;
		if (!this.hasChanged) return;
		if (this.lock) return;
		this.hasChanged=false;
		this.element.text ('');
		this.element.append ('<div class="var-send-slider"></div><input class="var-send-input" value="'+this.value+'" name="'+this.name+'" />');
		this.input = this.element.find ('.var-send-input').first();
		this.slider = this.element.find ('.var-send-slider').first();
		this.slider.slider ({
			range: this.name,
			value: this.value,
			step: (this.max-this.min)/1000,
			min: this.min,
			max: this.max,
			change : function (event, ui) {
				self.lock=false;
				self.input.val(ui.value);
				self.setValueAndReload (ui.value);
			},
			slide: function(event, ui) {
				self.lock = true;
				self.input.val(ui.value);
				self.setValueAndReload (ui.value);
			}
		}); 
		this.input.change(function () {
			self.lock = false;
			self.input.val(ui.value);
			self.setValueAndReload (self.input.val());
		});
	}
	
	var varVisible = false;
	$(document).ready (function () {
		var addVariableWindow = function () {
			if (varVisible) return;	//if window is already shown return without showing it again 
			varVisible = true;
			windowsManager.add (new Window ({
				editor : editor,
				close:true,	
				closeIcon:'&#xe804;',
				closing : function (wnd) {
					varVisible=false;
				},
				toggle:true,
				toggleUpIcon:'&#xe801;',
				toggleDownIcon:'&#xe800;',
				moveable:true,
				moveUpIcon:'&#xe803;',
				moveDownIcon:'&#xe802;',
				resizable:true,
				title:'ENVIROMENT',
				refresh : function () {
					this.varWindow.reload ();
				},
				init : function (wnd) {
					this.wnd=wnd;
					var varWindow = new VariableWindow (wnd.element);
					this.varWindow = varWindow;
					editor.registerListener (this);
				}
			}));
		};
		//addVariableWindow();
		$(".varWindowButton").click (addVariableWindow);
	});
	
	
	
	//TREE WINDOW requires global variables editor (ACE) 
	//to enable requires DOM element with class treeWindowButton
	var treeVisible = false;
	$(document).ready (function () {
		$(".treeWindowButton").click (function () {
			if (treeVisible) return;	//if window is already shown return without showing it again 
			treeVisible = true;
			windowsManager.add (new Window ({
				editor : editor,
				close:true,
				closeIcon:'&#xe804;',
				closing : function (wnd) {
					treeVisible=false;
				},
				toggle:true,
				toggleUpIcon:'&#xe801;',
				toggleDownIcon:'&#xe800;',
				moveable:true,
				moveUpIcon:'&#xe803;',
				moveDownIcon:'&#xe802;',
				resizable:true,
				title:'PROJECT TREE',
				refresh : function () {
					wnd=this.wnd;
					$.post("App?action=getTreeData", null, function(data) {
						var tree = wnd.content;
						tree.jstree("destroy");
						tree.bind("loaded.jstree", function (event, data) {
							tree.jstree("open_all");
							tree.jstree().redraw(true);
						});
						var treeData = $.parseJSON(data);		
						tree.jstree( { 
							'core' : {
								'data' : treeData
							} 
						});
					});	
				},	
				init : function (wnd) {
					this.wnd=wnd;
					editor.registerListener (this);
					wnd.setup.refresh (wnd);
				}
			}));
		});
	});
	
	//TESTING WINDOWS
	
	var windowContainer = $("#window-rail");
        var windowsManager = new WindowsManager (windowContainer);
        var i=0;
      
	$("#addWindow").click (function () {
		var wnd = new Window ({
			close:true,
			closeIcon:'&#xe804;',
			toggle:true,
			toggleUpIcon:'&#xe801;',
			toggleDownIcon:'&#xe800;',
			moveable:true,
			moveUpIcon:'&#xe803;',
			moveDownIcon:'&#xe802;',
			resizable:true,
			title:i++,
			refresh : function (wnd) {
				
			}
		});
		windowsManager.add(wnd);
		wnd.content.append ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a urna tortor. Cras sagittis ut risus vitae gravida. Aenean eu sem velit. Mauris rutrum, dui vitae pellentesque aliquam, arcu massa bibendum justo, consequat sagittis lectus ex luctus lacus. Sed at ipsum lectus. Integer aliquet sit amet dui at aliquet. Nam arcu orci, sodales id nunc non, aliquam congue nisi. Donec laoreet quam est, in congue mass");
	});

</script>

