var express = require("express");
var exphbs  = require('express-handlebars');
var http = require("http");
var logger = require("morgan");
var app = express();
var path = require("path");
var bodyParser = require("body-parser");
var entries = [];
app.use(logger("dev"));
app.use(bodyParser.urlencoded({ extended: true }));
app.set("views", path.resolve(__dirname, "views"));
app.engine('handlebars', exphbs({defaultLayout: 'main'}));
app.set('view engine', 'handlebars');
app.use(express.static(__dirname + "/public/"));
app.get("/", function(req, res) {
    app.locals.entries = entries;
    res.render("index");
});





