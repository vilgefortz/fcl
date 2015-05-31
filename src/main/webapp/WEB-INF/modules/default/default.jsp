<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<link rel="stylesheet" href="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.css">
	<link rel="stylesheet" href="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.theme.css">
	<link rel="stylesheet" href="scripts/lib/dist/themes/default/style.min.css" />
	<script src="scripts/lib/dist/jstree.min.js"></script>
	<script src="scripts/lib/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
	<script src="scripts/lib/ace/src-noconflict/ace.js"	type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="css/windowmanager/fontello.css" />
	<link rel="stylesheet" href="css/windowmanager/windowsmanager.css" />
	<script src="scripts/lib	/windowmanager/windowmanager.js" type="text/javascript"></script>
	<script language="javascript" type="text/javascript" src="scripts/lib/flot/jquery.flot.js"></script>
	<script language="javascript" type="text/javascript" src="scripts/lib/flot/jquery.flot.resize.js"></script>
	
<style>
.context-menu-item:hover {
	cursor:hand;
	cursor:pointer;
}
.context-menu {
	margin-left:40px;
	position: absolute;
	opacity:0,9;
	background-color:white;
	border: medium solid;
	padding: 10px;
	display: block;
	z-index: 3;
}

.term-tab {
    background-color: white;
    border-bottom: medium solid;
    opacity: 0.9;
    position: absolute;
    top:0px;
    left:0px;
    padding: 5px 15px 0px 0px;
    width: 100%;
    z-index: 2;
}
.term-tab > span {
	margin: 2px 3px 2px 10px;
	display:block;
}

.set-terms {
	margin : 5px;
}
.show-terms {
	background-color:red;
}
#resizable {
	width: 100%;
	height: 500px;
	max-width: 100%;
	min-width: 100%;
	border: 3px solid black
}
.var-management {
	margin-right:4px;
}
.mng-refresh-var {
	padding: 2px 4px 2px 3px;
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
	top: 30px;
	left: 0;
	right: 0;
	bottom: 0;
}

input {
	color: #000000;
}
.var-send-slider {
	margin:3px auto; 
	width:94%;
}

/*
.var-send-div {
	width:70%
}*/

.var-send-input {
	width:37%;
	margin-right:3%;
}
.var-input-label {
	margin-left:3%;
	width:57%;
}
</style>
<div class="row">
	<div class="col-md-8 editor">
		<div id="toolbox" class="toolbox">
			<button class='treeWindowButton'>TREE</button>
			<button class='varWindowButton'>VAR</button>
			<button class='chartWindowButton'>CHART</button>
		</div>
		<div id="resizable">
			<h3 class="ui-widget-header">FCL Editor</h3>
			<div id="editor">
				<jsp:include page='example.fcl' />
			</div>

		</div>
	</div>
	<div id="sidebar" class="col-md-4 sidebar">
		<div id="window-rail"></div>
	</div>
</div>


<script>
//default chart options
var options = {
			lines: {
				show: true
			},
			points: {
				show: false
			},
			xaxis: {
				tickDecimals: 0,
				tickSize: 1
			}
		};
	//	var mainData;
	//~ var env;
	//~ var refreshEnviromentData = function(callback) {
		//~ $.post("App?action=getEnviroment", null, function(data) {
			//~ env = $.parseJSON(data);
			//~ refreshVariableDiv();
			//~ if (callback)
				//~ callback();
		//~ });
	//~ };
	//~ var refreshVariableDiv = function() {
		//~ $("#varDiv").text("");
		//~ var text = "Variables :<br>";
		//~ $
				//~ .each(
						//~ env,
						//~ function(key, value) {
							//~ text += value.name;
							//~ text += "<input title='Wprowadź liczbę' pattern='-?[0-9]*\.?[0-9]+' class='variable-input' id='var-"
									//~ + key
									//~ + "' name='"
									//~ + value.name
									//~ + "' value='" + value.value + "'><br>";
						//~ });
		//~ var changingVariable;
		//~ $("#varDiv").append(text);
		//~ $(".variable-input").keyup(function() {
			//~ if (changingVariable != null)
				//~ return;
			//~ changingVariable = this;
			//~ window.setTimeout(function() {
				//~ var id = $(changingVariable).attr('id');
				//~ changingVariable = null;
				//~ id = /-.*/.exec(id);
				//~ id = ("" + id).substring(1);
				//~ var val = $("#var-" + id).val();
				//~ val = parseFloat(val);
				//~ if (val === env[id].value)
					//~ return;
				//~ var name = env[id].name;
				//~ $.post("App?action=setVariable", {
					//~ "name" : name,
					//~ "value" : val
				//~ }, function(data) {
					//~ if (data == "null") {
						//~ reloadEditor();
					//~ } else {
						//~ refreshVariables();
					//~ }
				//~ });
