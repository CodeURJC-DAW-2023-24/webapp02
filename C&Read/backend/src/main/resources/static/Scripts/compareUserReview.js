function isCurrentUser(reviewUserName, currentUserName) {
    console.log("He entrado en la funcion de isCUrrentUSER");

    if (reviewUserName == currentUserName){
        console.log("Same Names: TRUE");
    } else {
        console.log("Wrong Names: FALSE")
    }
    return reviewUserName === currentUserName;
    //return reviewUserName.equals(currentUserName);
}
