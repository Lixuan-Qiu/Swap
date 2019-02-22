
var $: any;
var msg: Message;
var add: Add;
var username: any;
var userid: any;
var Handlebars: any;

//Add class provides the function for posting a message to the board
class Add {
    constructor() {
        $("#Add-newMessageBtn").click(this.addMsg);
    }
    //sned user's message and username to backend
    private addMsg() {
        let newMsg = $("#Add-newMessage").val();
        $.ajax({
            type: "POST",
            url: "/newMessage",
            dataType: "json",
            data: JSON.stringify({ mMessage: newMsg}),
            success: msg.refresh
        });

    }
}
//Message class provides methods for refresh the whole list of messages
class Message {
    //get all the information 
    refresh() {
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: msg.updateMessage
        });
    }

    //clear all the messages and load the most updated messages
    private updateMessage(data: any) {
        if(data.mStatus=="error"){
            console.log("UpdateFail");
        }

        $("#Message").html(Handlebars.templates["Message.hb"](data));
    }


}
//on load function creates all the objects
$(function () {
    msg = new Message();
    add = new Add();
    setInterval(function(){ 
        console.log("Refresh every 2 s" );

        msg.refresh();
    }, 10000);
    
});