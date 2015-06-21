var Window = function (setup) {
        this.index=-1;
		this.element=null;
		this.setup=setup;
        }
	Window.prototype.close = function () {
		this.manager.close (this);
		if (this.setup.closing) this.setup.closing(this);
	}
	Window.prototype.setId = function (id) {
		this.element.attr ('id','window-' + this.manager.name + "-num-" + id);
		this.element.attr ('data-index', id);
	}
	Window.prototype.toggle = function () {
		if (this.toggleState = !this.toggleState) {
			this.resizable.show('slow');
			this.toggleElement.text('');
			this.toggleElement.append (this.setup.toggleUpIcon);
			}
		else {
			this.resizable.hide ('slow');
			this.toggleElement.text('');
			this.toggleElement.append (this.setup.toggleDownIcon);
		}
	}
	Window.prototype.moveUp = function () {
		this.manager.moveUp (this);
	}
	Window.prototype.moveDown = function () {
		this.manager.moveDown (this);
	}

    Window.prototype.init = function () {
		var self = this;
		this.element.append ("<div class='window-header'></div><div class='window-resizable'><div class='window-content'></div></div>");
		this.content = this.element.find (".window-content").first();
		this.resizable = this.element.find(".window-resizable").first();
		this.header = this.element.find (".window-header").first();
		this.header.append ("<div class='window-title'>"+this.setup.title+"</div><div class='window-management'></div>");
		this.navigation = this.element.find(".window-management").first();
		
		if (this.setup.moveable) {
			this.toggleState = true;
			this.navigation.append ("<div class='mng-move-up mng-button'>" + this.setup.moveUpIcon +"</div>" + "<div class='mng-move-down mng-button'>" + this.setup.moveDownIcon +"</div>");
			this.moveUpElement = this.navigation.find ('.mng-move-up').first();
			this.moveDownElement = this.navigation.find ('.mng-move-down').first();
			this.moveUpElement.click( function () {
				self.moveUp();
			});
			this.moveDownElement.click( function () {
				self.moveDown();
			});
		}
		
		if (this.setup.toggle) {
			this.toggleState = true;
			this.navigation.append ("<div class='mng-toggle mng-button'>" + this.setup.toggleUpIcon +"</div>");
			this.toggleElement = this.navigation.find ('.mng-toggle').first();
			this.toggleElement.click( function () {
				self.toggle();
			});
		}
		if (this.setup.close) {
			this.navigation.append ("<div class='mng-close mng-button'>"+this.setup.closeIcon+"</div>");
			this.navigation.find('.mng-close').first().click( function () {
				self.close();
			});
		}
		if (this.setup.resizable) {
			this.resizable.resizable({
				handles: 's'
				});
		}
		if (this.setup.init) this.setup.init(this);
	}
	
    function WindowsManager (container) {
                this.name = container.attr ('id');
                this.container = container;
                this.windows = [];
        }
	
	WindowsManager.prototype.add = function (window) {
				window.manager = this;
                window.index = this.windows.push(window) - 1;
                this.container.append("<div id='window-" + this.name + "-num-" + window.index + "' class='bs-window' data-index='"+window.index+"'></div>");
                window.element = this.container.find ($("#window-" + this.name + "-num-" + window.index)).first();	
               // window.element[0].__hwnd = window; //easydebug :D
                window.init ();
        } 
        
        WindowsManager.prototype.moveUp = function (window) {	
			var pos = window.index;
			if (pos == 0) return;
			this.windows[pos] = this.windows [pos-1];
			this.windows[pos-1] = window;
			this.windows[pos].setId(pos);
			this.windows[pos-1].setId(pos-1);
			this.windows[pos].index=pos;
			this.windows[pos-1].index=pos-1;
			this.swap (window.element[0], window.element.prev()[0]);
		}
		
		WindowsManager.prototype.moveDown = function (window) {
			var pos = window.index;
			if (pos == this.windows.length-1) return;
			this.windows[pos] = this.windows [pos+1];
			this.windows[pos+1] = window;
			this.windows[pos].index=pos;
			this.windows[pos+1].index=pos+1;
			this.windows[pos].setId(pos);
			this.windows[pos+1].setId(pos+1);
			this.swap (window.element[0], window.element.next()[0]);
		}
        
        WindowsManager.prototype.close = function (window) {
                for (var i=window.index; i<this.windows.length; i++) {
					this.windows[i].index -= 1;
					this.windows[i].setId (i-1);
				}
				this.windows.splice (window.index,1);
				window.element.remove();
        }
        WindowsManager.prototype.refresh = function () {
                this.windows.forEach ( function (window) {
                        window.refresh();
                });
        }
        WindowsManager.prototype.swap = function (elm1, elm2) {
			if (elm1 == undefined || elm2 == undefined) return;
			var parent1, next1,
			parent2, next2;

			parent1 = elm1.parentNode;
			next1   = elm1.nextSibling;
			parent2 = elm2.parentNode;
			next2   = elm2.nextSibling;

			parent1.insertBefore(elm2, next1);
			parent2.insertBefore(elm1, next2);
		}

        var windowContainer = $("#window-container");
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
		});
		windowsManager.add(wnd);
		wnd.content.append ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a urna tortor. Cras sagittis ut risus vitae gravida. Aenean eu sem velit. Mauris rutrum, dui vitae pellentesque aliquam, arcu massa bibendum justo, consequat sagittis lectus ex luctus lacus. Sed at ipsum lectus. Integer aliquet sit amet dui at aliquet. Nam arcu orci, sodales id nunc non, aliquam congue nisi. Donec laoreet quam est, in congue mass");
	});
