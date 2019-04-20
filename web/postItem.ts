var selectedId: any;
//contianer for user's POST 
class ItemData{
    id:number;
    title:any;
    description:any;
    category:number;
    postDate:number;
    longitude:number;
    latitude:number;
    address:any;
    city:any;
    state:any;
    zipcode:number;
    tradeMethod:number;
    price:number;
    availability:any;
    availableTime:any;
    wantedItemDescription:any;
    constructor(id:number,title:any,description:any,category:number,postDate:number,longitude:number,latitude:number,address:any,city:any,state:any,zipcode:number,tradeMethod:number,price:number,availability:any,availableTime:any,wantedItemDescription:any){
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.postDate = postDate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.tradeMethod = tradeMethod;
        this.price = price;
        this.availability = availability;
        this.availableTime = availableTime;
        this.wantedItemDescription = wantedItemDescription;
    }  
}
class PostItem {
    constructor() {

    }
    //get values from input tags and put into the ItemData cotianer
    //POST ajax call to send itemData
    //call back return to the item page of just posted item
    public postItem() {
        /*"Car", "School", "Furniture", "Electronics"
        "Sell",  "Trade",   "Rent",      "GiveAway"
            1      2         3             4   */
        let title = $("#title").val();
        let description = $("#description").val();
        let category = $("#category").val();
        let categoryNum = 0;
        if(category=="Car"){
            categoryNum=1;
        }else if(category=="School"){
            categoryNum=2;
        }else if(category=="Furniture"){
            categoryNum=3;
        }else if(category=="Electronics"){
            categoryNum=4;
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
        
        let mItemData = new ItemData(0,title,description,categoryNum,postDate,11,11,address,city,state,Number(zipcode),Number(tradeMethod),Number(price),true,availableTime,wantedItemDescription);
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