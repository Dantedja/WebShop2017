function validateLogin() {
    var un = document.loginform.usr.value;
    var pw = document.loginform.pword.value;
    sendRequest("POST","rest/shop/login","user=" + un + "&password=" +pw, function(response){
        alert(response);
        if(response == "Login succeeded"){
                loginSessionStart(un);
            }else if(response == "Login failed"){
                loginFailMessage();
            }
        });
}

function register() {
    var un = document.loginform.usr.value;
    var pw = document.loginform.pword.value;

}

function loginSessionStart(username){
    var loginState = document.getElementById("loginTracker");
    loginState.textContent = "Logged in";

    setTimeout(function() {
        location.reload();
    })
}

function loginFailMessage(){
    var loginState = document.getElementById("loginTracker");
    loginState.textContent = "Login failed!"
}