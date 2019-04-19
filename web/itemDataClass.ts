
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
    constructor(id,title,description,category,postDate,longitude,latitude,address,city,state,zipcode,tradeMethod,price,availability,availableTime,wantedItemDescription){
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