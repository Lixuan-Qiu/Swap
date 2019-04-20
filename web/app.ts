//import { loadavg } from "os";
/// This constant indicates the path to our backend server
//const backendUrl = "https://swap-lehigh.herokuapp.com";


var main: Main;
var selectedId: any; //record the id of the item being clicked
var selectedCategory: any; //record the category of the selected checkbox, it is "all" when every checkboxes are unchecked

//provide methods for main page
class Main {
    constructor() {
    }
    //helper functions that empty the main page and do ajax calls
    getAll() {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: /*backendUrl + */"/item/all",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getSchool() {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=school",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getCar() {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=car",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getElectronics() {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=electronics",
            dataType: "json",
            success: main.updateItemList
        });
    }
    getFurniture() {
        $("#Message").empty();
        $.ajax({
            type: "GET",
            url: "/item?category=furniture",
            dataType: "json",
            success: main.updateItemList
        });
    }
    lowToHigh() {
        if (selectedCategory == "all") {
            $.ajax({
                type: "GET",
                url: /*backendUrl + */"/item?pricerank=" + "asc",
                dataType: "json",
                success: main.updateItemList
            });
        } else {
            $.ajax({
                type: "GET",
                url: /*backendUrl + */"/item?category=" + selectedCategory + "&pricerank=" + "asc",
                dataType: "json",
                success: main.updateItemList
            });
        }
    }
    highToLow() {
        if (selectedCategory == "all") {
            $.ajax({
                type: "GET",
                url: /*backendUrl + */"/item?pricerank=" + "dsc",
                dataType: "json",
                success: main.updateItemList
            });
        } else {
            $.ajax({
                type: "GET",
                url: /*backendUrl + */"/item?category=" + selectedCategory + "&pricerank=" + "dsc",
                dataType: "json",
                success: main.updateItemList
            });
        }
    }

    //clear all the messages and load the most updated messages; 
    //also gives each item an onclick function "itemPage()"
    public updateItemList(data: any) {
        console.log("Update List");
        console.log(data);
        console.log("length: " + data.mData.length);
        console.log("id0= " + data.mData[0].itemId);
        console.log("title0= " + data.mData[0].itemTitle);

        $("#Message").empty();
        for (let i = 0; i < data.mData.length; i++) {
            $("#Message").append("<p " + "id='" + data.mData[i].itemId + "' " + " onclick='" + "itemPage(this, " + data.mData[i].itemId + ")'" + " >" + data.mData[i].itemTitle + " " + data.mData[i].itemPrice + "</p>");
        }
    }

    //helps the onclick function to redirct
    public getItem(id: any) {
        console.log("getItem");
        window.location.href = "item.html";
    }



}
//onclick function that redirct the page to "item.html" which display all the infomation about that specific item
function itemPage(elm: any, i: any) {
    //item = new Item();
    console.log("click id= " + i);
    main.getItem(i);
    selectedId = i;
}
//onclick function that redirects "postItem.html"
function redirectToPostItemPage(){
    window.location.href = "postItem.html";
}

//on load function creates all the objects
$(function () {
    //display all items at first time
    main = new Main();
    main.getAll();
    
    //checkbox event listers
    $("#all").change(
        function () {
            if ($(this).is(':checked')) {
                main.getAll();
            }

        }
    );
    $("#school").change(
        function () {
            if ($(this).is(':checked')) {
                selectedCategory = "school";
                main.getSchool();
            }
            else {
                selectedCategory = "all";
                console.log("Unchecked!");
            }

        }
    );
    $("#car").change(
        function () {
            if ($(this).is(':checked')) {
                selectedCategory = "car";
                main.getCar();
            }
            else {
                selectedCategory = "all";
                console.log("Unchecked!");
            }
        }
    );
    $("#electronics").change(
        function () {
            if ($(this).is(':checked')) {
                selectedCategory = "electronics";
                main.getElectronics();
            }
            else {
                selectedCategory = "all";
                console.log("Unchecked!");
            }
        }
    );
    $("#furniture").change(
        function () {
            if ($(this).is(':checked')) {
                selectedCategory = "furniture";
                main.getFurniture();
            } else {
                selectedCategory = "all";
                console.log("Unchecked!");
            }
        }
    );
    $("#high_to_low").change(
        function () {
            if ($(this).is(':checked')) {
                main.lowToHigh();
            }
        }
    );
    $("#low_to_high").change(
        function () {
            if ($(this).is(':checked')) {
                main.highToLow();
            }
        }
    );
});

