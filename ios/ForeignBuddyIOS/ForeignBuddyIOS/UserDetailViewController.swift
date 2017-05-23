//
//  UserDetailViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 16.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase

class UserDetailViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    //Interest Switches
    @IBOutlet weak var cultureSwitch: UISwitch!
    @IBOutlet weak var musicSwitch: UISwitch!
    @IBOutlet weak var natureSwitch: UISwitch!
    @IBOutlet weak var politicsSwitch: UISwitch!
    @IBOutlet weak var readingSwitch: UISwitch!
    @IBOutlet weak var sportsSwitch: UISwitch!
    @IBOutlet weak var technologySwitch: UISwitch!

    
    
    
    //UI Elemente
    @IBOutlet weak var slider: UISlider!
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var nativeLanguageLabel: UILabel!
    @IBOutlet weak var nativeLanguagePickerView: UIPickerView!
    @IBOutlet weak var foreignLanguagePickerView: UIPickerView!
    
    @IBOutlet weak var usernameTextField: UITextField!
    
    //Functional variables
    var valueNativeLanguage: String = "German"
    var valueForeignLanguage: String = "English"
    var valueDistanceToMatches: Int = 100
    var newUser: Bool = true
    
    //Array for interests boolean werte
        var interests = [false, false, false, false, false, false, false]
    
    //Database Referenece via FirebaseSingletonPattern
    let refFirebase = FirebaseSingletonPattern.getInstance()

    
    //Arrays for pickers
    let nativeLanguages = ["German", "English", "Spanish", "French"]
    let foreignLanguages = ["German", "English", "Spanish", "French"]

    
    @IBAction func distanceSlider(_ sender: UISlider) {
        
        distanceLabel.text = String(Int((sender.value)))
        valueDistanceToMatches = Int((sender.value))
        print(valueDistanceToMatches)

        
        
    }
    
    @IBAction func nextBtnClicked(_ sender: Any) {
        refFirebase.insertNativeLanguage(nativeLanguage: valueNativeLanguage)
        
        refFirebase.insertForeignLanguage(foreignLanguage: valueForeignLanguage)
        
        refFirebase.insertDistanceToMatch(distanceToMatch: valueDistanceToMatches)
        
        if let username = usernameTextField.text{
            refFirebase.insertUsername(username: username)
        }
        


        if cultureSwitch.isOn {
            self.interests[0] = true
        }
        if musicSwitch.isOn {
            self.interests[1] = true
        }
        if natureSwitch.isOn {
              self.interests[2] = true
        }
        if politicsSwitch.isOn {
              self.interests[3] = true
        }
        if readingSwitch.isOn {
            self.interests[4] = true
        }
        if sportsSwitch.isOn {
              self.interests[5] = true
        }
        if technologySwitch.isOn {
              self.interests[6] = true
        }
        
        refFirebase.insertInterests(interests: interests)
        
        
        self.performSegue(withIdentifier: "goToHome", sender: self)
    }
  
    
    
    //PickerView functions for foreign language picker
    func numberOfComponents(in foreignLanguagePickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if (pickerView == nativeLanguagePickerView){
            return nativeLanguages[row]
        }else{
            return foreignLanguages[row]
    }   }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return nativeLanguages.count
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if (pickerView == nativeLanguagePickerView){
            valueNativeLanguage = nativeLanguages[row]
        }else{
            valueForeignLanguage = foreignLanguages[row]

        }
    }

    override func viewDidAppear(_ animated: Bool) {
        //Depending on if user just registrated or already has data in firebase database default values or the prior defined user settings will be loaded
        if !newUser{
            getCurrentUserData()
        } else {
            setDefaultValues()
        }
    
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        //Dismiss keyboard when the view is tapped on
        usernameTextField.resignFirstResponder()
    
    }
    
    
    func getCurrentUserData(){
 
        //Load bufferUser from Firebase via SingletonPattern reference
        let bufferUser = refFirebase.user
        
        //print(bufferUser?.username ?? "Couldnt set username")
    
        //Set username in textedit
        usernameTextField.text = bufferUser?.username
        
        //Cast Int value from database to float so it works in the switch
        let tempDistanceToMatches = Float((bufferUser?.distanceToMatch!)!)
        //Set label and slider for distance to matches
        distanceLabel.text = String((bufferUser?.distanceToMatch!)!)
        slider.setValue(tempDistanceToMatches, animated: true)
        
     
        
        //Set interest switches
        if (refFirebase.user?.interests?[0])! {
            cultureSwitch.setOn(true, animated: true)
        } else {
            cultureSwitch.setOn(false, animated: true)

        }
        if (refFirebase.user?.interests?[1])! {
            musicSwitch.setOn(true, animated: true)
        } else {
            cultureSwitch.setOn(false, animated: true)
            
        }
        if (refFirebase.user?.interests?[2])! {
            natureSwitch.setOn(true, animated: true)
        } else {
            natureSwitch.setOn(false, animated: true)
            
        }
        if (refFirebase.user?.interests?[3])! {
            politicsSwitch.setOn(true, animated: true)
        } else {
            politicsSwitch.setOn(false, animated: true)
            
        }
        if (refFirebase.user?.interests?[4])! {
            readingSwitch.setOn(true, animated: true)
        } else {
            readingSwitch.setOn(false, animated: true)
            
        }
        if (refFirebase.user?.interests?[5])! {
            sportsSwitch.setOn(true, animated: true)
        } else {
            sportsSwitch.setOn(false, animated: true)
            
        }
        if (refFirebase.user?.interests?[6])! {
            technologySwitch.setOn(true, animated: true)
        } else {
            cultureSwitch.setOn(false, animated: true)
            
        }
        
 
        
        //Set native language
        switch ((refFirebase.user?.nativeLanguage)!) {
            case "German":
            nativeLanguagePickerView.selectRow(0, inComponent: 0, animated: false)

            case "English":
            nativeLanguagePickerView.selectRow(1, inComponent: 0, animated: false)

            case "Spanish":
            nativeLanguagePickerView.selectRow(2, inComponent: 0, animated: false)

            case "French":
            nativeLanguagePickerView.selectRow(3, inComponent: 0, animated: false)

        default:
            nativeLanguagePickerView.selectRow(0, inComponent: 0, animated: false)
        }

        //Set foreign language
        switch ((refFirebase.user?.language)!) {
        case "German":
            foreignLanguagePickerView.selectRow(0, inComponent: 0, animated: false)
            
        case "English":
            foreignLanguagePickerView.selectRow(1, inComponent: 0, animated: false)
            
        case "Spanish":
            foreignLanguagePickerView.selectRow(2, inComponent: 0, animated: false)
            
        case "French":
            foreignLanguagePickerView.selectRow(3, inComponent: 0, animated: false)
            
        default:
            foreignLanguagePickerView.selectRow(1, inComponent: 0, animated: false)


        }
 
        
        
        
        }
        
    
    
    
    func setDefaultValues(){
        
        nativeLanguagePickerView.selectRow(0, inComponent: 0, animated: true)
        foreignLanguagePickerView.selectRow(1, inComponent: 0, animated: true)
        
        let email = FIRAuth.auth()?.currentUser?.email
        let usernameEmailArr = email?.components(separatedBy: "@")
        let username: String = usernameEmailArr![0]
        usernameTextField.text = username
        

        //Set interest switches to false
        cultureSwitch.setOn(false, animated: true)
        musicSwitch.setOn(false, animated: true)
        natureSwitch.setOn(false, animated: true)
        politicsSwitch.setOn(false, animated: true)
        readingSwitch.setOn(false, animated: true)
        sportsSwitch.setOn(false, animated: true)
        technologySwitch.setOn(false, animated: true)
    
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
