var contentType = "my:expenseReport";
var documentName = url.templateArgs.documentName;
var currentTimeMillis = new Date().getTime();
documentName += "_" + currentTimeMillis.toString();

var document = companyhome.createNode(documentName, contentType);

if (document != null){
	model.document = document;
	model.msg = "Created Expense Report. OK!";
} else {
	model.msg = "Failed to create document!";
}