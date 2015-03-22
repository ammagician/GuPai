<html>
<body>
<h2>Hello World!</h2>
<div>  
        <input type="button" id="start" value="Start"/>  
        <input type="text" id="txt"/>
        <input type="button" id="login" value="Login"/>
    </div>  
    <div id="messages"></div>  
    <script type="text/javascript" src="js/lib/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="js/lib/jquery.json.min.js"></script>
	<script type="text/javascript" src="js/lib/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="js/utils/GP.Util.ArrayList.js"></script> 
	<script type="text/javascript" src="js/utils/GP.Util.Debug.js"></script> 
	<script type="text/javascript" src="js/utils/GP.Util.Connection.js"></script>  
    <script type="text/javascript">  
    	$(document).ready(function(){
    		$("#login").bind("click",function start() { 
            	var txt = "apang";
            	var msg = {
            		txt:txt	
            	};
            	
            	var conn = new GP.Util.Connection();
            	conn.add("url", "LoginLogin.do").add({async:true, type: "GET", data:{data:$.toJSON(msg)}});
        		conn.addListener("onSuccess", {onSuccess : function (conn, res) {
        			console.info(res);
        		}});
        		conn.connect(); 
            });
    		var webSocket = new WebSocket('ws://localhost:8080/GuPai/gameServer');  
            if(webSocket != null){
            	webSocket.onerror = function(event) {  
                    onError(event)  
                };  
          
                webSocket.onopen = function(event) {  
                    onOpen(event)  
                };  
          
                webSocket.onmessage = function(event) {  
                    onMessage(event)  
                }; 
                
                function onMessage(event) {  
                    document.getElementById('messages').innerHTML   
                        += '<br />' + event.data;  
                }  
          
                function onOpen(event) {  
                    document.getElementById('messages').innerHTML   
                        = 'Connection established';  
                }  
          
                function onError(event) {  
                    alert(event.data);  
                    alert("error");  
                }  
          
                $("#start").bind("click",function start() { 
                	var txt = $("#txt");
                	var v = $.trim(txt.val());
                	txt.val("");
                	if(v == ""){
                		return;
                	}
                	var msg = {
                		txt: v	
                	}
                    webSocket.send($.toJSON(msg)); 
                    return false;  
                });
            }
    	});      
    </script>
</body>
</html>
