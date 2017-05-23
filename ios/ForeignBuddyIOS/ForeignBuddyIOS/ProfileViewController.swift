//
//  ProfileViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 18.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase
import CoreLocation


class ProfileViewController: UIViewController, CLLocationManagerDelegate {
    
    
    @IBOutlet weak var btn_logout: UIButton!
   
    @IBOutlet weak var lblLocation: UILabel!
    
    let locationManager =  CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let refFirebase = FirebaseSingletonPattern.getInstance()
        refFirebase.loadCurrentUserData()
        
        locationManager.requestWhenInUseAuthorization()
        
        
        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
        
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            lblLocation.text = String(location.coordinate.latitude)
            CLGeocoder().reverseGeocodeLocation(location, completionHandler: { (placmark, error) in
                if (error != nil) {
                    print("Error occured while resolving location")
                } else {
                    if let place = placmark?[0] {
                        self.lblLocation.text = place.locality
                        self.locationManager.stopUpdatingLocation()
                    }
                }
            })
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
            self.present(alertControler, animated: true, completion: nil)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func btn_logout_clicked(_ sender: UIButton) {
        
        try! FIRAuth.auth()!.signOut()
        
        self.performSegue(withIdentifier: "goToLogin", sender: self)
        
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if(segue.identifier == "goToUserDetail") {
            
            let UserDetailViewController = (segue.destination as! UserDetailViewController)
            UserDetailViewController.newUser = false
            print("##############")
            print(UserDetailViewController.newUser)
            
            
            
        }
        
        
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
