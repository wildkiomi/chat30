var URL = "ws://localhost:8080/web-client";
var websocket;

$(document).ready(function(){
    connect();
});

function connect(){
    websocket = new WebSocket(URL);
    websocket.onopen = function(evnt) { onOpen(evnt) };
    websocket.onmessage = function(evnt) { onMessage(evnt) };
    websocket.onerror = function(evnt) { onError(evnt) };
}


function sendMessage() {
    var sMessage=document.getElementById('message').value
    if (sMessage.search(/\//) == -1) sMessage="/message "+sMessage
   var sendMessage={
       "message": sMessage.toString(),
       "sender": "web",
    }
    websocket.send(JSON.stringify(sendMessage));
}


function onOpen() {
    updateStatus("connected")
}
function onMessage(evnt) {
    if (typeof evnt.data == "string") {
        var rMessage = JSON.parse(evnt.data)
        $("#received_messages").append(
            $('<tr/>')
                .append($('<td/>').text("1"))
                .append($('<td/>').text(rMessage.sender))
                .append($('<td/>').text(rMessage.message)));

    }

}
function onError(evnt) {
    alert('ERROR: ' + evnt.data);
}
function updateStatus(status){
    if(status == "connected"){
        $("#status").removeClass (function (index, css) {
            return (css.match (/\blabel-\S+/g) || []).join(' ')
        });
        $("#status").text(status).addClass("label-success");
    }
}