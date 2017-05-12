const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//Matching Algoirithm
exports.matches = functions.database
	.ref('/users/{userID}/latitude')
	.onWrite(event => {
		const myUid = event.params.userID; // Actual UserID
		var matches = new Array();
	
		const ref = admin.database().ref("users");
		var myUser;
		ref.once('value')
  			.then(function(snapshot) {
				snapshot.forEach(function(childSnapshot) {
      				var key = childSnapshot.key;
      				var childData = childSnapshot.val();
					if (key === myUid) {
						myUser = childData;
					} else {
						matches.push(childData);
					}
				});	
				matches = filter(matches, myUser);
				var justWait = admin.database().ref("/users/" + myUid + "/matches").remove();
				for(var i = 0; i < matches.length; i++) {
					var justWait2 = admin.database().ref("/users/" + myUid + "/matches/" + matches[i].userID).set(4);
				}
  			});
});

//Set LastMessage to the Conversation
exports.lastMessage = functions.database
	.ref('/chats/{chatID}/{chatMessage}')
	.onWrite(event => {
		const chatID = event.params.chatID
		const uID1 = chatID.substr(0, chatID.indexOf("_"));
		const uID2 = chatID.substr(chatID.indexOf("_")+1, chatID.length);
		const message = event.data.val();
		const lastMessage = message.messageText;
	
		admin.database().ref("/users/" + uID1 + "/conversations/" + chatID + "/lastMessage").set(lastMessage);
		return admin.database().ref("/users/" + uID2 + "/conversations/" + chatID + "/lastMessage").set(lastMessage);
});

function filter(matches, myUser) {
	var newMatches = new Array();
	
	for(var i = 0; i < matches.length; i++) {
		// Fit User Languages?
		if(matches[i].nativeLanguage === myUser.language &&
		   matches[i].language === myUser.nativeLanguage){
			//Is distance in desired User range?
			if (distanceOK(matches[i], myUser) == true){
				newMatches.push(matches[i]);
			}	
		}
	}
	return newMatches;
}
function distanceOK(match, myUser) {
  const lat1 = match.latitude;
  const lon1 = match.longitude;
  const lat2 = myUser.latitude;
  const lon2 = myUser.longitude;
  const R = 6371; // Radius of the earth in km
  var dLat = deg2rad(lat2-lat1); // deg2rad below
  var dLon = deg2rad(lon2-lon1); 
  var a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
    Math.sin(dLon/2) * Math.sin(dLon/2)
    ; 
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c; // Distance in km
  
  if (d <= myUser.distanceToMatch && d <= match.distanceToMatch) {
	  return true;
  }
   return false;
}
function deg2rad(deg) {
  return deg * (Math.PI/180)
}