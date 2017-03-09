function validateLogin() {
    var un = document.loginform.usr.value;
    var pw = document.loginform.pword.value;
    sendRequest("POST","rest/shop/login","user=" + un + "&password=" +pw, function(response){
        if(response == "Login succeeded"){
                loginSessionStart(un);
            }else if(response == "Login failed"){
                loginFailMessage();
            }
        });
}

function loginSessionStart(username){
    var loginState = document.getElementById("loginTracker");
    loginState.textContent = "Logged in";

    setTimeout(function() {
        location.reload();
    },1000*60*30)
}

function loginFailMessage(){
    var loginState = document.getElementById("loginTracker");
    loginState.textContent = "Login failed!";
    setTimeout(function() {
        loginState.textContent = "Not logged in";
    },1000*60*30)
}

function register() {
    var un = document.registerform.regUsr.value;
    var pw = document.registerform.regPass.value;
    sendRequest("POST","rest/shop/register","user=" + un + "&password=" +pw, function(response){
        if(response == "success"){
            alert("Profile created.");
            document.getElementById("register").style.display = "none";
            setTimeout(function () {
                location.reload();
            }, 1000*30);
        } else if(response == "failure"){
            alert("Error: Username already taken. Please try again.");
        }
    });
}

$(document).ready(function () {
    $("#logButton").click(function(){
        $("#loginForm").toggle(500);
    });
});

$(document).ready(function () {
    $("#regbutton").click(function(){
        $("#registerForm").toggle(500);
    });
});
