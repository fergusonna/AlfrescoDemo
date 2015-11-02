var contentType = "my:expenseReport";
var documentName = url.templateArgs.documentName;
var currentTimeMillis = new Date().getTime();
documentName += "_" + currentTimeMillis.toString();

var randNum = Math.random()*10;

//Array to keep the properties in.
var properties = new Array();
properties["my:totalAmount"] = randNum.toFixed(2);
properties["my:product"] = "Flux Capacitor"; 

//Create document
var document = companyhome.createNode(documentName, contentType, properties);

if (document != null){
	model.document = document;
	model.msg = "Created Expense Report. OK!";
} else {
	model.msg = "Failed to create document!";
}