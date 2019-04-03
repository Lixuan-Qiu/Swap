"use strict";
var $;
var item;
var add;
var username;
var userid;
var Handlebars;
//Add class provides the function for posting a message to the board
var Add = /** @class */ (function () {
    function Add() {
        $("#Add-newMessageBtn").click(this.addItem);
    }
    //sned user's message and username to backend
    Add.prototype.addItem = function () {
        var newDescripition = $("#Add-newItem").val();
        $.ajax({
            type: "POST",
            url: "/newItem",
            dataType: "json",
            data: JSON.stringify({ description: newDescripition }),
            success: item.refresh
        });
    };
    return Add;
}());
//Message class provides methods for refresh the whole list of messages
var Item = /** @class */ (function () {
    function Item() {
    }
    //get all the information 
    Item.prototype.refresh = function () {
        console.log("refresh");
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/itemList/all",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getSchool = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/itemList/school",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getAll = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/itemList/all",
            dataType: "json",
            success: item.updateItemList
        });
    };
    //clear all the messages and load the most updated messages
    Item.prototype.updateItemList = function (data) {
        console.log("Update List");
        $("#Message").empty();
        for (var i = 0; i < data.length; i++) {
            $("#Message").append("<p " + "id='" + data[i].itemId + "' " + " onclick='" + "itemPage(this, " + i + ")'" + " >" + data[i].itemDescription + "</p>");
        }
        //$("#Message").html(
        //$("#Message").html(Handlebars.templates["itemList.hb"](data));
        /*
        for(let i = 0; i<data.length;i++){
            $("#" + data[i].itemId).click(this.getItemInfo(data[i].itemId))
        }
        */
    };
    Item.prototype.getItemListByName = function (name) {
        if (name == "all")
            this.refresh();
        $.ajax({
            type: "GET",
            url: "/itemList/" + name,
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getItemInfo = function (id) {
        $.ajax({
            type: "GET",
            url: "/itemInfo/" + id,
            dataType: "json",
            success: item.updateItemInfo
        });
    };
    Item.prototype.getItem = function (id) {
        $("#Message").empty();
        $("#Message").append("<p> Item Page</p>");
    };
    Item.prototype.updateItemInfo = function (data) {
        $("#itemInfo").html(Handlebars.templates["itemList.hb"](data));
    };
    Item.prototype.setOnClickFunction = function () {
        $("#category-all").click(this.getItemListByName("all"));
        $("#category-school").click(this.getItemListByName("school"));
        $("#category-car").click(this.getItemListByName("car"));
        $("#category-electronics").click(this.getItemListByName("electronics"));
        $("#category-furniture").click(this.getItemListByName("furniture"));
    };
    return Item;
}());
function getSchool() {
    item = new Item();
    item.getSchool();
}
function getAll() {
    item = new Item();
    item.getAll();
}
function itemPage(elm, i) {
    item = new Item();
    item.getItem(i);
}
//on load function creates all the objects
$(function () {
    item = new Item();
    add = new Add();
    //item.setOnClickFunction();
    $("#category-school").on("click", item.getItemListByName("school"));
    item.refresh();
    /*
    setInterval(function(){
        console.log("Refresh every 2 s" );

        item.refresh();
    }, 10000);
    */
});
