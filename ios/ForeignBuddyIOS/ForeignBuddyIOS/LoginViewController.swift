//
//  LoginViewController.swift
//  foreignbuddy
//
//  Created by Marlene Hasslinger on 01.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth


class LoginViewController: UIViewController {

    @IBOutlet weak var signinSelector: UISegmentedControl!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var signinButton: UIButton!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var errorTextLabel: UILabel!
    
    var isSignIn:Bool = true
    
    let refFirebase = FirebaseSingletonPattern.getInstance()

    
    override func viewDidLoad() {
        super.viewDidLoad()
            }
    
    
    override func viewDidAppear(_ animated: Bool) {
        registerButton.isHidden = true
        signinButton.isHidden = false
        FIRAuth.auth()!.addStateDidChangeListener() { auth, user in
            if user != nil && self.isSignIn {
                self.refFirebase.loadCurrentUserData()
                self.performSegue(withIdentifier: "leadsToHome", sender: self)
                
            }
        }

      
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func signinSelectorChanged(_ sender: UISegmentedControl) {
        //Flip boolean
        isSignIn = !isSignIn
        //Check boolean and set the button and labels
        if isSignIn {
            signinButton.isHidden = false
            registerButton.isHidden = true
        }else {
            signinButton.isHidden = true
            registerButton.isHidden = false
        }
    }

    @IBAction func signinButtonClicked(_ sender: Any) {
        //Do some form validation on email and password
        if let email = emailTextField.text, let pass = passwordTextField.text{
            //Check if its sign in or register
            if isSignIn{
                //Sign in the user with Firebase
                FIRAuth.auth()?.signIn(
                    withEmail: email, password: pass, completion:
                    { (user, error) in
                        //Check that user isnt nil
                        if  user != nil{
                            //User is found, go to homescreen
                            self.errorTextLabel.text = " "
                            self.performSegue(withIdentifier: "leadsToHome", sender: self)
                        }else{
                            //Check error and show message
                            self.errorTextLabel.text = "Wrong Password or Email"
                        }
                }
                )
            }
        }

    }
    

    
    @IBAction func registerButtonClicked(_ sender: Any) {
        if let email = emailTextField.text, let pass = passwordTextField.text {
            //Check if its sign in or register
            if !isSignIn{
            FIRAuth.auth()?.createUser(
                withEmail: email, password: pass, completion:
                { (user, error) in
                    //Check that user isnt nil
                    if  user != nil{
                        //Save user to database
                        let refFirebase = FirebaseSingletonPattern.getInstance()
                        refFirebase.insertUser()
                        //Go to user detail form,if no profile exist
                        self.performSegue(withIdentifier: "leadsToUserDetail", sender: self)
                    } else {
                        //Check Error and show message
                        self.errorTextLabel.text = "Something went wrong"
                    }
            }
            )
        }
        }
        
        
    }
 
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        //Dismiss keyboard when the view is tapped on
        emailTextField.resignFirstResponder()
        passwordTextField.resignFirstResponder()
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
