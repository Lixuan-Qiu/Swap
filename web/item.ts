
class Item{
    constructor() {
        
    }
    deleteItem(){
        $.ajax({
            type: "DELETE",
            url: "/item/"+ selectedId, //selectedId stores the id of the item that been clicked on 
            dataType: "json",
            success: this.redirctAfterDelete
        });
    }
    getItemById(){
        $.ajax({
            type: "GET",
            url: "/item/?id="+ selectedId, //selectedId stores the id of the item that been clicked on 
            dataType: "json",
            success: this.displayItemInfo
        });
    }
    public displayItemInfo(data:any){
        let i = selectedId;
        console.log(i);
        $("#itemInfo").empty();
        //$("#Message").html(Handlebars.templates["item.hb"](data));
        //"<div id=\'itemPage-left\'>"+ "<p>" +data.item[i].itemTitle+"</p>"+ "<p>" +data.item[i].itemDescription+"</p>"+"</div>" + +"<div id=\'itemPage-right\'>"+ "</div>"
        //"<p>" +data.item[i].itemTitle+"</p>" + "<p>" +data.item[i].itemDescription+"</p>" + "<p>" +data.item[i].itemSeller+"</p>"+ "<p>" +data.item[i].itemPrice+"</p>"
        $("#itemInfo").append("<p>" +data.item[i].itemTitle+"</p>" + "<p>" +data.item[i].itemDescription+"</p>" + "<p>" +data.item[i].itemSeller+"</p>"+ "<p>" +data.item[i].itemPrice+"</p>");
    }
    redirctAfterDelete(){
        window.location.href = "item.html";
    }
}
$(function () {
    let item = new Item();
    item.getItemById();
    $("#delete").click(function(){
        item.deleteItem();
    })
    
});