const box = document.querySelector(".box");
const buttons = document.querySelectorAll("button");

buttons[0].onclick = () => {
        box.classList.remove("bc-red");
        box.classList.remove("bc-pink");
}

buttons[1].onclick = () => {
    box.classList.remove("bc-red");
    box.classList.remove("bc-pink");

    box.classList.add("bc-red");
}

buttons[2].onclick = () => {
    box.classList.remove("bc-red");
    box.classList.remove("bc-pink");

    box.classList.add("bc-pink");
}


