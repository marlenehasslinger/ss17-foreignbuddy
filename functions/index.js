const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.matches = functions.database
	.ref('/users/{userID}/latitude')
	.onWrite(event => {
		const uid = event.params.userID; // Actual UserID
		const user = event.data.adminRef; 
		console.log(uid);
	
		//Write Matches in Database
		admin.database().ref("/users/" + uid + "/matches").set("TestTest");
});

exports.lastMessage = functions.database
	.ref('/chats/{chatID}/{chatMessage}')
	.onWrite(event => {
		const chatID = event.params.chatID
		const uID1 = chatID.substr(0, chatID.indexOf("_"));
		const uID2 = chatID.substr(chatID.indexOf("_")+1, chatID.length);
		const message = event.data.val();
		const lastMessage = message.messageText;
	
		admin.database().ref("/users/" + uID1 + "/conversations/" + chatID + "/lastMessage").set(lastMessage);
		admin.database().ref("/users/" + uID2 + "/conversations/" + chatID + "/lastMessage").set(lastMessage);

	
});