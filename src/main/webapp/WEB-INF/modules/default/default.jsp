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
	<script language="javascript" type="text/javascript" src="sc 	ripts/lib/flot/jquery.flot.resize.js"></script>
	
<style>
.context-menu-item:hover {
	cursor:hand;
	cursor:pointer;
}
.zoom-content {
	width:100%;
	height:475px;
}
.zoomed {
 z-index : 200;
 border-style: ridge;
 border-width: 5px;
 width:500px;
 height:500px;
 margin:0 auto;
 background:#f7f7f7;
 position:absolute;
 left:50%;
 top:50%;
 margin-left:-250px;
 margin-top:-250px;
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
			<button class='varWindowButton'>VARIABLES</button>
			<span><input type='checkbox' id='autorefresh' checked />Autorefresh</span>
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
				//tickDecimals: 0,
				//StickSize: 1
			}
		};

	var Variables = {
		registeredListeners : [],
		removeListener : function (fun) {
			if (fun._indexVar >= 0) this.registeredListeners [fun._indexVar] = null	;	
		},
		registerListener : function (fun) {
			var index = this.registeredListeners.push(fun) - 1 ;
			fun._indexVar = index;
		},
		fireChange : function () {
			$.each (this.registeredListeners, function (key, obj) {
				if (obj && obj.onVariableChange) obj.onVariableChange();
			});
		},	
	}
	
	var reloadEditor = function() {
		$.post("Gateway", {
			data : editor.getSession().getValue()
		}, function(value) {
			if (editor.registeredListeners.length > 0) 
				if (autorefresh)
					$.each (editor.registeredListeners, function (key, obj) {
						if (obj!=null && obj.refresh) obj.refresh();
					});
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
	var autorefresh = true;
	var Refresher = function () {
		this.valid = true;
	}
	
	$("#autorefresh").click (function () {
		autorefresh = $(this).prop('checked');
	});
	
	$(document).ready(function() {
		editNotification = false;
		editor = ace.edit("editor");
		editor.setTheme("ace/theme/monokai");
		editor.getSession().on('change', function() {
			if (editor.timeoutId) window.clearTimeout(editor.timeoutId);
			editor.timeoutId = window.setTimeout( function () { reloadEditor(); } , 2000);
		});
		$("#resizable").resizable	({
			resize : function(event, ui) {
				editor.resize();
			}
		});
		editor.registeredListeners = [];
		editor.removeListener = function (fun) {
			if (fun._index >= 0) this.registeredListeners[_index] = null;	
		}
		editor.registerListener = function (fun) {
			var index = this.registeredListeners.push(fun) - 1;
			fun._index = index;
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
		Variables.fireChange();
		if (self.lockReload) return;
		self.lockReload = true;
		setTimeout (function () {
		self.lockReload = false;
		$.post("App?action=setVariable", {
			"name" : self.name,
			"value" : self.tempValue,
		}, function(data) {
		if (data == "null") {

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
	var addVarChartWindow = function (fb,variable) {
		windowsManager.add (new Window ({
				editor : editor,
				close:true,	
				closeIcon:'&#xe804;',
				closing : function (wnd) {
					Variables.removeListener (this.chartWindow);
				},
				toggle:true,
				toggleUpIcon:'&#xe801;',
				toggleDownIcon:'&#xe800;',
				moveable:true,
				moveUpIcon:'&#xe803;',
				moveDownIcon:'&#xe802;',
				resizable:true,
				title:'Variable f() : ' + variable,
				refresh : function () {
					this.chartWindow.reload ();
				},
				init : function (wnd) {
					this.wnd=wnd;
					var chartWindow = new VarChartWindow (wnd,variable,fb);
					this.chartWindow = chartWindow;
					Variables.registerListener (this.chartWindow);
					wnd.content.css('margin','0px 10px, 0px, 10px');
					editor.registerListener (this);
				}
			}));
		};
	
	var addChartWindow = function (fb,variable) {
			windowsManager.add (new Window ({
				editor : editor,
				close:true,	
				closeIcon:'&#xe804;',
				closing : function (wnd) {
					chartVisible=false;
					Variables.removeListener (this.chartWindow);
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
					Variables.registerListener (this.chartWindow);
					wnd.content.css('margin','0px 10px, 0px, 10px');
					editor.registerListener (this);
				}
			}));
		};
	VarChartWindow = function (wnd, variable, fb) {
		this.variable = variable
		this.fb=fb;
		var self = this;
		this.wnd = wnd;		
		this.terms = Array ();
		this.resizeMark = false;
		this.reloadAll();
	}
	VarChartWindow.prototype.reloadAll = function () {
		var self = this;
		self.getVariables ();
	}
	VarChartWindow.prototype.onVariableChange = function () {
		var self = this;
		if (self._lastTimeout) window.clearTimeout (self._lastTimeout);
		self._lastTimeout = window.setTimeout (function () {
			self.reload();
		},300);
	}
	VarChartWindow.prototype.getVariables = function () {
		var self = this;
		$.post ('App?action=getVariables', {type:'input',fb:self.fb}, function (data) {
			if (data == 'false') return;
			data = $.parseJSON (data);
			self.input = data;
			self.ivariable = self.input[0];
			console.log(self.ivariable);
			if (!self.ivariable) return;
			self.tab = $("<div class='term-tab' style='display:none'></div>")
			self.wnd.resizable.prepend (self.tab);
			self.tab.append('<span><h5>Select terms to show on chart</h5></span>');
			self.tab.append('<span><select class="var-tab-select"></select></span>');
			self.select = self.tab.find(".var-tab-select").first();
			$.each (self.input, function (key, variable) {
				var selected = self.ivariable == variable? ' selected ': '' 
				self.select.append("<option " + selected + " value='" + variable + "'>" + variable + "</option>");				
			});
			self.tab.append ("<span><button class='set-input-vars set-input-vars-show'>Show</button><button class='set-input-vars set-input-vars-cancel'>Cancel</button></span>");
			self.addTabListeners ();
			self.wnd.navigation.prepend("<div class='mng-button zoom-chart'></div><div class='mng-button show-input-vars-tab'>&#xe800</div>");
			self.wnd.navigation.find('.show-input-vars-tab').first().click (function () {
				self.tab.show('fast');
			});
			self.wnd.navigation.find('.zoom-chart').first().click (function () {
				self.zoom();
			});
			self.wnd.content.css ("height",150);
			self.wnd.resizable.resize(function(){
				self.resize();
			});
			self.reload();
		});
	}
	VarChartWindow.prototype.resize = function () {
		var self = this;
		if (self.resizeMark) return;
		window.setTimeout (function (		) {
			self.resizeMark = false;
			self.wnd.content.css ("height",self.wnd.resizable.css("height"));
			if (self.data.error == "") $.plot(self.wnd.content, self.data.vars, options);
		},30);
		self.resizeMark = true;
	}
	VarChartWindow.prototype.zoom = function () {
		//TODO
	}
	VarChartWindow.prototype.addTabListeners = function () {
		var self = this;
		self.tab.find ('.set-input-vars-show').first().click (function () {
			self.ivariable = self.select.find(":selected").val();
			self.tab.hide('fast');
			self.reload();
		});
		self.tab.find ('.set-input-vars-cancel').first().click(function () {
			slef.tab.hide('fast');
		});
	}	
	
	VarChartWindow.prototype.reload = function () {
		var self = this
		$.post ('App?action=getVariableFunction', {fb:self.fb, ovar:self.variable, ivar:self.ivariable}, function (data) {
			if (data == 'false') return;
			self.data = $.parseJSON (data);
			$.plot(self.wnd.content, self.data.vars, options);
		});
	}
	var ChartWindow = function (wnd, variable,fb) {
		this.variable = variable
		this.fb=fb;
		var self = this;
		this.wnd = wnd;		
		this.terms = Array ();
		this.resizeMark = false;
		this.reloadAll();
	};
	
	ChartWindow.prototype.reloadAll = function () {
		
		var self = this;
		var wnd = self.wnd
		$.post ('App?action=getTerms', {variable:self.variable,fb:self.fb}, function (data) {
			if (data == 'false') return;
			data = $.parseJSON (data);
			$.each(data, function (key, term) {
				if (term !== null)
				self.terms.push (term.name);
			});
			self.enabledTerms = self.terms;
			self.tab = $("<div class='term-tab' style='display:none'></div>")
			self.wnd.resizable.prepend (self.tab);
			self.tab.append('<span><h5>Select terms to show on chart</h5></span>');
			$.each (self.terms, function (key, term) {
				var checked = self.enabledTerms.indexOf(term)>=0 ? "chec 	ked=''" : "";
				self.tab.append("<span><input type='checkbox' " + checked + " name='" + term + "' />" + term + "<br /></span>");				
			});
			self.tab.append ("<span><button class='set-terms set-terms-show'>Show</button><button class='set-terms set-terms-cancel'>Cancel</button></span>");
	
			self.addTabListeners ();
			self.wnd.navigation.prepend("<div class='mng-button zoom-chart'></div><div class='mng-button show-terms'>&#xe800</div>");
			self.wnd.navigation.find('.show-terms').first().click (function () {
				self.tab.show('fast');
			});
			self.wnd.navigation.find('.zoom-chart').first().click (function () {
				self.zoom();
			});
			//getting chart data
			$.post (self.generateUrl (self.fb,self.variable,self.enabledTerms), null, function (data) {
				self.wnd.content.css ("height",150);
				self.data = $.parseJSON(data);
				$.plot(wnd.content, self.data.terms, options);
				self.wnd.resizable.resize(function(){
					self.resize();
				});
			});
		});
	}
	ChartWindow.prototype.zoom = function () {
		var self = this;
		var body = $("body");
		body.prepend ("<div class='zoomed'><div class='window-header'><div class='window-title'>"+ 
		self.variable + "</div><div class='window-management'><div class='mng-close mng-button'></div></div><div class='zoom-content'></div></div></div>");
		var zoomed = body.find (".zoomed").first();
		var close = zoomed.find(".mng-close").first();
		close.click (function () {
			zoomed.remove();
		});
		var content = zoomed.find(".zoom-content").first();
		if (self.data.error == "") $.plot(content, self.data.terms, options);
	}

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
	ChartWindow.prototype.onVariableChange = function () {
		var self = this;
		if (self._lastTimeout) window.clearTimeout (self._lastTimeout);
		self._lastTimeout = window.setTimeout (function (		) {
			self.reload();
		},300);
	}
	
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
								label : "<a class='show-chart-item' onclick='return false;'>Show term chart</a>",
								click : function (params) { 
									addChartWindow (params.fb, params.varname);
								}
							}
						]
					};
					var cm = new ContextMenu (options,element);
					cm.open();
				},
				showOutputVariableContextMenu : function (fb,varname,element) {
					var options = {
						title : varname + " menu :",
						params : {
							fb : fb,
							varname : varname,
							element : element,
						},
						items : [
							{ 
								label : "<a class='show-chart-item' onclick='return false;'>Show term chart</a>",
								click : function (params) { 
									addChartWindow (params.fb, params.varname);
								}
							},
							{ 
								label : "<a class='show-chart-item' onclick='return false;'>Show variable chart</a>",
								click : function (params) { 
									addVarChartWindow (params.fb, params.varname);
								}
							}
						]
					};
					var cm = new ContextMenu (options,element);
					cm.open();
				},	
				title:'PROJECT TREE',
				refresh : function () {
					var self = this;
					wnd=this.wnd;
					$.post("App?action=getTreeData", null, function(data) {
						var tree = wnd.content;
						//tree.jstree("destroy");
						tree.bind ('select_node.jstree', function (node,selected) {
							var id = selected.node.id;
							var anchorId= id + "_anchor";
							var nodeElement = $("#" + anchorId);
							if (nodeElement.hasClass('input-var')) {
								self.showVariableContextMenu (nodeElement.attr('data-function_block'),nodeElement.attr('name'),nodeElement);
							}
							if (nodeElement.hasClass('output-var') || nodeElement.hasClass('inline-var'))	 {
								self.showOutputVariableContextMenu (nodeElement.attr('data-function_block'),nodeElement.attr('name'),nodeElement);
							}
						});
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
	//~ 
	var windowContainer = $("#window-rail");
    var windowsManager = new WindowsManager (windowContainer);
       
</script>

