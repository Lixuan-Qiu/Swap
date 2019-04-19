//import { loadavg } from "os";
/// This constant indicates the path to our backend server
//const backendUrl = "https://swap-lehigh.herokuapp.com";

var $: any;
var main: Main;
var username: any;
var userid: any;
var Handlebars: any;
var idList = [];
var selectedId: any;
var selectedCategory: any;

//Message class provides methods for refresh the whole list of messages
class Main {
    constructor() {
        
    }
    getAll(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item/all",
            dataType: "json",
            success: main.updateItemList
        });  
    }
    lowToHigh(){
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item?category=" + selectedCategory + "price=" + "asc",
            dataType: "json",
            success: main.updateItemList
        });
    }
    highToLow(){
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item?category=" + selectedCategory + "price=" + "dsc",
            dataType: "json",
            success: main.updateItemList
        });
    }

    getSchool(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=School",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getCar(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/car",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getElectronics(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/electronics",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getFurniture(){
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item/furniture",
            dataType: "json",
            success: main.updateItemList
        });
    }
    
    //clear all the messages and load the most updated messages
    public updateItemList(data:any) {
        console.log("Update List");
        console.log(data);
        $("#Message").empty();
        for(let i = 0; i<data.item.length;i++){
            $("#Message").append("<p "+ "id='"+data.item[i].itemId +"' "+ " onclick='"+ "itemPage(this, "+ i+")'" + " >" +data.item[i].itemTitle+" "+data.item[i].itemPrice+"</p>");
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

        $.ajax({
            type: "GET",
            url: "/itemList/" + name,
            dataType: "json",
            success: main.updateItemList
        });
    }

    public getItem(id:any){
        console.log("getItem");
        window.location.href = "item.html";
        /*
        $.ajax({
            type: "GET",
            url: "/item/all",
            dataType: "json",
            success: main.displayItemInfo
        });
        */
        
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
    
    setOnClickFunction(){       
        $("#category-all").click(this.getItemListByName("all"));
        $("#category-school").click(this.getItemListByName("school"));
        $("#category-car").click(this.getItemListByName("car"));
        $("#category-electronics").click(this.getItemListByName("electronics"));
        $("#category-furniture").click(this.getItemListByName("furniture"));
    }
    
}

function getSchool(){
    main = new Main();
    main.getSchool();
}
function getCar(){
    main = new Main();
    main.getCar();
}
function getElectronics(){
    main = new Main();
    main.getElectronics();
}
function getFurniture(){
    main = new Main();
    main.getFurniture();
}
function getAll(){
    main = new Main();
    main.getAll();
}
function itemPage(elm:any, i:any){
    //item = new Item();
    console.log("click id= "+i);
    main.getItem(i);
    selectedId = i;
}


//on load function creates all the objects
$(function () {

    main = new Main();
    main.getAll();
    if( $("#category > input > #all").checked == true )
        main.getAll();
    
    for(let i=1;i<=4;i++){
        if( ($("#category > input")[i]).checked == true )
            selectedCategory = ($("#category > input")[i]).val();
            $.ajax({
                type: "GET",
                url: /*backendUrl + */"/item?category=" + selectedCategory,
                dataType: "json",
                success: main.updateItemList
            });
    }
    if( $("#price > input > #high_to_low").checked == true )
        main.lowToHigh();
    if( $("#price > input > #low_to_high").checked == true )
        main.highToLow();
    


    
    
});

