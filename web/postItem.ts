var $: any;
class PostItem {
    constructor() {

    }
    public postItem() {
        /*"Car", "School", "Furniture", "Electronics"
        "Sell",  "Trade",   "Rent",      "GiveAway"
            1      2         3             4   */
        let title = $("#title").val();
        let description = $("#description").val();
        let category = $("#category").val();
        if(category=="Car"){
            category=1;
        }else if(category=="School"){
            category=2;
        }else if(category=="Furniture"){
            category=3;
        }else if(category=="Electronics"){
            category=4;
        }
        let d = new Date();
        let postDate = d.getFullYear() + (d.getMonth()+1) + d.getDate();
        let address = $("#address").val();
        let city = $("#city").val();
        let state = $("#state").val();
        let zipcode = $("#zipcode").val();
        let tradeMethod = $("#tradeMethod").val();
        if(tradeMethod=="Sell"){
            tradeMethod=1;
        }else if(tradeMethod=="Trade"){
            tradeMethod=2;
        }else if(tradeMethod=="Rent"){
            tradeMethod=3;
        }else if(tradeMethod=="GiveAway"){
            tradeMethod=4;
        }
        let price = $("#price").val();
        let availableTime = $("#availableTime").val();
        let wantedItemDescription = $("#wantedItemDescription").val();
        
        let mItemData = new ItemData(0,title,description,category,postDate,11,11,address,city,state,zipcode,tradeMethod,price,true,availableTime,wantedItemDescription);
        $.ajax({
            type: "POST",
            url: "/item/new", //selectedId stores the id of the item that been clicked on 
            dataType: "json",
            data: JSON.stringify({ itemData: mItemData }),
            success: this.redirct
        });
    }
    private redirct(data:any){
        console.log("In postItem page, redircting to main page");
        selectedId = data.item;
        window.location.href = "item.html";
    }

}

$(function () {
    console.log("hi, onload function of PostItem");
    let postNewItem = new PostItem();
    $("#submit_post").click(function(){
        postNewItem.postItem();
    })




});