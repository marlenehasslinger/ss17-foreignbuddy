//
//  ProfileViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 18.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase
import SDWebImage

class ProfileViewController: UIViewController {
        
    @IBOutlet weak var iv_profilePhoto: UIImageView!
    @IBOutlet weak var btn_logout: UIButton!
    @IBOutlet weak var lbl_Location: UILabel!
    @IBOutlet weak var lbl_NativeLanguage: UILabel!
    @IBOutlet weak var lbl_Username: UILabel!
    @IBOutlet weak var lbl_ForeignLanguage: UILabel!
    let refFirebase = FirebaseSingletonPattern.getInstance()

    
    
    override func viewDidAppear(_ animated: Bool) {
        refFirebase.loadCurrentUserData()
        //Set UI Labels with user data
        lbl_Username.text = refFirebase.user?.username
        lbl_NativeLanguage.text = refFirebase.user?.nativeLanguage
        lbl_ForeignLanguage.text = refFirebase.user?.language
        lbl_Location.text = refFirebase.user?.lastKnownCity
        
        
        
        let urlProfilephoto = refFirebase.user?.urlProfilephoto
            
        
        if !(urlProfilephoto!.isEmpty) {
            let url = URL(string: urlProfilephoto!)
            iv_profilePhoto.sd_setImage(with: url!)
        } else {
            iv_profilePhoto.image = UIImage(named: "user_male")
        }
        
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
        
        NotificationCenter.default.addObserver(forName: NSNotification.Name(Location.LOCATION_UPDATED), object: nil, queue: nil) { notification in
            self.lbl_Location.text = self.refFirebase.user?.lastKnownCity
            print("Location updated")
        }
        
        Location.getInstance().startLocationUpdate()

    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        iv_profilePhoto.setRounded()
        
       /*
        iv_profilePhoto.layer.cornerRadius=iv_profilePhoto.frame.size.width/2
        iv_profilePhoto.clipsToBounds = true
         */
        
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
