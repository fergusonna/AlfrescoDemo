var contentType = "my:expenseReport";
var documentName = url.templateArgs.documentName;
var currentTimeMillis = new Date().getTime();
documentName += "_" + currentTimeMillis.toString();

var randNum = Math.random()*100; //used to randomize set properties

//Folder for my place reports into
var folder = companyhome.childByNamePath("MyCo");

//If folder doesn't exist, then it is created
if (folder == undefined){
	folder = companyhome.createFolder("MyCo");
}

//Array to keep the properties in.
var properties = new Array();

properties["my:totalAmount"] = randNum.toFixed(2);
properties["my:clientName"] = "MyCo";

//Randomizing the Related Product
randNum = Math.floor(Math.random()*3);
if(randNum == 1){
	properties["my:product"] = "Flux Capacitor"; 
} else if (randNum == 2){
	properties["my:product"] = "Foobar Widget";
} else {
	properties["my:product"] = "Big Bold App";
}
//Random Expense Type
randNum = Math.floor(Math.random()*3);
if(randNum == 1){
	properties["my:expenseType"] = "Hours Billable"; 
} else if (randNum == 2){
	properties["my:expenseType"] = "Supply Expense";
} else {
	properties["my:expenseType"] = "License Cost";
}

//Create document
var document = folder.createNode(documentName, contentType, properties);

if (document != null){
	model.document = document;
	model.msg = "Created Expense Report. OK!";
} else {
	model.msg = "Failed to create document!";
}