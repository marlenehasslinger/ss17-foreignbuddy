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
    
    //UI Elemente
    @IBOutlet weak var iv_profilePhoto: UIImageView!
    @IBOutlet weak var btn_logout: UIButton!
    @IBOutlet weak var lbl_Location: UILabel!
    @IBOutlet weak var lbl_NativeLanguage: UILabel!
    @IBOutlet weak var lbl_Username: UILabel!
    @IBOutlet weak var lbl_ForeignLanguage: UILabel!
    let refFirebase = FirebaseSingletonPattern.getInstance()
    var profileImage : UIImage?
    //this variable is used in the ProfileViewController to prevent the application from accidently downloading an
    //old photo from firebase before a new photo is uploaded
    var imageChanged = false
    
    override func viewDidAppear(_ animated: Bool) {
        
        NotificationCenter.default.addObserver(forName: NSNotification.Name(Location.LOCATION_UPDATED), object: nil, queue: nil) { notification in
            self.lbl_Location.text = self.refFirebase.user?.lastKnownCity
            print("Location updated")
        }
        
        Location.getInstance().startLocationUpdate()

    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Load current user data from database via Singleton Pattern
        refFirebase.loadCurrentUserData()
        
        //Set UI Labels with user data from database
        lbl_Username.text = refFirebase.user?.username
        lbl_NativeLanguage.text = refFirebase.user?.nativeLanguage
        lbl_ForeignLanguage.text = refFirebase.user?.language
        lbl_Location.text = refFirebase.user?.lastKnownCity
        
        //Load profile photo URL from database
        let urlProfilephoto = refFirebase.user?.urlProfilephoto
        
        //If a profile photo is uploaded by the user already it will be downloaded via it's URL from the database
        //and set in the image view. If no profile photo is uploaded yet,
        //there won't be an url in the database so a default image will be set.
        if !(urlProfilephoto!.isEmpty) {
            let url = URL(string: urlProfilephoto!)
            if (!imageChanged){
                iv_profilePhoto.sd_setImage(with: url!)
            }
        } else {
                iv_profilePhoto.image = UIImage(named: "user_male")
            
        }

        //Set profile photo rounded
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
        //If user signs out go back to Login screen
        try! FIRAuth.auth()!.signOut()
        self.performSegue(withIdentifier: "goToLogin", sender: self)
    }

    
    
    @IBAction func btn_choosePhoto(_ sender: UIButton) {
        //function is declared in extentionProfileViewController.swift
       handleSelectProfileImageView()
    }
    
    
    @IBAction func btn_takePhoto(_ sender: UIButton) {
        //function is declared in extentionProfileViewController.swift
        handleTakePhoto()
    }
    
    

    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if(segue.identifier == "goToUserDetail") {
            let UserDetailViewController = (segue.destination as! UserDetailViewController)
            
            //set boolean 'newUser' to false because this is how we keep track of if the UserDetailViewController is called 
            //within the registration process or from the users profile when he  wants to change his current profile settings
            //In order to load the correct data (either default values in case of the registration process
            //or the current user data from data base if the user opens the view from his profile)
            //into the profileViewController this variable is used.
            UserDetailViewController.newUser = false
            
            
            
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
