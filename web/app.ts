
var $: any;
var item: Item;
var add: Add;
var username: any;
var userid: any;
var Handlebars: any;

//Add class provides the function for posting a message to the board
class Add {
    constructor() {
        $("#Add-newMessageBtn").click(this.addItem);
    }
    //sned user's message and username to backend
    private addItem() {
        let newDescripition = $("#Add-newItem").val();
        $.ajax({
            type: "POST",
            url: "/newItem",
            dataType: "json",
            data: JSON.stringify({ description: newDescripition}),
            success: item.refresh
        });
    }
}
//Message class provides methods for refresh the whole list of messages
class Item {
    constructor() {
        $("#category-all").click(this.getItemListByName("all"));
        $("#category-school").click(this.getItemListByName("school"));
        $("#category-car").click(this.getItemListByName("car"));
        $("#category-electronics").click(this.getItemListByName("electronics"));
        $("#category-furniture").click(this.getItemListByName("furniture"));
    }
    //get all the information 
    refresh() {
        $.ajax({
            type: "GET",
            url: "/itemList/"+"all",
            dataType: "json",
            success: item.updateItemList
        });
    }
    //clear all the messages and load the most updated messages
    private updateItemList(data:any) {
        /*
        if(data.mStatus=="error"){
            console.log("UpdateFail");
        }
        */
        $("#Message").html(Handlebars.templates["itemList.hb"](data));
        for(let i = 0; i<data.itemId.length;i++){
            $("#" + data.itemId[0]).click(this.getItemInfo(data.itemId[0]))
        }
    }
    private getItemListByName(name){
        if(name.equals("all"))
            this.refresh();
        $.ajax({
            type: "GET",
            url: "/itemList/:" + name,
            dataType: "json",
            success: item.updateItemList
        });
    }
    private getItemInfo(id){
        $.ajax({
            type: "GET",
            url: "/itemInfo/" + id,
            dataType: "json",
            success: item.updateItemInfo
        });
    }
    private updateItemInfo(data:any){
        $("#itemInfo").html(Handlebars.templates["itemList.hb"](data));
    }
    
}
//on load function creates all the objects
$(function () {
    item = new Item();
    add = new Add();
    setInterval(function(){ 
        console.log("Refresh every 2 s" );

        item.refresh();
    }, 10000);
    
});