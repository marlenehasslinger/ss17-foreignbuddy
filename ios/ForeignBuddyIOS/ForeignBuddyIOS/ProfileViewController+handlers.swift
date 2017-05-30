//
//  LoginController+handlers.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 25.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase

let refFirebase = FirebaseSingletonPattern.getInstance()


extension ProfileViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
  

    func handleSelectProfileImageView(){
        let picker = UIImagePickerController()
        
        picker.delegate = self
        picker.allowsEditing = true
        
        present(picker, animated: true, completion: nil)
    }
    
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
        var selectedImageFromPicker: UIImage?
        
        if let editedImage = info["UIImagePickerControllerEditedImage"] as? UIImage {
            selectedImageFromPicker = editedImage
        } else if let originalImage = info["UIImagePickerControllerOriginalImage"] as? UIImage {
            selectedImageFromPicker = originalImage
        }
        
        if let selectedImage = selectedImageFromPicker{
            iv_profilePhoto.image = selectedImage
            
            uploadPhoto()
            
        }
        
        
        dismiss(animated: true, completion: nil)

    }
    
    func handleTakePhoto(){
        
        let picker = UIImagePickerController()
        
        picker.delegate = self
        picker.allowsEditing = true
        picker.sourceType = .camera
        
        present(picker, animated:true, completion: nil)
        
    }
    

    
    
    func uploadPhoto() {
        
        let storageRef = FIRStorage.storage().reference().child((FIRAuth.auth()?.currentUser?.email)! + "_profilePhoto.png")
        
        let uploadData = UIImagePNGRepresentation(self.iv_profilePhoto.image!)
        
        storageRef.put(uploadData!, metadata: nil, completion: { (metadata, error) in
            
            if error != nil {
                print(error ?? "Something went wrong")
                return
            }
            
            
            let downloadURLString = "\(metadata!.downloadURL()!)"
            
            self.refFirebase.insertProfilePhotoUrl(photoUrl: downloadURLString)
            print(metadata ?? "Metadata default")
        })
    
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        print ("cancel")
        dismiss(animated: true, completion: nil)
    }
    
}
