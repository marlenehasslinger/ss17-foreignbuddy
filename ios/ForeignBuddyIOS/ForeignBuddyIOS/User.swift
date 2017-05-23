//
//  User.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 21.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation

class User{

var userID:String?
var username: String?
var email: String?
var nativeLanguage: String?
var language: String?
var urlProfilephoto: String?
var lastKnownCity: String?
var distanceToMatch: Int?
var interests: Array<Bool>?


init(username: String?, nativeLanguage: String?, language: String?, distanceToMatch: Int?){
    
self.username = username
self.nativeLanguage = nativeLanguage
self.language = language
self.distanceToMatch = distanceToMatch

}

/*
let latitude;
let longitude;
let interests;
let profilePhoto;
let commonInterest = new ArrayList<>();
let numberOfCommonInterest;
let Double distanceToMyUser;
*/
    
    
}
