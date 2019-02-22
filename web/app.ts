/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/Login.ts"/>
/// <reference path="ts/Profile.ts"/>
/// <reference path="ts/EditProfile.ts"/>
// / <reference path="ts/Account.ts"/>
/// <reference path="ts/UpdateInfo.ts"/>

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
let $: any;
var profile: any;
// let googleUser: any;
const backendUrl = "https://desolate-eyrie-25147.herokuapp.com";


// Prevent compiler errors when using Handlebars
let Handlebars: any;

// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation 
let editEntryForm: EditEntryForm;

/**
 * For the yolo login in html to work the onSignIn function must be
 * declared at the run time
 * Login.onSignIn will on be created after the Login.refresh is called,
 * which will be created after the login already been executed
 * @param googleUser data return from yolo login
 */
// function onSignIn(googleUser) {
//     // Useful data for your client-side scripts:
//     profile = googleUser.getBasicProfile();
//     console.log("ID: " + profile.getId()); // Don't send this directly to your server!
//     console.log('Full Name: ' + profile.getName());
//     console.log('Given Name: ' + profile.getGivenName());
//     console.log('Family Name: ' + profile.getFamilyName());
//     console.log("Image URL: " + profile.getImageUrl());
//     console.log("Email: " + profile.getEmail());
//     // The ID token you need to pass to your backend:
//     profile.username = profile.getName();
//     profile.email = profile.getEmail();
//     // The ID token you need to pass to your backend:
//     var id_token = googleUser.getAuthResponse().id_token;
//     profile.id_token = id_token;
//     console.log("ID Token: " + id_token);

//     //because of the time difference between when each instance of the classes
//     //Login, Navbar, and etc, a timeout is set outside their class so the function
//     //is called properly 
//     setTimeout(() => {
//         Login.isLoggedIn = true;
//         Navbar.signedIn();
//         $.ajax({
//             type: "POST",
//             url: "/login",
//             dataType: "json",
//             data: JSON.stringify({ uUsername: profile.username, token: profile.id_token}),
//             success: Login.onServerSignIn,
//         })
//     }, 1000);
// };

// Run some configuration code when the web page loads
$(document).ready(function () {

    Navbar.refresh();
    NewEntryForm.refresh();
    Login.refresh();
    ElementList.refresh();
    Profile.refresh();

    // Account.refresh();
    // CreateAccount.refresh();
    // Create the object that controls the "Edit Entry" form
    //editEntryForm = new EditEntryForm();
    EditEntryForm.refresh();
    EditProfile.refresh();
    // UpdateInfo.refresh();
    // set up initial UI state
    $("#editElement").hide();
});