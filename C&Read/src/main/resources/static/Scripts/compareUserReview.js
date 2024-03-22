function isCurrentUser(reviewUserName, currentUserName) {
    console.log("He entrado en la funcion de isCUrrentUSER");

    if (reviewUserName == currentUserName){
        console.log("VERDADERO, IGUALES LOCO");
    } else {
        console.log("FALSO, NO SE PARECEN MAMON")
    }
    return reviewUserName === currentUserName;
    //return reviewUserName.equals(currentUserName);
}
