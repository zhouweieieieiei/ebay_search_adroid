var express = require('express');
var app = express();
var http=require("http");
var request = require("request");

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.use(express.static('ebayapp'))

app.get('/', function (req, res) {
   res.send('Hello!');
})

app.get('/search', function (req, res) {
  var searchurl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=WeiZhou-571HW6We-PRD-416de56b6-31d67bf4&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50";
  searchurl+="&keywords=";
  searchurl+=(req.query.keyword).replace(/\s/g, "_");
  if(req.query.category == "Art") searchurl+="&categoryId=550";
  if(req.query.category == "Baby") searchurl+="&categoryId=2984";
  if(req.query.category == "Books") searchurl+="&categoryId=267";
  if(req.query.category == "Clothing, Shoes & Accessories") searchurl+="&categoryId=11450";
  if(req.query.category == "Computers/Tablets & Networking") searchurl+="&categoryId=58058";
  if(req.query.category == "Health & Beauty") searchurl+="&categoryId=26395";
  if(req.query.category == "Music") searchurl+="&categoryId=11233";
  if(req.query.category == "Video Games & Consoles") searchurl+="&categoryId=1249";
  if(!(req.query.zipcode == "-1"&&req.query.currentzipcode == "-1")){
    if(req.query.zipcode == "") searchurl+="&buyerPostalCode="+req.query.currentzipcode;
    else searchurl+="&buyerPostalCode="+req.query.zipcode;
  }
  var i = 0;
  if(req.query.distance !="-1"){
    searchurl+="&itemFilter("+i.toString()+").name=MaxDistance&itemFilter("+i.toString()+").value="+req.query.distance;
    i = i+1;
  }
  if(req.query.free_shipping == "true"){
    searchurl+="&itemFilter("+i.toString()+").name=FreeShippingOnly&itemFilter("+i.toString()+").value=true";
    i = i+1;
  }
  if(req.query.local_pickup == "true"){
    searchurl+="&itemFilter("+i.toString()+").name=LocalPickupOnly&itemFilter("+i.toString()+").value=true";
    i = i+1;
  }
  searchurl+="&itemFilter("+i.toString()+").name=HideDuplicateItems&itemFilter("+i.toString()+").value=true";
  i = i+1;
  if(req.query.new == "true"||req.query.used == "true"||req.query.unspecified == "true"){
    var j = 0;
    searchurl+="&itemFilter("+i.toString()+").name=Condition";
    if(req.query.new == "true"){
      searchurl+="&itemFilter("+i.toString()+").value("+j.toString()+")=New";
      j = j+1;
    }
    if(req.query.used == "true"){
      searchurl+="&itemFilter("+i.toString()+").value("+j.toString()+")=Used";
      j = j+1;
    }
    if(req.query.unspecified == "true"){
      searchurl+="&itemFilter("+i.toString()+").value("+j.toString()+")=Unspecified";
      j = j+1;
    }
    i = i+1;
  }
  searchurl+="&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";
  //console.log(searchurl)
  request.get(searchurl, function(err, response, body){
    res.send(response.body);
  })
})

app.get('/detail', function (req, res) {
  var searchurl = "http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=WeiZhou-571HW6We-PRD-416de56b6-31d67bf4&siteid=0&version=967&ItemID=";
  searchurl+=(req.query.itemID);
  searchurl+="&IncludeSelector=Description,Details,ItemSpecifics";
  //console.log(searchurl)
  request.get(searchurl, function(err, response, body){
    res.send(response.body);
  })
})

app.get('/similar', function (req, res) {
  var searchurl = "http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=WeiZhou-571HW6We-PRD-416de56b6-31d67bf4&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=";
  searchurl+=(req.query.itemID);
  searchurl+="&maxResults=8";
  //console.log(searchurl)
  request.get(searchurl, function(err, response, body){
    res.send(response.body);
  })
})

app.get('/zip', function (req, res) {
  var searchurl = "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=";
  searchurl+=(req.query.zipcode);
  searchurl+="&username=zhouweieieieiei&country=US&maxRows=5";
  //console.log(searchurl)
  request.get(searchurl, function(err, response, body){
    res.send(response.body);
  })
})

app.get('/photos', function (req, res) {
  var searchurl = "https://www.googleapis.com/customsearch/v1?q=";
  searchurl+=(req.query.title);
  searchurl+="&cx=014919264606935340131:wqzju7ukews&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyCpMTFWkYXyuOVmwvmK-iwerRiIIuM9WwU";
  searchurl = encodeURI(searchurl);
  //console.log(searchurl)
  request.get(searchurl, function(err, response, body){
    res.send(response.body);
  })
})

var server = app.listen(8081, function () {
 
  var host = server.address().address
  var port = server.address().port
 
  //console.log("访问地址为 http://%s:%s", host, port)
 
})