function setcolor(){
    var x = document.getElementsByName("selectedColor");

    for (i = 0; i < x.length; i++) {
        if (x[i].checked)
            document.getElementById("setcolor2").value = x[i].value;
        document.getElementById("setcolor1").value = x[i].value;
    }

}

$( "#formAddCart" ).on( "submit", function( event ) {
    var color = document.getElementById("setcolor2").value;
    if(color == ""){
        alert("Vui lòng chọn màu sắc");
        event.preventDefault();
    }

});


$( "#formBuyNow" ).on( "submit", function( event ) {
    var color = document.getElementById("setcolor1").value;
    if(color == ""){
        alert("Vui lòng chọn màu sắc");
        event.preventDefault();
    }

});

function changeMainImage(src) {
    var mainImage = document.getElementById("mainImage");
    mainImage.src = src;
}
document.addEventListener("DOMContentLoaded", function () {
    new Zooming({
        enableGrab: false, // Disable grab-to-drag
    }).listen("#mainImage");
});
// Format the price as VND with the symbol "₫"
document.addEventListener('DOMContentLoaded', () => {
    const priceElements = document.querySelectorAll('#price');
    priceElements.forEach(element => {
        const price = element.textContent;
        const formattedPrice = formatCurrencyVND(price);
        element.textContent = formattedPrice;
    });
});

// Function to format the price as VND
function formatCurrencyVND(price) {
    const formatter = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });
    return formatter.format(price);
}