//import { loadavg } from "os";
/// This constant indicates the path to our backend server
//const backendUrl = "https://swap-lehigh.herokuapp.com";

var $: any;
var item: Item;
var add: Add;
var username: any;
var userid: any;
var Handlebars: any;
var idList = [];
var selectedId: any;
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
        
    }
    //get all the information 
    refresh() {
        console.log("refresh");
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item/all",
            dataType: "json",
            success: item.updateItemList
        });
    }
    getSchool(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=School",
            dataType: "json",
            success: item.updateItemList
        });
    }
    getCar(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/car",
            dataType: "json",
            success: item.updateItemList
        });
    }
    getElectronics(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/electronics",
            dataType: "json",
            success: item.updateItemList
        });
    }
    getFurniture(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/furniture",
            dataType: "json",
            success: item.updateItemList
        });
    }
    getAll(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item/all",
            dataType: "json",
            success: item.updateItemList
        });  
    }
    //clear all the messages and load the most updated messages
    private updateItemList(data:any) {
        console.log("Update List");
        console.log(data);
        /*
        console.log(data.item[0].itemId);
        console.log(data.item[0].itemDescription);
        */
        $("#Message").empty();
        for(let i = 0; i<data.item.length;i++){
            $("#Message").append("<p "+ "id='"+data.item[i].itemId +"' "+ " onclick='"+ "itemPage(this, "+ i+")'" + " >" +data.item[i].itemTitle+"</p>");
        }
        /*
        for(let i = 0; i<data.length;i++){
            $("#"+i).click(function(){
                window.location.href = "item.html";
            });
        }
        */
        //$("#Message").html(
        //$("#Message").html(Handlebars.templates["itemList.hb"](data));
        /*
        for(let i = 0; i<data.length;i++){
            $("#" + data[i].itemId).click(this.getItemInfo(data[i].itemId))
        }
        */
    }
    
    public getItemListByName(name:any){
        if(name=="all")
            this.refresh();
        $.ajax({
            type: "GET",
            url: "/itemList/" + name,
            dataType: "json",
            success: item.updateItemList
        });
    }
    private getItemInfo(id:any){
        $.ajax({
            type: "GET",
            url: "/itemInfo/" + id,
            dataType: "json",
            success: item.updateItemInfo
        });
    }
    public getItem(id:any){
        console.log("getItem");
        $.ajax({
            type: "GET",
            url: "/item/all",
            dataType: "json",
            success: item.displayItemInfo
        });
        
        //$("#Message").load("item.html");
        //$("#Message").html(Handlebars.templates["itemList.hb"](id));
    }
    public displayItemInfo(data:any){
        let i = selectedId;
        console.log(i);
        $("#Message").empty();
        //$("#Message").html(Handlebars.templates["item.hb"](data));
        //"<div id=\'itemPage-left\'>"+ "<p>" +data.item[i].itemTitle+"</p>"+ "<p>" +data.item[i].itemDescription+"</p>"+"</div>" + +"<div id=\'itemPage-right\'>"+ "</div>"
        //"<p>" +data.item[i].itemTitle+"</p>" + "<p>" +data.item[i].itemDescription+"</p>" + "<p>" +data.item[i].itemSeller+"</p>"+ "<p>" +data.item[i].itemPrice+"</p>"
        $("#Message").append("<p>" +data.item[i].itemTitle+"</p>" + "<p>" +data.item[i].itemDescription+"</p>" + "<p>" +data.item[i].itemSeller+"</p>"+ "<p>" +data.item[i].itemPrice+"</p>");
    }
    private updateItemInfo(data:any){
        $("#itemInfo").html(Handlebars.templates["itemList.hb"](data));
    }
    setOnClickFunction(){       
        $("#category-all").click(this.getItemListByName("all"));
        $("#category-school").click(this.getItemListByName("school"));
        $("#category-car").click(this.getItemListByName("car"));
        $("#category-electronics").click(this.getItemListByName("electronics"));
        $("#category-furniture").click(this.getItemListByName("furniture"));
    }
    
}
function getSchool(){
    item = new Item();
    item.getSchool();
}
function getCar(){
    item = new Item();
    item.getCar();
}
function getElectronics(){
    item = new Item();
    item.getElectronics();
}
function getFurniture(){
    item = new Item();
    item.getFurniture();
}
function getAll(){
    item = new Item();
    item.getAll();
}
function itemPage(elm:any, i:any){
    //item = new Item();
    console.log("click id= "+i);
    item.getItem(i);
    selectedId = i;
}
//on load function creates all the objects
$(function () {

    item = new Item();
    add = new Add();
    //item.setOnClickFunction();
    //$("#category-school").on("click",item.getItemListByName("school"));
    item.refresh();
    /*
    setInterval(function(){ 
        console.log("Refresh every 2 s" );

        item.refresh();
    }, 10000);
    */
    
});