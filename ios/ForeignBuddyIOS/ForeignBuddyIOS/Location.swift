//
//  Location.swift
//  ForeignBuddyIOS
//
//  Created by Marc-Julian Fleck on 30.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation
import Firebase
import CoreLocation

class Location : NSObject, CLLocationManagerDelegate {
    
    private static var instance : Location?
    public static let LOCATION_UPDATED = "LOCATION_UPDATED"
    
    let locationManager =  CLLocationManager()
    
    override init() {
        super.init()
    }
    
    public static func getInstance() -> Location{
        if(instance==nil){
            instance = Location()
        }
        return instance!
    }
    
    func startLocationUpdate() {
        locationManager.requestWhenInUseAuthorization()
        
        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
    }
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            CLGeocoder().reverseGeocodeLocation(location, completionHandler: { (placmark, error) in
                if (error != nil) {
                    print("Error occured while resolving location")
                } else {
                    if let place = placmark?[0] {
                        let refFirebase = FirebaseSingletonPattern.getInstance()
                        refFirebase.user?.lastKnownCity = place.locality
                        refFirebase.user?.latitude = locations.first?.coordinate.latitude
                        refFirebase.user?.longitude = locations.first?.coordinate.longitude
                        refFirebase.insertLocationUpdate(lastKnownCity: place.locality!, longitude: (locations.first?.coordinate.longitude)!, latitude: (locations.first?.coordinate.latitude)!)
                        NotificationCenter.default.post(name: NSNotification.Name(Location.LOCATION_UPDATED), object: nil)
                    }
                }
            })
            self.locationManager.stopUpdatingLocation()
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if (status == CLAuthorizationStatus.denied) {
        
            let alertControler = UIAlertController(title: "Location is denied",
                                                   message: "In order to find matches we need your Location",
                                                   preferredStyle: .alert)
            
            let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
            alertControler.addAction(cancelAction)
            
            let openAction = UIAlertAction(title: "Open Settings", style: .default, handler: { (action) in
                if let url = URL(string: UIApplicationOpenSettingsURLString) {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                }
            })
            alertControler.addAction(openAction)
            let ad = UIApplication.shared
            
            ad.keyWindow?.rootViewController?.present(alertControler, animated: true, completion: nil)
        }
    }
}
