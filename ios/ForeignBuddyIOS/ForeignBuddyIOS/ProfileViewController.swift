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
import SDWebImage

class ProfileViewController: UIViewController, CLLocationManagerDelegate {
        
    @IBOutlet weak var iv_profilePhoto: UIImageView!
    @IBOutlet weak var btn_logout: UIButton!
    @IBOutlet weak var lbl_Location: UILabel!
    @IBOutlet weak var lbl_NativeLanguage: UILabel!
    @IBOutlet weak var lbl_Username: UILabel!
    @IBOutlet weak var lbl_ForeignLanguage: UILabel!
    let refFirebase = FirebaseSingletonPattern.getInstance()
    
    let locationManager =  CLLocationManager()
    
    
    override func viewDidAppear(_ animated: Bool) {
        refFirebase.loadCurrentUserData()
        //Set UI Labels with user data
        lbl_Username.text = refFirebase.user?.username
        lbl_NativeLanguage.text = refFirebase.user?.nativeLanguage
        lbl_ForeignLanguage.text = refFirebase.user?.language
        
        
        
        let urlProfilephoto = refFirebase.user?.urlProfilephoto
            
        let url = URL(string: urlProfilephoto!)
        
        iv_profilePhoto.sd_setImage(with: url!)
        
        
        //Set imageview with image from Firebase Database
        
        /*
        if let urlProfilephoto = refFirebase.user?.urlProfilephoto {
        
            let url = URL(string: urlProfilephoto)
            URLSession.shared.dataTask(with: url!, completionHandler: { (data, response, error) in
            
                if error != nil {
                    print(error ?? "Profile photo download went wrong")
                    return
                }
            
                
                let backgroundQueue = DispatchQueue(label: "de.hdm-stuttgart.ForeignBuddyIOS",
                                                    qos: .background,
                                                    target: nil)
                
                backgroundQueue.async {
                  self.iv_profilePhoto?.image = UIImage(data: data!)
                }
                
                
            })
        }
        
        */
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        iv_profilePhoto.setRounded()
        
       /*
        iv_profilePhoto.layer.cornerRadius=iv_profilePhoto.frame.size.width/2
        iv_profilePhoto.clipsToBounds = true
         */
        
    
        //Set Location
        locationManager.requestWhenInUseAuthorization()
        
        
        if CLLocationManager.locationServicesEnabled(){
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
        }
        
    }
    

    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            lbl_Location.text = String(location.coordinate.latitude)
            CLGeocoder().reverseGeocodeLocation(location, completionHandler: { (placmark, error) in
                if (error != nil) {
                    print("Error occured while resolving location")
                } else {
                    if let place = placmark?[0] {
                        self.lbl_Location.text = place.locality
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
    @IBAction func btnLogout_clicked(_ sender: UIButton) {
        try! FIRAuth.auth()!.signOut()
        
        self.performSegue(withIdentifier: "goToLogin", sender: self)
    }

    
    
    @IBAction func btn_choosePhoto(_ sender: UIButton) {
        
       handleSelectProfileImageView()
    }
    
    
    @IBAction func btn_takePhoto(_ sender: UIButton) {
        
        handleTakePhoto()
        
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
