"use strict";
//import { loadavg } from "os";
/// This constant indicates the path to our backend server
var backendUrl = "https://swap-lehigh.herokuapp.com";
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
            url: backendUrl + "/item/all",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getSchool = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=school",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getCar = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=car",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getElectronics = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=electronics",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getFurniture = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=furniture",
            dataType: "json",
            success: item.updateItemList
        });
    };
    Item.prototype.getAll = function () {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: backendUrl + "/item/all",
            dataType: "json",
            success: item.updateItemList
        });
    };
    //clear all the messages and load the most updated messages
    Item.prototype.updateItemList = function (data) {
        console.log("Update List");
        console.log(data);
        console.log(data[1]);
        $("#Message").empty();
        for (var i = 0; i < data.length; i++) {
            $("#Message").append("<a " + "id='" + data[2][i].itemId + "' " + " onclick='" + "itemPage(this, " + i + ")'" + " >" + data[2][i].itemDescription + "</a>");
        }
        for (var i = 0; i < data.length; i++) {
            $("#" + i).click(function () {
                window.location.href = "item.html";
            });
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
        //$("#Message").load("item.html");
        //$("#Message").html(Handlebars.templates["itemList.hb"](id));
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
function getCar() {
    item = new Item();
    item.getCar();
}
function getElectronics() {
    item = new Item();
    item.getElectronics();
}
function getFurniture() {
    item = new Item();
    item.getFurniture();
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
