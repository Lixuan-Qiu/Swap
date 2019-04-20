declare var selectedId: any;
/*main page will jump to item page if and only if one item is clicked,
  upon clicking, the id of that item will assign to selectedId.*/
class Item{
    constructor() {
        
    }
    //detele item by selectedId
    //call back will redirect to the main page
    deleteItem(){
        $.ajax({
            type: "DELETE",
            url: "/item/"+ selectedId, //selectedId stores the id of the item that been clicked on 
            dataType: "json",
            success: this.redirctAfterDelete
        });
    }
    //ajax call to get info by selectedId
    //display all info by selectedId
    getItemById(){
        $.ajax({
            type: "GET",
            url: "/item/?id="+ selectedId, //selectedId stores the id of the item that been clicked on 
            dataType: "json",
            success: this.displayItemInfo
        });
    }
    //
    public displayItemInfo(data:any){
        let i = selectedId;
        console.log(i);
        $("#itemInfo").empty();
        $("#itemInfo").append("<p>" +data.item[i].itemTitle+"</p>" + "<p>" +data.item[i].itemDescription+"</p>" + "<p>" +data.item[i].itemSeller+"</p>"+ "<p>" +data.item[i].itemPrice+"</p>");
    }
    redirctAfterDelete(){
        window.location.href = "item.html";
    }
}
$(function () {
    let item = new Item();
    item.getItemById();
    //click event listener for delete button
    $("#delete").click(function(){
        item.deleteItem();
    })
    
});