//~ 
			//~ }, 1500);
		//~ });
	//~ };
	//~ var refreshVariables = function() {
		//~ refreshEnviromentData(function() {
			//~ $.each(env, function(key, value) {
				//~ if (value.value !== $("#var-" + key).val())
					//~ $("#var-" + key).val(value.value);
			//~ });
		//~ });
	//~ };

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

	//~ var refreshIDE = function() {
		//~ refreshEnviromentData();
	//~ //	refreshTree();
		//~ getErrors();
	//~ }

	var reloadEditor = function() {
		$.post("Gateway", {
			data : editor.getSession().getValue()
		}, function(value) {
			if (editor.registeredListeners.length > 0) 
				$.each (editor.registeredListeners, function (key, obj) {
					obj.refresh();
				});
			editNotification = false;
			getErrors (null);
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
	var edited = false;
	var editor = null;
	var previousRefreshEventx
	var lastRefreshEvent
	var Refresher = function () {
		this.valid = true;
	}
	
	
	
	$(document).ready(function() {
		editNotification = false;
		editor = ace.edit("editor");
		editor.setTheme("ace/theme/monokai");
		editor.getSession().on('change', function() {
			edited = false;
			if (editNotification) edited = true;
			if (!editNotification) {
				if (editor.timeoutId) window.clearTimeout(editor.timeoutId);
				editor.timeoutId = window.setTimeout( function () { reloadEditor(); } , 2000);
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
			fun.index = index;
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
				if (this.variables[i].name == this.env[j].name) {
					varExists = true;
					break;
				}
			}
			if (!varExists) {
				this.variables[i].element.remove();
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
		this.noRange = value.noRange || (this.max==this.min);
		this.lock = false // lock this value changes when sliding
		this.hasChanged = true;
		this.lockReload = false; //this value is setup true to notify that there is change about to be done
		this.lockReloadDelay = 200;
	}
	
	Variable.prototype.updateValue = function (value) {
		if (value.value != this.value || value.max !=this.max || value.min!=this.min || value.name!= this.name) {
			this.hasChanged = true;
			this.name = value.name;
			this.min = value.min;
			this.max = value.max;
			this.noRange = value.noRange || (this.max==this.min);
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
		
		this.element.append ('</div><label class="var-input-label" for="var-input-' + this.name + '">'+this.name+'</label><input name="var-input-'+ this.name+'" class="var-send-input" value="'
							+this.value+'" name="'+this.name+'" /><div class="var-send-slider">');
		this.input = this.element.find ('.var-send-input').first();
		this.label = this.element.find ('.var-input-label').first();
		this.slider = this.element.find ('.var-send-slider').first();
		if (!this.noRange) {
			this.slider.slider ({
				range: this.name,
				value: this.value,
				step: Math.abs(this.max-this.min)/1000,
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
		}
		if (this.noRange) {
			this.label.append("<div class='window-management var-management'>"+
			"<div class='mng-refresh-var mng-button'>&#xe805;</div>"+
			"<div class='mng-remove-var mng-button'>&#xe804;</div>"+
			"</div>");
			this.label.find('.mng-reresh-var').first().click( function () {
				self.parent.reload();
			});
			this.label.find('.mng-remove-var').first().click( function () {
				$.post ('App?action=remove-var', { name : self.name }, function () {
					self.parent.reload();
				});
			})
		}
		this.input.change(function () {
			self.lock = false;
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
					var varWindow = new VariableWindow (wnd.content);
					this.varWindow = varWindow;
					wnd.content.css('margin','0px 10px, 0px, 10px');
					editor.registerListener (this);
				}
			}));
		};
		//addVariableWindow();
		$(".varWindowButton").click (addVariableWindow);
	});
	
	var ContextMenu = function (options,element) {	this.element = element;
		this.options = options;
	};
	
	
	ContextMenu.prototype.open = function () {
		var self = this;
		this.menu = $("<div class='context-menu' style='display:none'></div>");
		$(this.element).parent().prepend (this.menu);
		this.menu.show('fast');
		$(document).on ('mouseup', function (e) {
			if (!self.menu.is(e.target) && self.menu.has(e.target).length === 0) {
				self.close();
			}
		});
		$.each (self.options.items, function (key,value) {
			self.renderMenuOption (value);
		});
	}
	
	ContextMenu.prototype.renderMenuOption = function (option) {
		var self = this;
		var item = $("<div class='context-menu-item'>"+option.label+"</div>");
		self.menu.append(item);
		if (option.click) $(item).click(function () {
			option.click (self.options.params);
		});
		$(item).click(function () {
			self.close();	
		});
	}
	
	ContextMenu.prototype.close = function () {
		this.menu.hide('fast');
		var self = this;
		window.setTimeout( function () {
			self.menu.remove();
		}, 200);
		$("document").off('mouseup');
	}
	
	var addChartWindow = function (fb,variable) {
			windowsManager.add (new Window ({
				editor : editor,
				close:true,	
				closeIcon:'&#xe804;',
				closing : function (wnd) {
					chartVisible=false;
				},
				toggle:true,
				toggleUpIcon:'&#xe801;',
				toggleDownIcon:'&#xe800;',
				moveable:true,
				moveUpIcon:'&#xe803;',
				moveDownIcon:'&#xe802;',
				resizable:true,
				title:'Variable : ' + variable,
				refresh : function () {
					this.chartWindow.reload ();
				},
				init : function (wnd) {
					this.wnd=wnd;
					var chartWindow = new ChartWindow (wnd,variable,fb);
					this.chartWindow = chartWindow;
					wnd.content.css('margin','0px 10px, 0px, 10px');
					editor.registerListener (this);
				}
			}));
		};
	//chart mwah
	var chartVisible = false;
	$(document).ready (function () {
		//addVariableWindow();
		$(".chartWindowButton").click (addChartWindow);
	});
	var ChartWindow = function (wnd, variable,fb) {
		this.variable = variable
		this.fb=fb;
		var self = this;
		this.wnd = wnd;		
		this.terms = Array ();
		this.resizeMark = false;
		$.post ('App?action=getTerms', {variable:variable,fb:fb}, function (data) {
			if (data == 'false') return;
			data = $.parseJSON (data);
			$.each(data, function (key, term) {
				self.terms.push (term.name);
			});
			self.enabledTerms = self.terms;
			self.tab = $("<div class='term-tab' style='display:none'></div>")
			wnd.resizable.prepend (self.tab);
			self.tab.append('<span><h5>Select terms to show on chart</h5></span>');
			$.each (self.terms, function (key, term) {
				var checked = self.enabledTerms.indexOf(term)>=0 ? "checked=''" : "";
				self.tab.append("<span><input type='checkbox' " + checked + " name='" + term + "' />" + term + "<br /></span>");				
			});
			self.tab.append ("<span><button class='set-terms set-terms-show'>Show</button><button class='set-terms set-terms-cancel'>Cancel</button></span>");
	
			self.addTabListeners ();
			wnd.navigation.prepend("<div class='mng-button show-terms'>&#xe800</div>");
			wnd.navigation.find('.show-terms').first().click (function () {
				self.tab.show('fast');
			});
			//getting chart data
			$.post (self.generateUrl (fb,variable,self.enabledTerms), null, function (data) {
				wnd.content.css ("height",150);
				self.data = $.parseJSON(data);
				$.plot(wnd.content, self.data.terms, options);
				wnd.resizable.resize(function(){
					self.resize();
				});
			});
		});
	};
	ChartWindow.prototype.generateUrl = function (fb,varname,terms) {
		var url = "App?action=getTermsData&fb="+fb+"&var="+varname;
		$.each(terms, function (key, term) {
			url += "&term=" + term;
		});
		return url;
	};
	ChartWindow.prototype.loadTerms  = function () {
		var self = this;
		$.post ('App?action=getTerms', {variable:this.variable,fb:this.fb}, function (data) {
			if (data == 'false') return;
			data = $.parseJSON (data);
			self.terms = [];
			$.each(data, function (key, term) {
				self.terms.push (term.name);
			});
			self.tab.empty();
			self.tab.append('<span><h5>Select terms to show on chart</h5></span>');
			$.each (self.terms, function (key, term) {
				var checked = self.enabledTerms.indexOf(term)>=0 ? "checked=''" : "";
				self.tab.append("<span><input type='checkbox' " + checked + " name='" + term + "' />" + term + "<br /></span>");				
			});
			self.tab.append ("<span><button class='set-terms set-terms-show'>Show</button><button class='set-terms set-terms-cancel'>Cancel</button></span>");
			self.addTabListeners ();
		});
	};	
	
	ChartWindow.prototype.addTabListeners = function () {
		var self = this;
		var show = self.tab.find('.set-terms-show').first();
		var cancel = self.tab.find('.set-terms-cancel').first();
		show.click (function (e) {
			self.enabledTerms = [];
			self.tab.find(':checked').each (function (key, element) {
				self.enabledTerms.push ($(element).attr('name'));
			});
			self.tab.hide('fast');
			self.reload ();
		});
		cancel.click (function (e) {
			self.tab.hide('fast');
		});
	};
	
	ChartWindow.prototype.reload = function () {
		var self = this;
		$.post (this.generateUrl (this.fb,this.variable,this.enabledTerms), null, function (data) {
			self.data = $.parseJSON(data);
			if (self.data.error != "") {
				self.wnd.content.html(self.data.error);
			} else 
			self.wnd.content.empty(); //html('');
			$.plot(self.wnd.content, self.data.terms, options);
		});
		self.loadTerms();
	}
	
	ChartWindow.prototype.resize = function () {
		var self = this;
		if (self.resizeMark) return;
		window.setTimeout (function () {
			self.resizeMark = false;
			self.wnd.content.css ("height",self.wnd.resizable.css("height"));
			if (self.data.error == "") $.plot(self.wnd.content, self.data.terms, options);
		},30);
		self.resizeMark = true;
	}
	
	//TREE WINDOW requires global variables editor (ACE) 
	//to enable requires DOM element with class treeWindowButton
	var treeVisible = false;
	$(document).ready (function () {
		$(".treeWindowButton").click (function () {
			if (treeVisible) return;	//if window is already shown return without showing it	 again 
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
				showVariableContextMenu : function (fb,varname,element) {
					var options = {
						title : varname + " menu :",
						params : {
							fb : fb,
							varname : varname,
							element : element,
						},
						items : [
							{ 
								label : "<a class='show-chart-item' onclick='return false;'>Show chart</a>",
								click : function (params) { 
									addChartWindow (params.fb, params.varname);
								}
							}
						]
					};
					var cm = new ContextMenu (options,element);
					cm.open();
				},
				
				//setupTermListeners: function () {
					//var self = this;
					//$(".term_node").off('click').on('click', function () {
						//var variable
					//});
					//$(".input-var").off('click').on('click', function (e) {
						////e.stopPropagation();
						
					//});
					//$(".inline-var").off('click').on('click', function (e) {
////						e.stopPropagation();
						//self.showVariableContextMenu ($(this).attr('data-function_block'),$(this).attr('name'),this);
					//});
					//$(".output-var").off('click').on('click', function (e) {

						//self.showVariableContextMenu ($(this).attr('data-function_block'),$(this).attr('name'),this);
					//});
				//},
				title:'PROJECT TREE',
				refresh : function () {
					var self = this;
					wnd=this.wnd;
					$.post("App?action=getTreeData", null, function(data) {
						var tree = wnd.content;
						tree.jstree("destroy");
						tree.bind ('select_node.jstree', function (node,selected) {
							var id = selected.node.id;
							var anchorId= id + "_anchor";
							var nodeElement = $("#" + anchorId);
							if (nodeElement.hasClass('var-node')) {
								self.showVariableContextMenu (nodeElement.attr('data-function_block'),nodeElement.attr('name'),nodeElement);
							}
						});
						tree.bind("loaded.jstree", function (event, data) {
							tree.jstree("open_all");
							tree.jstree().redraw(true);
					//		self.setupTermListeners()
						});
						var treeData = $.parseJSON(data);		
						tree.jstree( { 
							'core' : {
								'data' : treeData
							} 
						});
						//0elf.setupTermListeners ();
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
	//~ 
	var windowContainer = $("#window-rail");
    var windowsManager = new WindowsManager (windowContainer);
       
      
	//~ $("#addWindow").click (function () {
		//~ var wnd = new Window ({
			//~ close:true,
			//~ closeIcon:'&#xe804;',
			//~ toggle:true,
			//~ toggleUpIcon:'&#xe801;',
			//~ toggleDownIcon:'&#xe800;',
			//~ moveable:true,
			//~ moveUpIcon:'&#xe803;',
			
			//~ moveDownIcon:'&#xe802;',
			//~ resizable:true,
			//~ title:i++,
			//~ refresh : function (wnd) {
				//~ 
			//~ }
		//~ });
		//~ windowsManager.add(wnd);
		//~ wnd.content.append ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a urna tortor. Cras sagittis ut risus vitae gravida. Aenean eu sem velit. Mauris rutrum, dui vitae pellentesque aliquam, arcu massa bibendum justo, consequat sagittis lectus ex luctus lacus. Sed at ipsum lectus. Integer aliquet sit amet dui at aliquet. Nam arcu orci, sodales id nunc non, aliquam congue nisi. Donec laoreet quam est, in congue mass");
	//~ });

</script>

