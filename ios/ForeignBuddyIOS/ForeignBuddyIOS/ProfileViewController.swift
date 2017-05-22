//
//  ProfileViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 18.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase

class ProfileViewController: UIViewController {
 

    @IBOutlet weak var btn_logout: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let refFirebase = FirebaseSingletonPattern.getInstance()
        refFirebase.loadCurrentUserData()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
