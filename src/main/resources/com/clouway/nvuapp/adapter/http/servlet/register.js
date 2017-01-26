function registerAcc() {
    var id = document.registration.TUTOR_ID;
    var password = document.registration.PASSWORD;
    var confirmPassword = document.registration.CONFIRMPASSWORD;
    var message = document.getElementById("message");

    if ((!id_validate(id, 5)) ||
        (!password_validate(password, 5, 10) ||
        (!confirm_password(password, confirmPassword)))) {
        return false;
    }

    function id_validate(id, length) {
        var letters = /[0-9a-zA-Z]+$/;
        var idLength = id.value.length;
        if (idLength > length || idLength < length) {
            message.innerHTML = "Дължината на ID трябва да бъде точно " + length + " символа.";
            return false;
        }
        if (!id.value.match(letters)) {
            message.innerHTML = "Позволени са само букви и цифри.";
            return false;
        }
        return true;
    }

    function password_validate(password, x, y) {
        var letters = /[0-9a-zA-Z]+$/;
        var passlength = password.value.length;
        if (passlength > y || passlength < x) {
            message.innerHTML = "Дължината на паролата трябва да бъде между " + x + " и " + y + " символа.";
            return false;
        }
        if (!password.value.match(letters)) {
            message.innerHTML = "Позволени са само букви и цифри.";
            return false;
        }
        return true;
    }

    function confirm_password(password, confirmPassword) {
        var passValue = password.value;
        var confPassValue = confirmPassword.value;
        if (!passValue.match(confPassValue) || confPassValue.length != passValue.length) {
            message.innerHTML = "Паролите трябва да са еднакви.";
            confirmPassword.focus();
            return false;
        }
        return true;
    }
}